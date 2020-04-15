package xyz.neonkid.simplememoj.main.view.MemoEdit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.adapter.MemoImageRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.component.anim.RevealAnimation;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.MemoEditPresenter;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.view.MemoEditPresenterView;
import xyz.neonkid.simplememoj.main.util.MemoCode;
import xyz.neonkid.simplememoj.main.view.MemoEdit.fragment.MemoEditLeftBSFragment;
import xyz.neonkid.simplememoj.main.view.MemoEdit.fragment.MemoEditRightBSFragment;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoEditActivity extends BaseActivity implements MemoEditPresenterView,
        BottomAppBar.OnClickListener, OnListItemClickListener, RealmChangeListener<Memo>,
        MemoEditLeftBSFragment.OnDataSetListener, MemoEditRightBSFragment.OnDataSetListener, TextWatcher {

    private MemoEditLeftBSFragment leftFragment;
    private MemoEditRightBSFragment rightFragment;

    private MemoImageRealmRecyclerAdapter adapter;
    private MemoEditPresenter presenter;
    private Memo memo;

    private RevealAnimation animation;
    private String curColor;

    @BindView(R.id.memo_img)
    RecyclerView memoImg;

    @BindView(R.id.memo_edit_layout)
    ConstraintLayout memoEditLayout;

    @BindView(R.id.title_edit)
    EditText titleEdit;

    @BindView(R.id.content_edit)
    EditText contentEdit;

    @BindView(R.id.memo_edit_bar)
    BottomAppBar memoEditbar;

    // TEMP IMAGE
    private String mImageUri;

    // AutoSave event
    private Handler asHandler = new Handler();
    private Runnable autoSave;
    private long last_edited_time = 0;
    private long autoSaveTime = 7000;
    private boolean saved = true;

    @Override
    public void onBackPressed() {
        if (animation != null)
            animation.unRevealActivity();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate() {
        setSupportActionBar(memoEditbar);

        presenter = new MemoEditPresenter(this, this);

        dataLoading();

        memoEditbar.setNavigationOnClickListener(this);
        memoImg.setAdapter(adapter);

        leftFragment = MemoEditLeftBSFragment.getInstance();
        rightFragment = MemoEditRightBSFragment.getInstance();

        autoSave = () -> {
            if (System.currentTimeMillis() > (last_edited_time + (autoSaveTime - 500))) {
                presenter.insertMemo(memo.getId(), titleEdit.getText().toString(),
                        contentEdit.getText().toString(), curColor, null);
            }
            saved = true;
            asHandler.removeCallbacks(autoSave);
        };

        titleEdit.addTextChangedListener(this);
        contentEdit.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        asHandler.removeCallbacks(autoSave);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asHandler.removeCallbacks(autoSave);

        titleEdit.removeTextChangedListener(this);
        contentEdit.removeTextChangedListener(this);

        if (isAllempty())
            presenter.deleteMemo(memo.getId());
        else if (!saved)
            presenter.insertMemo(memo.getId(), titleEdit.getText().toString(), contentEdit.getText().toString(), curColor, null);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_memo_edit;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }

    /**
     * 이 함수에서 이미지의 삽입과 삭제를 담당합니다.
     * MemoCode에 정의되어 있는 Request와 Result 상수를 통해 데이터를 전달받게 되며,
     *
     * 카메라, 갤러리에서는 내장 스토리지에 저장된 이미지 주소를,
     * URL은 사용자가 입력한 URL의 주소를 받아 DB에 저장합니다.
     *
     * 이미지의 삭제는 ImageEditActivity에서 삭제 명령을 통해 전달받게 되며,
     * Realm DB에 저장된 Image ID 값을 받아 삭제하게 됩니다.
     *
     * @param requestCode Request code
     * @param resultCode Result code
     * @param data Intent data
     *
     * @see MemoCode
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final String title = titleEdit.getText().toString();
        final String content = contentEdit.getText().toString();
        int curId = memo != null ? memo.getId() : 0;

        switch (requestCode) {
            case MemoCode.MemoRequest.CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.insertMemo(curId, title, content, curColor, new MemoImage(mImageUri));
                    saved = true;
                }
                break;

            case MemoCode.MemoRequest.PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {
                            String path = presenter.getContentImageCopyPath(curId, data.getData());
                            presenter.insertMemo(curId, title, content, curColor, new MemoImage(path));
                            saved = true;
                        } catch (IOException ex) {
                            setToastMessage(ex.getMessage());
                        }
                    }
                }
                break;

            case MemoCode.MemoRequest.PREVIEW_IMAGE:
                if (resultCode == MemoCode.MemoResult.DELETED_IMAGE) {
                    if (data != null) {
                        int id = data.getIntExtra("imageId", 0);
                        presenter.deleteMemoImage(id);
                    }
                }
                break;
        }
    }

    private boolean isAllempty() {
        return titleEdit.getText().toString().equals("") &&
                contentEdit.getText().toString().equals("") && adapter.getItemList().isEmpty();
    }

    /**
     * MemoEditActivity에서는 새로운 메모의 저장과 기존 메모의 편집을 담당합니다.
     * Memo ID 값은 1부터 시작하기 때문에, 0을 받게 되면 새로운 메모로 인식하도록 구현하였습니다.
     *
     * memo 객체에 지정된 ChangeListener는 해당 메모의 내용이 DB에서 Transaction이 발생하는 경우,
     * 호출하는 인터페이스를 지정합니다.
     */
    private void dataLoading() {
        Intent intent = getIntent();

        if (intent.hasExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y))
            animation = new RevealAnimation(memoEditLayout, intent, this);

        int curId = intent.getIntExtra("memoId", 0);

        // 메모가 존재하지 않으면, 빈 메모 생성
        if (curId > 0)
            memo = presenter.getMemoWithId(curId);
        else {
            curColor = getString(R.color.colorPrimary);
            presenter.insertMemo(curId, "", "", curColor, null);

            RealmResults<Memo> memos = presenter.loadMemos();
            if (memos.isLoaded())
                memo = memos.last();
        }

        if (memo != null) {
            adapter = new MemoImageRealmRecyclerAdapter(this, this,
                    memo.getImgsResult(), true);

            renderMemo(memo);

            memo.addChangeListener(this);
        }
    }

    private boolean isValidURL(String url) {
        if (url.isEmpty())
            return false;

        return Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * DB에서 지정된 메모에 Transaction 이벤트가 발생하면,
     * 호출되는 콜백 함수입니다.
     *
     * @param memo 지정된 메모
     */
    @Override
    public void onChange(Memo memo) {
        if (memo.isValid())
            renderMemo(memo);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 키보드에서 입력이 발생하면, 메모의 내용이 변화된 것으로 인식하고, 저장 플래그가 Off 되며,
     * 실행된 Timer는 초기화 되고, 새로운 Timer가 실행됩니다.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        saved = false;
        asHandler.removeCallbacks(autoSave);
    }

    /**
     * 입력이 멈추면, Timer가 실행되며 정해진 시간이 지나면, 자동 저장 이벤트가 발생합니다.
     */
    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && !saved) {
            last_edited_time = System.currentTimeMillis();
            asHandler.postDelayed(autoSave, autoSaveTime);
        }
    }

    /**
     * 하단 바 왼쪽 이미지 추가 버튼을 클릭했을 때 발생하는 콜백 함수입니다.
     */
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        if (memo != null)
            bundle.putInt(Memo.FIELD_ID, memo.getId());
        else
            bundle.putInt(Memo.FIELD_ID, 0);

        leftFragment.setArguments(bundle);
        leftFragment.show(getSupportFragmentManager(), "LEFT_MENU");
    }

    /**
     * 하단 바 오른쪽 메모 색상 변경 버튼을 클릭했을 때 발생하는 콜백 함수입니다.
     */
    @OnClick(R.id.memo_color_button)
    public void memoColorBtnClick() {
        rightFragment.show(getSupportFragmentManager(), "RIGHT_MENU");
    }

    /**
     * 카메라 촬영을 통해 이미지를 받을 경우,
     * 최종 저장된 URL을 저장하는 콜백 함수입니다.
     *
     * @param file 카메라로 촬영된 이미지
     */
    @Override
    public void onFileSet(File file) {
        mImageUri = file.getAbsolutePath();
    }

    /**
     * URL 입력 alert을 이용해 사용자로부터 전달 받은
     * URL을 받아오는 콜백 함수입니다.
     *
     * @param url URL Alert에 입력된 URL
     */
    @Override
    public void onURLSet(String url) {
        final String title = titleEdit.getText().toString();
        final String content = contentEdit.getText().toString();
        int curId = memo != null ? memo.getId() : 0;

        if (!isValidURL(url)) {
            setToastMessage(getString(R.string.alert_invalid_url));
            return;
        }

        presenter.insertMemo(curId, title, content, curColor, new MemoImage(url));
    }

    /**
     * Color Chooser Dialog에서 선택한 색상값을
     * 문자열 형태로 가져오는 콜백 함수입니다.
     *
     * @param color 색상 값
     */
    @Override
    public void onColorSet(String color) {
        memoEditLayout.setBackgroundColor(Color.parseColor(color));
        memoEditbar.setBackgroundColor(Color.parseColor(color));
        setToolbarColor(color);
        setStatusBarColor(color);

        curColor = color;

        saved = false;
    }

    @Override
    public void renderMemo(Memo memo) {
        titleEdit.setText(memo.getTitle());
        contentEdit.setText(memo.getContent());

        onColorSet(memo.getColor());
    }

    @Override
    public void setToast(String message) {
        setToastMessage(message);
    }

    /**
     * 메모에 저장된 이미지를 클릭했을 때,
     * 발생하는 콜백 함수입니다.
     *
     * 저장된 모든 이미지를 한 번에 보여주기 위해서,
     * adapter로부터 현재 가지고 있는 이미지를 리스트로 받고, 넘겨줍니다.
     *
     * @param pos 클릭한 이미지의 위치
     */
    @Override
    public void onItemClick(View v, int pos) {
        ArrayList<MemoImage> imgList = new ArrayList<>(adapter.getItemList());

        Intent intent = new Intent(this, ImageEditActivity.class);

        intent.putExtra("imgPos", pos);
        intent.putParcelableArrayListExtra("imgList", imgList);

        startActivityForResult(intent, MemoCode.MemoRequest.PREVIEW_IMAGE);
    }
}

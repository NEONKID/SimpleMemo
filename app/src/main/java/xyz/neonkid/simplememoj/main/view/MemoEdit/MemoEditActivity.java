package xyz.neonkid.simplememoj.main.view.MemoEdit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.main.adapter.MemoImageRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.component.anim.RevealAnimation;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;
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
                        contentEdit.getText().toString(), curColor, null, true);
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
            presenter.insertMemo(memo.getId(), titleEdit.getText().toString(),
                    contentEdit.getText().toString(), curColor, null, true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_memo_edit;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final String title = titleEdit.getText().toString();
        final String content = contentEdit.getText().toString();
        int curId = memo != null ? memo.getId() : 0;

        switch (requestCode) {
            case MemoCode.MemoRequest.CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.insertMemo(curId, title, content, curColor,
                            new MemoImage(mImageUri), true);
                    saved = true;
                }
                break;

            case MemoCode.MemoRequest.PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {
                            String path = presenter.getContentImageCopyPath(curId, data.getData());
                            presenter.insertMemo(curId, title, content, curColor,
                                    new MemoImage(path), true);
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

    private void dataLoading() {
        Intent intent = getIntent();

        if (intent.hasExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y))
            animation = new RevealAnimation(memoEditLayout, intent, this);

        int curId = intent.getIntExtra("memoId", 0);

        if (curId > 0)
            memo = presenter.getMemoWithId(curId);
        else {
            curColor = getString(R.color.colorPrimary);
            presenter.insertMemo(curId, "", "", curColor, null, false);
            memo = presenter.loadMemos().last();
        }

        if (memo != null) {
            adapter = new MemoImageRealmRecyclerAdapter(this, this,
                    memo.getImgsResult(), true);

            renderMemo(memo);

            memo.addChangeListener(this);
        }
    }

    @Override
    public void onChange(Memo memo) {
        if (memo.isValid())
            renderMemo(memo);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        saved = false;
        asHandler.removeCallbacks(autoSave);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && !saved) {
            last_edited_time = System.currentTimeMillis();
            asHandler.postDelayed(autoSave, autoSaveTime);
        }
    }

    // Image 메뉴 띄우기
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

    @OnClick(R.id.memo_color_button)
    public void memoColorBtnClick() {
        rightFragment.show(getSupportFragmentManager(), "RIGHT_MENU");
    }

    // Fragment 로부터 찍은 사진의 File 을 가져옴
    @Override
    public void onFileSet(File file) {
        mImageUri = file.getAbsolutePath();
    }

    // Fragment 로부터 URI 값을 가져옴
    @Override
    public void onURISet(String uri) {
        final String title = titleEdit.getText().toString();
        final String content = contentEdit.getText().toString();
        int curId = memo != null ? memo.getId() : 0;

        presenter.insertMemo(curId, title, content, curColor, new MemoImage(uri), true);
    }

    // Fragment 로부터 Color 값을 가져옴
    @Override
    public void onColorSet(String color) {
        memoEditLayout.setBackgroundColor(Color.parseColor(color));
        memoEditbar.setBackgroundColor(Color.parseColor(color));
        setToolbarColor(color);
        setStatusBarColor(color);

        curColor = color;

        saved = false;
    }

    // 메모 내용 렌더링
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

    // Image 를 클릭했을 때 이벤트
    @Override
    public void onItemClick(View v, int pos) {
        ArrayList<MemoImage> imgList = new ArrayList<>(adapter.getItemList());

        Intent intent = new Intent(this, ImageEditActivity.class);

        intent.putExtra("imgPos", pos);
        intent.putParcelableArrayListExtra("imgList", imgList);

        startActivityForResult(intent, MemoCode.MemoRequest.PREVIEW_IMAGE);
    }
}

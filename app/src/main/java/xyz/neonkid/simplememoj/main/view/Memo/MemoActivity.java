package xyz.neonkid.simplememoj.main.view.Memo;

import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.adapter.MemoImageRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.component.dialog.AlertDialogFragment;
import xyz.neonkid.simplememoj.main.component.view.AutoSizeRecyclerView;
import xyz.neonkid.simplememoj.main.presenter.Memo.MemoPresenter;
import xyz.neonkid.simplememoj.main.presenter.Memo.view.MemoPresenterView;
import xyz.neonkid.simplememoj.main.util.MemoCode;
import xyz.neonkid.simplememoj.main.view.MemoEdit.MemoEditActivity;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoActivity extends BaseActivity implements MemoPresenterView,
        RealmChangeListener<Memo>, OnListItemClickListener {
    private MemoImageRealmRecyclerAdapter adapter;
    private MemoPresenter presenter;
    private Memo cur;

    @BindView(R.id.memo_view_layout)
    ConstraintLayout memoViewLayout;

    @BindView(R.id.memoView_img)
    AutoSizeRecyclerView memoViewImg;

    @BindView(R.id.memoView_title)
    TextView memoViewTitle;

    @BindView(R.id.memoView_content)
    TextView memoViewContent;

    @BindView(R.id.memo_view_bar)
    BottomAppBar memoViewBar;

    @BindView(R.id.memo_modified)
    TextView memoModified;

    @Override
    protected void onCreate() {
        presenter = new MemoPresenter(this, this);

        dataLoading();

        memoViewImg.setAdapter(adapter);
    }

    private void dataLoading() {
        Intent memoViewIntent = getIntent();

        int curId = memoViewIntent.getIntExtra("memoId", 0);

        if (curId > 0)
            cur = presenter.getMemoWithId(curId);

        if (cur.isLoaded() && cur.isValid()) {
            RealmResults<MemoImage> imgs = cur.getImgsResult();
            if (imgs.isLoaded()) {
                adapter = new MemoImageRealmRecyclerAdapter(this, this, imgs, true);
                cur.addChangeListener(this);
            }
        }

        renderMemo(cur);
    }

    @Override
    public void onChange(Memo memo) {
        if (memo.isValid())
            renderMemo(memo);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_memo;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.memo_mod:
                Intent memoEditIntent = new Intent(this, MemoEditActivity.class);
                memoEditIntent.putExtra("memoId", cur.getId());
                startActivityForResult(memoEditIntent, MemoCode.MemoRequest.EDIT_MEMO);
                break;

            case R.id.memo_delete:
                AlertDialogFragment.newInstance(() -> {
                    presenter.deleteMemo(cur.getId());
                    finish();
                }, getString(R.string.alert_delete)).show(getSupportFragmentManager(), "MEMO_DEL_ALERT");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderMemo(Memo memo) {
        if (memo != null) {
            String modMsg = presenter.convertModTimeString(memo.getModified());

            memoViewTitle.setText(memo.getTitle());
            memoViewContent.setText(memo.getContent());
            memoModified.setText(modMsg);

            setMemoColor(memo.getColor());
        }
    }

    @Override
    public void onItemClick(View v, int pos) {
        ArrayList<MemoImage> imgList = new ArrayList<>(adapter.getItemList());

        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("imgPos", pos);
        intent.putParcelableArrayListExtra("imgList", imgList);

        startActivityForResult(intent, MemoCode.MemoRequest.PREVIEW_IMAGE);
    }

    private void setMemoColor(String color) {
        memoViewLayout.setBackgroundColor(Color.parseColor(color));
        memoViewBar.setBackgroundColor(Color.parseColor(color));

        setStatusBarColor(color);
        setToolbarColor(color);
    }

    @Override
    public void setToast(String msg) {
        setToastMessage(msg);
    }
}

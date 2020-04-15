package xyz.neonkid.simplememoj.main.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import io.realm.RealmResults;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.base.adapter.listener.OnItemChangeListener;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.adapter.MemoRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.component.anim.RevealAnimation;
import xyz.neonkid.simplememoj.main.presenter.MainPresenter;
import xyz.neonkid.simplememoj.main.presenter.view.MainPresenterView;
import xyz.neonkid.simplememoj.main.util.MemoCode;
import xyz.neonkid.simplememoj.main.view.Memo.MemoActivity;
import xyz.neonkid.simplememoj.main.view.MemoEdit.MemoEditActivity;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MainActivity extends BaseActivity implements MainPresenterView,
        OnListItemClickListener, OnItemChangeListener, View.OnClickListener {
    private MainPresenter presenter;
    private MemoRealmRecyclerAdapter adapter;

    @BindView(R.id.memo_view)
    RecyclerView memoView;

    @BindView(R.id.memo_counter)
    TextView memoCounter;

    @BindView(R.id.memo_add)
    FloatingActionButton memoAdd;

    @Override
    protected void onCreate() {
        presenter = new MainPresenter(this, this);

        RealmResults<Memo> memos = presenter.loadMemos();
        if (memos.isLoaded()) {
            adapter = new MemoRealmRecyclerAdapter(this, this, this, memos);

            memoView.setAdapter(adapter);
            memoAdd.setOnClickListener(this);

            onChange();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMainColor();
    }

    /**
     * Realm DB에서 메모가 추가, 수정, 삭제 등의 변경사항이 발생되었을 경우,
     * 호출되는 콜백 함수입니다.
     *
     * 이 함수는 MemoRealmRecyclerAdapter와 연동되어 있습니다.
     */
    @Override
    public void onChange() {
        int cnt = adapter.getItemCount();

        String bottomText = getString(R.string.no_memo);

        if (cnt == 1)
            bottomText = cnt + getString(R.string.memo_single_count);
        else if (cnt > 1)
            bottomText = cnt + getString(R.string.memo_multi_count);

        memoCounter.setText(bottomText);
    }

    /**
     * 각 메모를 클릭했을 때 발생하는 콜백 함수입니다.
     * Realm DB에 저장된 메모의 ID를 넘겨줍니다.
     *
     * @param v view
     * @param pos memo position
     */
    @Override
    public void onItemClick(View v, int pos) {
        Memo choice = adapter.getItemList().get(pos);

        Intent intent = new Intent(this, MemoActivity.class);
        intent.putExtra("memoId", choice.getId());

        startActivityForResult(intent, MemoCode.MemoRequest.PREVIEW_MEMO);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.app_name;
    }

    @Override
    public void onClick(View v) {
        startRevealActivity(v);
    }

    private void startRevealActivity(View v) {
        // calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        // create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, MemoEditActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        // just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        // to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    private void setMainColor() {
        setStatusBarColor(getString(R.color.colorPrimary));
        setToolbarColor(getString(R.color.colorPrimary));
    }
}

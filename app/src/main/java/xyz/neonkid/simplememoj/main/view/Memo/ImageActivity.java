package xyz.neonkid.simplememoj.main.view.Memo;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.main.adapter.MemoImagePagerRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.presenter.Memo.ImagePresenter;
import xyz.neonkid.simplememoj.main.presenter.Memo.view.ImagePresenterView;

/**
 * Created by Neon K.I.D on 2/20/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class ImageActivity extends BaseActivity implements ImagePresenterView, OnListItemClickListener {
    private ImagePresenter presenter;
    private MemoImagePagerRecyclerAdapter adapter;

    @BindView(R.id.img_pager)
    ViewPager2 imgPager;

    private ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            setPagerStatus(position);
        }
    };

    private void setPagerStatus(int pos) {
        String title = (pos + 1) + " / " + adapter.getItemCount();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate() {
        presenter = new ImagePresenter(this, this);

        dataLoading();

        setToolbarColor(getString(R.color.colorPVImgToolbar));
    }

    private void dataLoading() {
        Intent memoViewIntent = getIntent();

        ArrayList<MemoImage> list = memoViewIntent.getParcelableArrayListExtra("imgList");
        int imgPos = memoViewIntent.getIntExtra("imgPos", 0);

        adapter = new MemoImagePagerRecyclerAdapter(this, this);

        if (list != null) {
            for (MemoImage img : list)
                adapter.addImage(img);
        }

        imgPager.setAdapter(adapter);
        imgPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        imgPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        imgPager.setCurrentItem(imgPos, false);
        imgPager.registerOnPageChangeCallback(callback);

        setPagerStatus(imgPos);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }

    @Override
    public void onItemClick(View v, int pos) {
        if (Objects.requireNonNull(getSupportActionBar()).isShowing())
            getSupportActionBar().hide();
        else
            getSupportActionBar().show();
    }
}

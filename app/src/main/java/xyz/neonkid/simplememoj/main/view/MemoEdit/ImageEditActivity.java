package xyz.neonkid.simplememoj.main.view.MemoEdit;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.main.adapter.MemoImagePagerRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.component.dialog.AlertDialogFragment;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.ImageEditPresenter;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.view.ImageEditPresenterView;
import xyz.neonkid.simplememoj.main.util.MemoCode;

/**
 * Created by Neon K.I.D on 2/17/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class ImageEditActivity extends BaseActivity implements ImageEditPresenterView, OnListItemClickListener {
    private ImageEditPresenter presenter;
    private MemoImagePagerRecyclerAdapter adapter;
    private Intent memoEditIntent;
    private int curImgId;

    @BindView(R.id.img_edit_pager)
    ViewPager2 imgEditPager;

    private ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            setPagerStatus(position);
        }
    };

    @Override
    protected void onCreate() {
        presenter = new ImageEditPresenter(this, this);

        dataLoading();

        setToolbarColor(getString(R.color.colorPVImgToolbar));
    }

    private void setPagerStatus(int pos) {
        curImgId = adapter.getItemList().get(pos).getId();
        String title = (pos + 1) + " / " + adapter.getItemCount();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private void dataLoading() {
        memoEditIntent = getIntent();

        ArrayList<MemoImage> list = memoEditIntent.getParcelableArrayListExtra("imgList");
        int imgPos = memoEditIntent.getIntExtra("imgPos", 0);

        adapter = new MemoImagePagerRecyclerAdapter(this, this);

        if (list != null) {
            curImgId = list.get(imgPos).getId();
            for (MemoImage img : list)
                adapter.addImage(img);
        }

        imgEditPager.setAdapter(adapter);
        imgEditPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        imgEditPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        imgEditPager.setCurrentItem(imgPos, false);
        imgEditPager.registerOnPageChangeCallback(callback);

        setPagerStatus(imgPos);
    }

    @Override
    public void onItemClick(View v, int pos) {
        if (Objects.requireNonNull(getSupportActionBar()).isShowing())
            getSupportActionBar().hide();
        else
            getSupportActionBar().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.img_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.img_delete) {
            AlertDialogFragment.newInstance(() -> {
                memoEditIntent.putExtra("imageId", curImgId);
                setResult(MemoCode.MemoResult.DELETED_IMAGE, memoEditIntent);
                finish();
            }, getString(R.string.alert_delete)).show(getSupportFragmentManager(), "MEMO_IMG_DEL_ALERT");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_edit;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }
}

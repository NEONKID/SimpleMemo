package xyz.neonkid.simplememoj.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.adapter.MemoImagePagerRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;

/**
 * Created by Neon K.I.D on 2/27/20
 *
 * 이미지 Activity에 사용할 공통 코드를 정의한 클래스입니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseImageActivity extends BaseActivity implements OnListItemClickListener {
    protected MemoImagePagerRecyclerAdapter adapter;
    protected Intent intent;
    protected int curImgId;

    @BindView(R.id.img_pager)
    ViewPager2 imgPager;

    private ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            setPagerStatus(position);
        }
    };

    /**
     * 화면에 띄워진 이미지가 메모의 몇 번째 이미지인지를 반환하는 함수입니다.
     *
     * @param pos 화면에 띄워진 이미지의 위치
     */
    private void setPagerStatus(int pos) {
        String title = (pos + 1) + " / " + adapter.getItemCount();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataLoading();

        setToolbarColor(getString(R.color.colorPVImgToolbar));
    }

    private void dataLoading() {
        intent = getIntent();

        ArrayList<MemoImage> list = intent.getParcelableArrayListExtra("imgList");
        int imgPos = intent.getIntExtra("imgPos", 0);

        adapter = new MemoImagePagerRecyclerAdapter(this, this);

        if (list != null) {
            curImgId = list.get(imgPos).getId();
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
    public void onItemClick(View v, int pos) {
        if (Objects.requireNonNull(getSupportActionBar()).isShowing())
            getSupportActionBar().hide();
        else
            getSupportActionBar().show();
    }
}

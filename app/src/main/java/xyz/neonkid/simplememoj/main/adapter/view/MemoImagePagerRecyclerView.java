package xyz.neonkid.simplememoj.main.adapter.view;

import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.simplememoj.main.adapter.MemoImagePagerRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoImagePagerRecyclerView extends BaseRecyclerView<MemoImagePagerRecyclerAdapter, MemoImage> {
    @BindView(R.id.multi_img_view)
    PhotoView multiImgView;

    public MemoImagePagerRecyclerView(ViewGroup parent, MemoImagePagerRecyclerAdapter adapter) {
        super(R.layout.content_memo_preimage, parent, adapter);
    }

    @Override
    public void onViewHolder(@Nullable MemoImage item, int position) {
        if (item != null)
            Glide.with(getContext())
                    .load(item.getUri()).into(multiImgView);
    }
}

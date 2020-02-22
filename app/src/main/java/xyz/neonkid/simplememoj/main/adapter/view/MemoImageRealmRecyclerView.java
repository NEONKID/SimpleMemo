package xyz.neonkid.simplememoj.main.adapter.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRealmRecyclerView;
import xyz.neonkid.simplememoj.main.adapter.MemoImageRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;

/**
 * Created by Neon K.I.D on 2/19/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoImageRealmRecyclerView extends BaseRealmRecyclerView<MemoImageRealmRecyclerAdapter, MemoImage> {
    @BindView(R.id.memo_imgModel)
    ImageView memoImgModel;

    public MemoImageRealmRecyclerView(ViewGroup parent, MemoImageRealmRecyclerAdapter adapter) {
        super(R.layout.content_memo_image, parent, adapter);
    }

    @Override
    public void onViewHolder(@Nullable MemoImage item, int position) {
        if (item != null) {
            Glide.with(getContext()).load(item.getUri())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .fallback(new ColorDrawable(Color.GRAY))
                    .into(memoImgModel);
        }
    }
}

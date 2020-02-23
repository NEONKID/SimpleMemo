package xyz.neonkid.simplememoj.main.adapter.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import io.realm.RealmResults;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRealmRecyclerView;
import xyz.neonkid.simplememoj.main.adapter.MemoRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoRealmRecyclerView extends BaseRealmRecyclerView<MemoRealmRecyclerAdapter, Memo> {
    @BindView(R.id.memo_card)
    CardView memoCard;

    @BindView(R.id.memo_img_thumbnail)
    ImageView memoImgThumbnail;

    @BindView(R.id.memo_title)
    TextView memoTitle;

    @BindView(R.id.memo_content)
    TextView memoContent;

    public MemoRealmRecyclerView(ViewGroup parent, MemoRealmRecyclerAdapter adapter) {
        super(R.layout.content_memomodel, parent, adapter);
    }

    @Override
    public void onViewHolder(@Nullable Memo item, int position) {
        if (item != null) {
            RealmResults<MemoImage> imgs = item.getImgsResult();

            if (!imgs.isEmpty()) {
                MemoImage mi = imgs.first();
                if (mi != null)
                    Glide.with(getContext()).load(mi.getUri())
                            .placeholder(R.drawable.im_placeholder)
                            .error(R.drawable.im_error)
                            .fitCenter()
                            .fallback(new ColorDrawable(Color.GRAY))
                            .into(memoImgThumbnail);
            } else memoImgThumbnail.setImageResource(0);

            memoTitle.setText(item.getTitle());
            memoContent.setText(item.getSummary());
            memoCard.setCardBackgroundColor(Color.parseColor(item.getColor()));
        }
    }
}

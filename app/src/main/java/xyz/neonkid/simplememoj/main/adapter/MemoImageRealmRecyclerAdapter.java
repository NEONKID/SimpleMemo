package xyz.neonkid.simplememoj.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.realm.OrderedRealmCollection;
import xyz.neonkid.simplememoj.base.adapter.BaseRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImageModel;
import xyz.neonkid.simplememoj.main.adapter.view.MemoImageRealmRecyclerView;

/**
 * Created by Neon K.I.D on 2/19/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoImageRealmRecyclerAdapter extends BaseRealmRecyclerAdapter<MemoImage, MemoImageRealmRecyclerView> implements MemoImageModel {
    public MemoImageRealmRecyclerAdapter(Context context, OnListItemClickListener listener,
                                         @Nullable OrderedRealmCollection<MemoImage> data,
                                         boolean autoUpdate) {
        super(context, listener, null, data, autoUpdate);
    }

    @NonNull
    @Override
    public MemoImageRealmRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoImageRealmRecyclerView(parent, this);
    }

    @Override
    public void addImage(MemoImage img) {
        addItem(img, true);
    }
}

package xyz.neonkid.simplememoj.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import xyz.neonkid.simplememoj.base.adapter.BaseRecyclerAdapter;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImageModel;
import xyz.neonkid.simplememoj.main.adapter.view.MemoImagePagerRecyclerView;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoImagePagerRecyclerAdapter extends BaseRecyclerAdapter<MemoImage> implements MemoImageModel {
    public MemoImagePagerRecyclerAdapter(Context context, OnListItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void addItem(MemoImage data) {
        addItem(data, false);
    }

    @NonNull
    @Override
    public BaseRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoImagePagerRecyclerView(parent, this);
    }

    @Override
    public void addImage(MemoImage img) {
        addItem(img);
    }
}

package xyz.neonkid.simplememoj.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import xyz.neonkid.simplememoj.base.adapter.BaseRecyclerAdapter;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.simplememoj.main.adapter.model.MemoColor;
import xyz.neonkid.simplememoj.main.adapter.model.MemoColorModel;
import xyz.neonkid.simplememoj.main.adapter.view.MemoColorRecyclerView;

/**
 * Created by Neon K.I.D on 2/20/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoColorRecyclerAdapter extends BaseRecyclerAdapter<MemoColor> implements MemoColorModel {
    public MemoColorRecyclerAdapter(Context context, OnListItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public BaseRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoColorRecyclerView(parent, this);
    }

    @Override
    protected void addItem(MemoColor data) {
        addItem(data, true);
    }

    @Override
    public void addMemoColor(MemoColor mc) {
        addItem(mc);
    }
}

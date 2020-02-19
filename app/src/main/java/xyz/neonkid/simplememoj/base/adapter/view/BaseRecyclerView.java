package xyz.neonkid.simplememoj.base.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;
import xyz.neonkid.simplememoj.base.adapter.BaseRecyclerAdapter;

public abstract class BaseRecyclerView<AD extends BaseRecyclerAdapter, T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private AD adapter;

    public BaseRecyclerView(AD adapter, View itemView) {
        super(itemView);
        this.adapter = adapter;

        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public BaseRecyclerView(@LayoutRes int layoutRes, ViewGroup parent, AD adapter) {
        this(adapter, LayoutInflater.from(adapter.getContext()).inflate(layoutRes, parent, false));
    }

    public abstract void onViewHolder(final @Nullable T item, int position);

    protected AD getAdapter() {
        return adapter;
    }

    protected Context getContext() {
        return adapter.getContext();
    }

    @Override
    public void onClick(View v) {
        int pos = getLayoutPosition() + 1;
        adapter.getListener().onItemClick(v, pos);
    }
}

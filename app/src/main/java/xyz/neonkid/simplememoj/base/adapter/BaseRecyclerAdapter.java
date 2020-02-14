package xyz.neonkid.simplememoj.base.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.neonkid.simplememoj.base.adapter.view.BaseRecyclerView;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerView> {
    private Context context;

    private List<T> itemList;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerView holder, int position) {
        holder.onViewHolder(getItem(position), position);
    }

    protected abstract void addItem(T data);

    protected void addItem(@Nullable T data, boolean isNotify) {
        itemList.add(data);
        if (isNotify)
            notifyItemChanged(data);
    }

    @Nullable
    protected T getItem(int position) {
        return itemList != null ? (T)itemList.get(position) : null;
    }

    protected void notifyItemChanged(T item) {
        if (getItemList() != null) {
            int idx = getItemList().indexOf(item);
            notifyItemChanged(idx);
        }
    }

    public Context getContext() {
        return context;
    }

    public List<T> getItemList() {
        return itemList;
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}

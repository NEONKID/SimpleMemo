package xyz.neonkid.simplememoj.base.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.neonkid.simplememoj.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * RecyclerView Adapter의 기본 코드를 정의해 놓은 클래스입니다.
 *
 * 이 클래스는 Realm DB와 연동되지 않는 클래스이므로,
 * Realm DB를 사용하지 않는 일반 RecyclerView 구현시 사용해주십시오.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerView> {
    private Context context;
    private List<T> itemList;
    private OnListItemClickListener mListener;

    public BaseRecyclerAdapter(Context context, OnListItemClickListener listener) {
        this.context = context;
        this.itemList = new ArrayList<>();
        this.mListener = listener;
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

    public OnListItemClickListener getListener() { return mListener; }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}

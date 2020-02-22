package xyz.neonkid.simplememoj.base.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmRecyclerViewAdapter;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRealmRecyclerView;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseRealmRecyclerAdapter<T extends RealmObject, VH extends BaseRealmRecyclerView>
        extends RealmRecyclerViewAdapter<T, VH> {
    private Context context;
    private OrderedRealmCollection<T> results;
    private OnListItemClickListener mListener;

    public BaseRealmRecyclerAdapter(Context context, OnListItemClickListener listener,
                                    @Nullable OrderedRealmCollection<T> data, boolean autoUpdate) {
        super(data, autoUpdate);

        this.context = context;

        if (data == null)
            this.results = new RealmList<>();
        else
            this.results = data;

        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.onViewHolder(results.get(position), position);
    }

    protected void addItem(@Nullable T data, boolean isNotify) {
        results.add(data);
        if (isNotify)
            notifyItemChanged(data);
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

    @Nullable
    public T getItem(int position) {
        return results != null ? (T)results.get(position) : null;
    }

    public OrderedRealmCollection<T> getItemList() {
        return results;
    }

    @Override
    public int getItemCount() {
        return results != null ? results.size() : 0;
    }

    public OnListItemClickListener getListener() { return mListener; }
}

package xyz.neonkid.simplememoj.base.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRealmRecyclerView;
import xyz.neonkid.simplememoj.base.adapter.listener.OnItemChangeListener;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * RealmRecyclerView Adapter의 기본 코드를 정의해놓은 클래스입니다.
 *
 * 이 클래스는 Realm DB와 연동되는 RecyclerView를 위한 Adapter입니다.
 *
 * 경고: 이 클래스에서는 RealmCollection을 이용합니다.
 * Java의 List를 사용할 경우, 오류가 발생함을 유의하십시오.
 *
 * 반드시 사용할 때, Application에서 Realm을 초기화한 후 사용하십시오.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseRealmRecyclerAdapter<T extends RealmObject, VH extends BaseRealmRecyclerView>
        extends RealmRecyclerViewAdapter<T, VH> implements OrderedRealmCollectionChangeListener {
    private Context context;
    private OrderedRealmCollection<T> results;
    private OnListItemClickListener mClickListener;
    private OnItemChangeListener mChangeListener;

    public BaseRealmRecyclerAdapter(Context context,
                                    @Nullable OnListItemClickListener clickListener,
                                    @Nullable OnItemChangeListener changeListener,
                                    @Nullable OrderedRealmCollection<T> data, boolean autoUpdate) {
        super(data, autoUpdate);

        this.context = context;

        if (data == null)
            this.results = new RealmList<>();
        else
            this.results = data;

        if (changeListener != null)
            this.mChangeListener = changeListener;

        if (clickListener != null)
            this.mClickListener = clickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        addListener(getItemList());
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        removeListener(getItemList());
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

    public OnListItemClickListener getClickListener() { return mClickListener; }

    @Override
    public void onChange(@NonNull Object o, OrderedCollectionChangeSet changeSet) {
        if (changeSet.getState() == OrderedCollectionChangeSet.State.INITIAL) {
            notifyDataSetChanged();
            return;
        }

        // For deletions, the adapter has to be notified in reverse order.
        OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
        for (int i = deletions.length - 1; i >= 0; i--) {
            OrderedCollectionChangeSet.Range range = deletions[i];
            notifyItemRangeRemoved(range.startIndex + dataOffset(), range.length);
        }

        OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
        for (OrderedCollectionChangeSet.Range range : insertions)
            notifyItemRangeInserted(range.startIndex + dataOffset(), range.length);

        OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
        for (OrderedCollectionChangeSet.Range range : modifications)
            notifyItemRangeChanged(range.startIndex + dataOffset(), range.length);

        if (mChangeListener != null)
            this.mChangeListener.onChange();
    }

    private void addListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults<T> results = (RealmResults<T>) data;

            // noinspection unchecked
            results.addChangeListener(this);
        } else if (data instanceof RealmList) {
            RealmList<T> list = (RealmList<T>) data;

            // noinspection unchecked
            list.addChangeListener(this);
        } else
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
    }

    private void removeListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults<T> results = (RealmResults<T>) data;

            // noinspection unchecked
            results.removeChangeListener(this);
        } else if (data instanceof RealmList) {
            RealmList<T> list = (RealmList<T>) data;

            // noinspection unchecked
            list.removeChangeListener(this);
        } else
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
    }
}

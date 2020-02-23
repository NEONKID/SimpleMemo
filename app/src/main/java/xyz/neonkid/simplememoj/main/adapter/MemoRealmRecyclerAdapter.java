package xyz.neonkid.simplememoj.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import xyz.neonkid.simplememoj.base.adapter.BaseRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.view.MemoRealmRecyclerView;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.component.listener.OnMemoChangeListener;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoRealmRecyclerAdapter extends BaseRealmRecyclerAdapter<Memo, MemoRealmRecyclerView> implements
        OrderedRealmCollectionChangeListener {
    private OnMemoChangeListener changeListener;

    public MemoRealmRecyclerAdapter(Context context, OnListItemClickListener listener,
                                    OnMemoChangeListener changeListener,
                                    @Nullable OrderedRealmCollection<Memo> data) {
        super(context, listener, data, false);
        this.changeListener = changeListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        addListener(getItemList());
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        removeListener(getItemList());
    }

    @NonNull
    @Override
    public MemoRealmRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoRealmRecyclerView(parent, this);
    }

    @Override
    public void onChange(Object o, OrderedCollectionChangeSet changeSet) {
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

        this.changeListener.onChange();
    }

    private void addListener(@NonNull OrderedRealmCollection<Memo> data) {
        if (data instanceof RealmResults) {
            RealmResults<Memo> results = (RealmResults<Memo>) data;

            // noinspection unchecked
            results.addChangeListener(this);
        } else if (data instanceof RealmList) {
            RealmList<Memo> list = (RealmList<Memo>) data;

            // noinspection unchecked
            list.addChangeListener(this);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private void removeListener(@NonNull OrderedRealmCollection<Memo> data) {
        if (data instanceof RealmResults) {
            RealmResults<Memo> results = (RealmResults<Memo>) data;

            // noinspection unchecked
            results.removeChangeListener(this);
        } else if (data instanceof RealmList) {
            RealmList<Memo> list = (RealmList<Memo>) data;

            // noinspection unchecked
            list.removeChangeListener(this);
        } else
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
    }
}

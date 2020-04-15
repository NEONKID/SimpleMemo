package xyz.neonkid.simplememoj.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.realm.OrderedRealmCollection;
import xyz.neonkid.simplememoj.base.adapter.BaseRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.base.adapter.listener.OnItemChangeListener;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.view.MemoRealmRecyclerView;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoRealmRecyclerAdapter extends BaseRealmRecyclerAdapter<Memo, MemoRealmRecyclerView> {

    public MemoRealmRecyclerAdapter(Context context, OnListItemClickListener clickListener,
                                    OnItemChangeListener changeListener,
                                    @Nullable OrderedRealmCollection<Memo> data) {
        super(context, clickListener, changeListener, data, false);
    }

    @NonNull
    @Override
    public MemoRealmRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoRealmRecyclerView(parent, this);
    }
}

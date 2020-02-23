package xyz.neonkid.simplememoj.base.adapter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;
import xyz.neonkid.simplememoj.base.adapter.BaseRealmRecyclerAdapter;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * RealmRecyclerView의 기본 코드를 정의해 놓은 클래스입니다.
 * 이 클래스에서는 Realm DB를 사용하는 Adapter와 연동할 때 사용합니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseRealmRecyclerView<AD extends BaseRealmRecyclerAdapter, T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private AD adapter;

    public BaseRealmRecyclerView(AD adapter, @NonNull View itemView) {
        super(itemView);
        this.adapter = adapter;

        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public BaseRealmRecyclerView(@LayoutRes int layoutRes, ViewGroup parent, AD adapter) {
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
        int pos = getLayoutPosition();
        adapter.getListener().onItemClick(v, pos);
    }
}

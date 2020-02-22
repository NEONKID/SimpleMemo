package xyz.neonkid.simplememoj.main.adapter.view;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.simplememoj.main.adapter.MemoColorRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoColor;

/**
 * Created by Neon K.I.D on 2/20/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoColorRecyclerView extends BaseRecyclerView<MemoColorRecyclerAdapter, MemoColor> {
    @BindView(R.id.memo_color)
    Button memoColor;

    public MemoColorRecyclerView(ViewGroup parent, MemoColorRecyclerAdapter adapter) {
        super(R.layout.content_memo_color, parent, adapter);
    }

    @Override
    public void onViewHolder(@Nullable MemoColor item, int position) {
        if (item != null)
            memoColor.setBackgroundColor(Color.parseColor(item.color));
    }
}

package xyz.neonkid.simplememoj.main.view.MemoEdit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindArray;
import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseBottomSheetFragment;
import xyz.neonkid.simplememoj.main.adapter.MemoColorRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.MemoColor;
import xyz.neonkid.simplememoj.main.component.listener.OnListItemClickListener;

/**
 * Created by Neon K.I.D on 2/16/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoEditRightBSFragment extends BaseBottomSheetFragment implements OnListItemClickListener {
    // INSTANCE
    private static MemoEditRightBSFragment fragment = new MemoEditRightBSFragment();

    // Listener
    private OnDataSetListener onDataSetListener;

    public static MemoEditRightBSFragment getInstance() {
        return fragment;
    }

    private MemoColorRecyclerAdapter adapter;

    @BindView(R.id.color_chooser)
    RecyclerView colorChooser;

    @BindArray(R.array.memocolor)
    String[] memoColor;

    @Override
    public int getLayoutResource() {
        return R.layout.bottom_memo_edit_right;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MemoEditLeftBSFragment.OnDataSetListener)
            onDataSetListener = (MemoEditRightBSFragment.OnDataSetListener) context;
        else
            throw new RuntimeException(context.toString() + "must implement OnDataSetListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDataSetListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new MemoColorRecyclerAdapter(getContext(), this);
        colorChooser.setAdapter(adapter);

        for (String color : memoColor)
            adapter.addMemoColor(new MemoColor(color));
    }

    @Override
    public void onItemClick(View v, int pos) {
        onDataSetListener.onColorSet(adapter.getItemList().get(pos).color);
        fragment.dismiss();
    }

    public interface OnDataSetListener {
        void onColorSet(String color);
    }
}

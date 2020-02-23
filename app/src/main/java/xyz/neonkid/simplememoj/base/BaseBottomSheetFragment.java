package xyz.neonkid.simplememoj.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import butterknife.ButterKnife;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * 이 클래스는 메모 에디터에서 하단 메뉴를 띄우는 데 사용하는 Fragment의 공통 코드입니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected final void setToastMessage(String message) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show());
    }

    @LayoutRes
    public abstract int getLayoutResource();
}

package xyz.neonkid.simplememoj.base.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import butterknife.ButterKnife;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BaseDialogFragment extends DialogFragment {
    private AlertDialog.Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(getLayoutResource(), null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        onCreate();

        return builder.create();
    }

    protected final AlertDialog.Builder getBuilder() {
        return builder;
    }

    protected abstract int getLayoutResource();

    protected abstract void onCreate();
}

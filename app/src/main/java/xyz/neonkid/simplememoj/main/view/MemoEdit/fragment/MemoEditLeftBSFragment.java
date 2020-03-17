package xyz.neonkid.simplememoj.main.view.MemoEdit.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.OnClick;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseBottomSheetFragment;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.component.dialog.URLDialogFragment;
import xyz.neonkid.simplememoj.main.util.FileUtils;
import xyz.neonkid.simplememoj.main.util.MemoCode;

/**
 * Created by Neon K.I.D 2/15/20
 *
 * Blog: https://blog.neonkid.xyz
 * Github: https://github.com/NEONKID
 */
public class MemoEditLeftBSFragment extends BaseBottomSheetFragment {
    // INSTANCE
    private static MemoEditLeftBSFragment fragment = new MemoEditLeftBSFragment();

    // URL Listener
    private OnDataSetListener onDataSetListener;

    // Cur Memo ID
    private int curId;

    public static MemoEditLeftBSFragment getInstance() {
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.bottom_memo_edit_left;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle extra = this.getArguments();
        if (extra != null)
            curId = extra.getInt(Memo.FIELD_ID);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDataSetListener)
            onDataSetListener = (OnDataSetListener) context;
        else
            throw new RuntimeException(context.toString() + "must implement OnDataSetListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDataSetListener = null;
    }

    private void onTakePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            Uri mImageUri;

            try {
                File imageFile = FileUtils.createTempImageFile(curId,
                        Objects.requireNonNull(getContext()).getFilesDir());

                mImageUri = FileUtils.getFileProviderPath(getContext(), imageFile);

                onDataSetListener.onFileSet(imageFile);
            } catch (IOException ex) {
                setToastMessage(ex.getMessage());
                return;
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            getActivity().startActivityForResult(intent, MemoCode.MemoRequest.CAPTURE_IMAGE);
        }
    }

    private void onGetContent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Objects.requireNonNull(getActivity())
                .startActivityForResult(intent, MemoCode.MemoRequest.PICK_IMAGE);
    }

    private void onGetWebURL() {
        if (getFragmentManager() != null)
            URLDialogFragment.newInstance(URL -> {
                onDataSetListener.onURLSet(URL);
                fragment.dismiss();
            }).show(getFragmentManager(), "URL_DIALOG");
    }

    @OnClick(R.id.menu_camera)
    public void menuCameraClick() {
        onTakePicture();
        fragment.dismiss();
    }

    @OnClick(R.id.menu_storage)
    public void menuStorageClick() {
        onGetContent();
        fragment.dismiss();
    }

    @OnClick(R.id.menu_web)
    public void menuWebClick() {
        onGetWebURL();
    }

    public interface OnDataSetListener {
        void onFileSet(File file);
        void onURLSet(String Url);
    }
}

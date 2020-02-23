package xyz.neonkid.simplememoj.main.component.dialog;

import android.widget.EditText;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.dialog.BaseDialogFragment;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * 이미지 URL 입력 Dialog Fragment
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class URLDialogFragment extends BaseDialogFragment {
    private URLInputListener listener;

    @BindView(R.id.url_input)
    EditText UrlInput;

    public static URLDialogFragment newInstance(URLInputListener listener) {
        URLDialogFragment fragment = new URLDialogFragment();
        fragment.listener = listener;
        return fragment;
    }

    public interface URLInputListener {
        void onURLChanged(String URL);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_url;
    }

    @Override
    protected void onCreate() {
        getBuilder().setPositiveButton(getString(R.string.alert_ok), (dialog, which) -> {
                    listener.onURLChanged(UrlInput.getText().toString());
                    dialog.dismiss();
        }).setNegativeButton(getString(R.string.alert_cancel), null);
    }
}

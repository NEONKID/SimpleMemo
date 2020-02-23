package xyz.neonkid.simplememoj.main.component.dialog;

import android.widget.TextView;

import butterknife.BindView;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.dialog.BaseDialogFragment;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * SimpleMemoJ의 기본 Alert Dialog
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class AlertDialogFragment extends BaseDialogFragment {
    private EventListener listener;
    private String msg;

    @BindView(R.id.alert_msg)
    TextView AlertMsg;

    public static AlertDialogFragment newInstance(EventListener listener, String msg) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.listener = listener;
        fragment.msg = msg;
        return fragment;
    }

    public interface EventListener {
        void onOKClick();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_alert;
    }

    @Override
    protected void onCreate() {
        AlertMsg.setText(msg);
        getBuilder().setPositiveButton(getString(R.string.alert_ok), (dialog, which) -> listener.onOKClick())
                .setNegativeButton(getString(R.string.alert_cancel), null);
    }
}

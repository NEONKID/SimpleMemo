package xyz.neonkid.simplememoj.main.view.Error;

import android.content.Intent;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import xyz.neonkid.simplememoj.BuildConfig;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.main.presenter.Error.ErrorPresenter;
import xyz.neonkid.simplememoj.main.presenter.Error.view.ErrorPresenterView;

/**
 * Created by Neon K.I.D on 3/21/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class ErrorActivity extends BaseActivity implements ErrorPresenterView {
    public static final String EXTRA_INTENT = "EXTRA_INTENT";
    public static final String EXTRA_ERROR_INTENT = "EXTRA_ERROR_INTENT";

    private Intent lastActivityIntent;
    private String errorText;
    private ErrorPresenter presenter;

    @BindView(R.id.tv_error_log)
    TextView errLog;

    @Override
    protected void onCreate() {
        presenter = new ErrorPresenter(this ,this);

        Intent intent = getIntent();

        lastActivityIntent = intent.getParcelableExtra(EXTRA_INTENT);
        errorText = intent.getStringExtra(EXTRA_ERROR_INTENT);

        if (BuildConfig.DEBUG)
            errLog.setText(errorText);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_error;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }

    @OnClick(R.id.btn_reload)
    public void reloadClick() {
        startActivity(lastActivityIntent);
        finish();
    }
}

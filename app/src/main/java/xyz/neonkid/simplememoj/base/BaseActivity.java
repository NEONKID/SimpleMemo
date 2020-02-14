package xyz.neonkid.simplememoj.base;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.neonkid.simplememoj.R;

public abstract class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);

        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.setStatusBarColor(this.getColor(R.color.colorPrimary));

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));

        setToolbar();
        onCreate();
    }

    private void setToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getToolbarTitle());
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    protected abstract void onCreate();

    @LayoutRes
    protected abstract int getLayoutResource();

    @StringRes
    protected abstract int getToolbarTitle();
}

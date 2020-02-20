package xyz.neonkid.simplememoj.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
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

        setStatusBarColor(getString(R.color.colorPrimary));
        setToolbar();

        onCreate();
    }

    protected void setStatusBarColor(String color) {
        Window window = this.getWindow();
        window.setStatusBarColor(Color.parseColor(color));
    }

    private void setToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getToolbarTitle());
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    protected void setToolbarColor(String color) {
        if (toolbar != null)
            toolbar.setBackgroundColor(Color.parseColor(color));
    }

    protected final void setToastMessage(String msg) { Toast.makeText(this, msg, Toast.LENGTH_LONG).show(); }

    protected abstract void onCreate();

    @LayoutRes
    protected abstract int getLayoutResource();

    @StringRes
    protected abstract int getToolbarTitle();
}

package xyz.neonkid.simplememoj.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.neonkid.simplememoj.R;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * 각 Activity에 사용할 공통 코드를 정의한 BaseActivity입니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
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

    /**
     * 안드로이드의 상단바 색상을 변경합니다.
     * 디자인을 위한 함수입니다.
     *
     * @param color 색상 값 (16)
     */
    protected void setStatusBarColor(String color) {
        Window window = this.getWindow();
        window.setStatusBarColor(Color.parseColor(color));
    }

    /**
     * 뷰에 Toolbar 를 설정하는 함수입니다.
     * Layout Resource 에서 Toolbar가 있다면, 렌더링 되고,
     * 그렇지 않으면 렌더링되지 않습니다.
     *
     * 각 뷰별로 Toolbar를 사용자 정의하기 위해 구현되었습니다.
     */
    private void setToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getToolbarTitle());
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    /**
     * Action bar의 색상을 변경하는 함수입니다.
     * 메모의 색상별로 화면의 디자인 조화를 이루기 위한 함수입니다.
     *
     * @param color 색상 값 (16)
     */
    protected void setToolbarColor(String color) {
        if (toolbar != null)
            toolbar.setBackgroundColor(Color.parseColor(color));
    }

    protected final void setToastMessage(String msg) { Toast.makeText(this, msg, Toast.LENGTH_LONG).show(); }

    /**
     * 기본 Activity 의 onCreate 함수에 이어서 적용되는 추상 함수입니다.
     * 이 함수에서 각 Activity 별로 코드를 별도로 작성하여 로드합니다.
     */
    protected abstract void onCreate();

    /**
     * 각 Activity에서 사용할 Layout Resource를 정의하는 추상 함수입니다.
     *
     * @return Layout Resource
     */
    @LayoutRes
    protected abstract int getLayoutResource();

    /**
     * 각 Activity에서 사용할 Toolbar 제목을 정의합니다.
     *
     * 경고: 이 함수에서 0을 사용하지 마십시오.
     * 과거에는 0을 사용하여 empty 처리가 되었지만 지금은 문제가 있는 것으로 보입니다.
     *
     * 만약 제목을 비우고 싶다면 R.string.empty 값을 이용하십시오.
     *
     * @return String Resource
     */
    @StringRes
    protected abstract int getToolbarTitle();
}

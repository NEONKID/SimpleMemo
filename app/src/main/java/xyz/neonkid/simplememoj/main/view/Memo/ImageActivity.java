package xyz.neonkid.simplememoj.main.view.Memo;

import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseImageActivity;
import xyz.neonkid.simplememoj.base.adapter.listener.OnListItemClickListener;
import xyz.neonkid.simplememoj.main.presenter.Memo.ImagePresenter;
import xyz.neonkid.simplememoj.main.presenter.Memo.view.ImagePresenterView;

/**
 * Created by Neon K.I.D on 2/20/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class ImageActivity extends BaseImageActivity implements ImagePresenterView, OnListItemClickListener {
    private ImagePresenter presenter;

    @Override
    protected void onCreate() {
        presenter = new ImagePresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }
}

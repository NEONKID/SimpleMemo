package xyz.neonkid.simplememoj.base.presenter;

import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.base.presenter.view.BasePresenterView;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public abstract class BasePresenter<V extends BasePresenterView, C extends BaseActivity> {
    private V view;
    private C context;

    public BasePresenter(V view) {
        this.view = view;
    }

    public BasePresenter(V view, C context) {
        this.view = view;
        this.context = context;
    }

    protected final V getView() { return view; }

    protected final C getContext() { return context; }
}

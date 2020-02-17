package xyz.neonkid.simplememoj.base.presenter;

import io.realm.Realm;
import xyz.neonkid.simplememoj.base.BaseActivity;
import xyz.neonkid.simplememoj.base.presenter.view.BasePresenterView;

public abstract class BasePresenter<V extends BasePresenterView, C extends BaseActivity> {
    private V view;
    private C context;
    private static Realm realm;

    public BasePresenter(V view) {
        this.view = view;
    }

    public BasePresenter(V view, C context) {
        this.view = view;
        this.context = context;

        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    protected final V getView() { return view; }

    protected final C getContext() { return context; }

    protected final Realm getDB() { return realm; }
}

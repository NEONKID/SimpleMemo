package xyz.neonkid.simplememoj.main.presenter.Error;

import xyz.neonkid.simplememoj.base.presenter.BasePresenter;
import xyz.neonkid.simplememoj.main.presenter.Error.view.ErrorPresenterView;
import xyz.neonkid.simplememoj.main.view.Error.ErrorActivity;

/**
 * Created by Neon K.I.D on 3/23/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class ErrorPresenter extends BasePresenter<ErrorPresenterView, ErrorActivity> {
    public ErrorPresenter(ErrorPresenterView view, ErrorActivity context) {
        super(view, context);
    }
}

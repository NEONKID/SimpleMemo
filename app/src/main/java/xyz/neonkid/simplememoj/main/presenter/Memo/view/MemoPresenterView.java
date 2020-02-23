package xyz.neonkid.simplememoj.main.presenter.Memo.view;

import xyz.neonkid.simplememoj.base.presenter.view.BasePresenterView;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface MemoPresenterView extends BasePresenterView {
    void renderMemo(Memo memo);
    void setToast(String msg);
}

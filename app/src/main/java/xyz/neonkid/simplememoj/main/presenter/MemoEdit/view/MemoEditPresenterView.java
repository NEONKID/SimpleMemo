package xyz.neonkid.simplememoj.main.presenter.MemoEdit.view;

import xyz.neonkid.simplememoj.base.presenter.view.BasePresenterView;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;

/**
 * Created by Neon K.I.D on 2/16/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface MemoEditPresenterView extends BasePresenterView {
    void renderMemo(Memo memo);
    void setToast(String message);
}

package xyz.neonkid.simplememoj.main.presenter;

import io.realm.RealmResults;
import xyz.neonkid.simplememoj.base.presenter.BasePresenter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.presenter.view.MainPresenterView;
import xyz.neonkid.simplememoj.main.util.RealmHelper;
import xyz.neonkid.simplememoj.main.view.MainActivity;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MainPresenter extends BasePresenter<MainPresenterView, MainActivity> {
    public MainPresenter(MainPresenterView view, MainActivity context) {
        super(view, context);
    }

    /**
     * Realm DB에서 모든 메모들을 가져옵니다.
     *
     * @return Realm DB 내 모든 메모들
     */
    public RealmResults<Memo> loadMemos() {
        return RealmHelper.getInstance().getMemos();
    }
}

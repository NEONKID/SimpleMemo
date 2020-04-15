package xyz.neonkid.simplememoj.main.presenter.Memo;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.presenter.BasePresenter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.presenter.Memo.view.MemoPresenterView;
import xyz.neonkid.simplememoj.main.util.FileUtils;
import xyz.neonkid.simplememoj.main.util.RealmHelper;
import xyz.neonkid.simplememoj.main.view.Memo.MemoActivity;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoPresenter extends BasePresenter<MemoPresenterView, MemoActivity> {
    public MemoPresenter(MemoPresenterView view, MemoActivity context) {
        super(view, context);
    }

    public Memo getMemoWithId(int id) {
        return RealmHelper.getInstance().getMemoWithId(id).first();
    }

    public void deleteMemo(int memoId) {
        Realm.Transaction.OnSuccess success = () -> getView().setToast(getContext().getString(R.string.memo_deleted_msg));
        Realm.Transaction.OnError error = ex -> getView().setToast(ex.getMessage());

        RealmHelper.getInstance().deleteMemoWithId(memoId, success, error);

        File memoRoot = new File(getContext().getFilesDir(), memoId + "/images");
        FileUtils.deleteDirectory(memoRoot);
    }

    public String convertModTimeString(Date modified) {
        DateFormat df = DateFormat.getDateInstance();
        df.setTimeZone(TimeZone.getDefault());

        return getContext().getString(R.string.memo_modified_prefix) + df.format(modified);
    }
}

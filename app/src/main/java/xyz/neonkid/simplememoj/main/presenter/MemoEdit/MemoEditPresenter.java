package xyz.neonkid.simplememoj.main.presenter.MemoEdit;

import android.net.Uri;

import java.io.File;
import java.io.IOException;

import io.realm.RealmResults;
import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.presenter.BasePresenter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.view.MemoEditPresenterView;
import xyz.neonkid.simplememoj.main.util.FileUtils;
import xyz.neonkid.simplememoj.main.util.RealmHelper;
import xyz.neonkid.simplememoj.main.view.MemoEdit.MemoEditActivity;

/**
 * Created by Neon K.I.D on 2/16/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class MemoEditPresenter extends BasePresenter<MemoEditPresenterView, MemoEditActivity> {
    public MemoEditPresenter(MemoEditPresenterView view, MemoEditActivity context) {
        super(view, context);
    }

    public String getContentImageCopyPath(int memoId, Uri origin) throws IOException {
        File imageFile = FileUtils.createTempImageFile(memoId, getContext().getFilesDir());
        FileUtils.copyFile(getContext(), origin, imageFile);

        return FileUtils.getFileProviderPath(getContext(), imageFile).toString();
    }

    public RealmResults<Memo> loadMemos() {
        return RealmHelper.getInstance().getMemos();
    }

    public Memo getMemoWithId(int id) {
        return RealmHelper.getInstance().getMemoWithId(id).first();
    }

    public void insertMemo(int id, String title, String content, String color, MemoImage img, boolean notify) {
        RealmHelper.getInstance().insertMemo(id, title, content, color, img);
        if (notify)
            getView().setToast(getContext().getString(R.string.memo_saved_msg));
    }

    public void deleteMemo(int memoId) {
        RealmHelper.getInstance().deleteMemoWithId(memoId);

        File memoRoot = new File(getContext().getFilesDir(), memoId + "");
        FileUtils.deleteDirectory(memoRoot);

        getView().setToast(getContext().getString(R.string.memo_deleted_msg));
    }

    public void deleteMemoImage(int id) {
        RealmHelper.getInstance().deleteMemoImageWithMemoImageId(id);
        getView().setToast(getContext().getString(R.string.memo_image_deleted_msg));
    }
}

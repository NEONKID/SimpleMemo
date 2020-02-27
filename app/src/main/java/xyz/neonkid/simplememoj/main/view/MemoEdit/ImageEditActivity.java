package xyz.neonkid.simplememoj.main.view.MemoEdit;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.base.BaseImageActivity;
import xyz.neonkid.simplememoj.main.component.dialog.AlertDialogFragment;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.ImageEditPresenter;
import xyz.neonkid.simplememoj.main.presenter.MemoEdit.view.ImageEditPresenterView;
import xyz.neonkid.simplememoj.main.util.MemoCode;

/**
 * Created by Neon K.I.D on 2/17/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class ImageEditActivity extends BaseImageActivity implements ImageEditPresenterView {
    private ImageEditPresenter presenter;

    @Override
    protected void onCreate() {
        presenter = new ImageEditPresenter(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.img_menu, menu);
        return true;
    }

    /**
     * 삭제 버튼을 클릭하게 되면, 현재 화면에 보여진 이미지의 ID를
     * MemoEditActivity에 넘겨주고, 삭제하게 됩니다.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.img_delete) {
            AlertDialogFragment.newInstance(() -> {
                intent.putExtra("imageId", curImgId);
                setResult(MemoCode.MemoResult.DELETED_IMAGE, intent);
                finish();
            }, getString(R.string.alert_delete)).show(getSupportFragmentManager(), "MEMO_IMG_DEL_ALERT");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_edit;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.empty;
    }
}

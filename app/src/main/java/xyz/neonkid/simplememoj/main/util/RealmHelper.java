package xyz.neonkid.simplememoj.main.util;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import xyz.neonkid.simplememoj.main.SimpleMemoJMigration;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.adapter.model.MemoImage;

/**
 * Created by Neon K.I.D on 2/18/20
 *
 * Realm DB 데이터 관련 클래스입니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class RealmHelper {
    private static RealmHelper helper = new RealmHelper();

    public static RealmHelper getInstance() {
        return helper;
    }

    private Realm realm;

    /**
     * 앱에서 사용할 DB 이름과 스키마 버전을 정의합니다.
     * 스키마 버전은 차후 업데이트 시 마이그레이션 버전 관리로 사용하게 됩니다.
     *
     * 마이그레이션은 SimpleMemoJMigration 클래스에서 구현할 수 있습니다.
     * 마이그레이션 코드가 완성된 뒤에는 반드시 schemaVersion을 고쳐야 합니다.
     *
     * @see SimpleMemoJMigration
     */
    private RealmHelper() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("simplememoj-memo.realm")
                .schemaVersion(1)
                .migration(new SimpleMemoJMigration())
                .build();
        realm = Realm.getInstance(config);
    }

    public Realm getRealmInstance() {
        return realm;
    }

    /**
     * 메모를 저장하는 함수입니다.
     *
     * 기존된 작성 메모는 ID를 통해 수정이 이루어집니다.
     * 새로 작성된 메모는 id로 0을 받게 되며 새로운 ID 값을 부여 받게 됩니다.
     *
     * @param id 기존 메모 ID (없을 경우, 0)
     * @param title 메모의 제목
     * @param content 메모의 내용
     * @param color 메모 색깔
     * @param mi 메모에 들어갈 이미지 (Nullable)
     */
    public void insertMemo(int id, String title, String content, String color, @Nullable MemoImage mi) {
        // 기존 메모인 경우..
        if (editMemoWithId(id, title, content, color, mi))
            return;

        // 새로운 메모인 경우
        Memo memo = new Memo();
        memo.setId(getNewMemoId());
        memo.setTitle(title);
        memo.setContent(content);
        memo.setColor(color);
        memo.setCreated(Calendar.getInstance(TimeZone.getDefault()).getTime());
        memo.setModified(Calendar.getInstance(TimeZone.getDefault()).getTime());

        realm.executeTransaction(realm -> {
            if (mi != null)
                memo.setTransactionImg(mi);

            realm.copyToRealm(memo);
        });
    }

    /**
     * 메모에 이미지를 DB에 저장하는 함수입니다.
     * 이미지에 ID가 부여 되지 않은 경우, 새 ID가 부여됩니다.
     *
     * 단, Realm DB에서 기존에 부여된 ID가 있다면 Exception을 발생합니다.
     *
     * @param memo 대상 메모
     * @param mi 추가할 이미지
     */
    public void insertMemoImage(Memo memo, MemoImage mi) {
        if (mi.getId() == 0)
            mi.setId(getNewMemoImageId());

        final MemoImage managedMi = realm.copyToRealmOrUpdate(mi);
        memo.getImgs().add(managedMi);
    }

    /**
     * DB에 저장된 모든 메모를 가져오는 함수입니다.
     * 작성 순서별로 배치하기 위해 Primary Key룰 부여받은 순으로 정렬합니다.
     *
     * @return 저장된 메모들
     */
    public RealmResults<Memo> getMemos() {
        return realm.where(Memo.class).findAllAsync().sort(Memo.FIELD_ID, Sort.ASCENDING);
    }

    /**
     * 한 메모에 들어있는 모든 이미지를 가져오는 함수입니다.
     *
     * @param memoId 대상 메모 ID
     * @return memoId에 해당하는 이미지들
     */
    public RealmResults<MemoImage> getMemoImages(int memoId) {
        return realm.where(MemoImage.class).equalTo("memo.id", memoId).findAllAsync()
                .sort(MemoImage.FIELD_ID, Sort.ASCENDING);
    }

    /**
     * 메모의 ID 값에 해당하는 메모를 가져오는 함수입니다.
     *
     * @param id 대상 메모 ID
     * @return id에 해당하는 모든 메모
     */
    public RealmResults<Memo> getMemoWithId(int id) {
        return realm.where(Memo.class).equalTo(Memo.FIELD_ID, id).findAllAsync()
                .sort(Memo.FIELD_ID, Sort.ASCENDING);
    }

    /**
     * 메모 이미지의 ID 값에 해당하는 이미지를 가져오는 함수입니다.
     *
     * @param id 대상 메모 이미지 ID
     * @return id에 해당하는 모든 메모 이미지
     */
    public RealmResults<MemoImage> getMemoImageWithId(int id) {
        return realm.where(MemoImage.class).equalTo(MemoImage.FIELD_ID, id).findAllAsync()
                .sort(MemoImage.FIELD_ID, Sort.ASCENDING);
    }

    /**
     * 기존의 메모를 수정하기 위한 함수입니다.
     * 만약 기존의 존재하던 메모가 없다면 false를 반환합니다.
     *
     * @param id 대상 메모 ID
     * @param title 수정할 제목
     * @param content 수정할 내용
     * @param color 수정할 색깔
     * @param mi 메모 이미지
     * @return 저장 결과
     */
    private boolean editMemoWithId(int id, String title, String content, String color,
                                   @Nullable MemoImage mi) {
        RealmResults<Memo> res = getMemoWithId(id);

        if (res.isLoaded()) {
            if (res.isEmpty())
                return false;

            Memo memo = res.get(0);

            if (memo != null) {
                realm.executeTransaction(realm -> {
                    memo.setTitle(title);
                    memo.setContent(content);
                    memo.setColor(color);
                    memo.setModified(Calendar.getInstance(TimeZone.getDefault()).getTime());

                    if (mi != null)
                        memo.setTransactionImg(mi);
                });
            }
        }

        return true;
    }

    /**
     * 대상 메모를 제거하는 함수입니다.
     * 메모 이미지가 존재한다면, 이미지 제거 후, 삭제를 진행합니다.
     *
     * @param id 대상 메모 ID
     */
    public void deleteMemoWithId(int id) {
        RealmResults<Memo> res = getMemoWithId(id);
        deleteMemoImageWithMemoId(id);

        if (res.isLoaded()) {
            if (res.isEmpty())
                return;
            realm.executeTransaction(realm -> res.deleteAllFromRealm());
        }
    }

    /**
     * 대상 메모 이미지를 제거하는 함수입니다.
     * 실제 이미지와 동시에 제거하게 됩니다.
     *
     * @param imageId 대상 메모 이미지 ID
     */
    public void deleteMemoImageWithMemoImageId(int imageId) {
        RealmResults<MemoImage> res = getMemoImageWithId(imageId);

        if (res.isLoaded()) {
            if (res.isEmpty())
                return;

            for (MemoImage img : res)
                FileUtils.deleteFile(new File(img.getUri()));

            realm.executeTransaction(realm -> res.deleteAllFromRealm());
        }
    }

    private void deleteMemoImageWithMemoId(int memoId) {
        RealmResults<MemoImage> res = getMemoImages(memoId);

        if (res.isLoaded()) {
            if (res.isEmpty())
                return;
            realm.executeTransaction(realm -> res.deleteAllFromRealm());
        }
    }

    /**
     * 새로운 메모 ID와 메모 이미지 ID는 아래의 3개 함수를 통해 이루어집니다.
     * 모든 메모 ID는 이 클래스의 private 함수를 통해 받게 됩니다.
     *
     * @return 새로운 메모, 메모 이미지 ID
     */
    private int getNewMemoId() {
        return getAutoIncrementId(new Memo());
    }

    private int getNewMemoImageId() {
        return getAutoIncrementId(new MemoImage());
    }

    private int getAutoIncrementId(Object obj) {
        Number curId = null;

        if (obj instanceof Memo)
            curId = realm.where(Memo.class).max(Memo.FIELD_ID);
        else if (obj instanceof MemoImage)
            curId = realm.where(MemoImage.class).max(MemoImage.FIELD_ID);

        if (curId == null)
            return 1;
        else
            return curId.intValue() + 1;
    }
}

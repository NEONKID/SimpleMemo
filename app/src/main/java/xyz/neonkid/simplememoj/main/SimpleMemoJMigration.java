package xyz.neonkid.simplememoj.main;

import androidx.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Neon K.I.D on 2/22/20
 *
 * Realm DB 마이그레이션 클래스입니다.
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class SimpleMemoJMigration implements RealmMigration {

    /**
     * 아래의 콜백 함수를 통해 마이그레이션이 진행됩니다.
     * oldVersion 을 이용해서 현재 앱에서 사용하고 있는 스키마의 버전을 가져오게 됩니다.
     *
     * 버전에 따라 마이그레이션을 주어줄 수 있습니다.
     * 가능하면, 버전별로 마이그레이션을 줄 수 있도록 구현하는 것을 권장합니다.
     *
     * @param realm: Realm instance
     * @param oldVersion: current schema version
     * @param newVersion: migrate schema version
     */
    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        RealmObjectSchema memoSchema = schema.get("Memo");
        RealmObjectSchema miSchema = schema.get("MemoImage");
    }
}

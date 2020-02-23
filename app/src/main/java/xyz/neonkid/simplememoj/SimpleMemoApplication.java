package xyz.neonkid.simplememoj;

import android.app.Application;

import io.realm.Realm;
import xyz.neonkid.simplememoj.main.util.RealmHelper;

public class SimpleMemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Realm realm = RealmHelper.getInstance().getRealmInstance();
        if (!realm.isClosed())
            realm.close();
    }
}

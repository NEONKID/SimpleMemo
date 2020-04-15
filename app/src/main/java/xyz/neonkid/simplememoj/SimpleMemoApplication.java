package xyz.neonkid.simplememoj;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import xyz.neonkid.simplememoj.main.util.RealmHelper;
import xyz.neonkid.simplememoj.main.util.AppExceptionHandler;

public class SimpleMemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        setCrashHandler();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Realm realm = RealmHelper.getInstance().getRealmInstance();
        if (!realm.isClosed())
            realm.close();
    }

    private void setCrashHandler() {
        Thread.UncaughtExceptionHandler defaultExceptionHandler
                = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {});

        Fabric.with(this, new Crashlytics());

        Thread.UncaughtExceptionHandler fabricExceptionHandler
                = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler(
                this,
                defaultExceptionHandler,
                fabricExceptionHandler
        ));
    }
}

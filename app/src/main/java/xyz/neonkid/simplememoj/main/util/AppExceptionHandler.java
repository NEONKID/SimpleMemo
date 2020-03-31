package xyz.neonkid.simplememoj.main.util;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

import xyz.neonkid.simplememoj.main.view.Error.ErrorActivity;
import xyz.neonkid.simplememoj.main.view.MainActivity;

/**
 * Created by Neon K.I.D on 3/20/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class AppExceptionHandler implements Thread.UncaughtExceptionHandler, Application.ActivityLifecycleCallbacks {
    private Application app;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private Thread.UncaughtExceptionHandler fabricExceptionHandler;

    // Save Last Activity
    private Activity lastActivity;
    private int activityCnt;

    public AppExceptionHandler(
            Application app,
            Thread.UncaughtExceptionHandler defaultExceptionHandler,
            Thread.UncaughtExceptionHandler fabricExceptionHandler) {
        this.app = app;
        this.defaultExceptionHandler = defaultExceptionHandler;
        this.fabricExceptionHandler = fabricExceptionHandler;

        this.app.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (isSkipActivity(activity))
            return;

        activityCnt++;
        lastActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (isSkipActivity(activity))
            return;

        lastActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (isSkipActivity(activity))
            return;

        activityCnt--;
        if (activityCnt < 0)
            lastActivity = null;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        fabricExceptionHandler.uncaughtException(t, e);

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        startErrorActivity(lastActivity, sw.toString());

        defaultExceptionHandler.uncaughtException(t, e);

        Process.killProcess(Process.myPid());
        System.exit(-1);
    }

    private void startErrorActivity(Activity activity, String errText) {
        Intent errIntent = new Intent(activity, ErrorActivity.class);
        errIntent.putExtra(ErrorActivity.EXTRA_INTENT, activity.getIntent());
        errIntent.putExtra(ErrorActivity.EXTRA_ERROR_INTENT, errText);
        errIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        activity.startActivity(errIntent);
        activity.finish();
    }

    private boolean isSkipActivity(Activity activity) {
        return activity instanceof ErrorActivity;
    }
}

package xyz.neonkid.simplememoj.main.component.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Neon K.I.D on 2/21/20
 *
 * Reveal Animation 을 구현한 코드입니다.
 *
 * 이 소스 코드는 아래의 링크를 참조하였습니다.
 *
 * Ref:
 * https://codesnipps.simolation.com/post/android/create-circular-reveal-animation-when-starting-activitys/
 */
public class RevealAnimation {
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private final View mView;
    private Activity mActivity;

    private int revealX;
    private int revealY;

    public RevealAnimation(View view, Intent intent, Activity activity) {
        mView = view;
        mActivity = activity;

        if (intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            view.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver observer = view.getViewTreeObserver();
            if (observer.isAlive()) {
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else view.setVisibility(View.INVISIBLE);
    }

    private void revealActivity(int x, int y) {
        float finRadius = (float)(Math.max(mView.getWidth(), mView.getHeight()) * 1.1);

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(mView, x, y, 0, finRadius);
        circularReveal.setDuration(300);
        circularReveal.setInterpolator(new AccelerateInterpolator());

        // make the view visible and start the animation
        mView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    public void unRevealActivity() {
        float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                mView, revealX, revealY, finalRadius, 0);

        circularReveal.setDuration(300);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mView.setVisibility(View.INVISIBLE);
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
            }
        });

        circularReveal.start();
    }
}

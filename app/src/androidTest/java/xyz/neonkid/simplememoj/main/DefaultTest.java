package xyz.neonkid.simplememoj.main;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import xyz.neonkid.simplememoj.R;
import xyz.neonkid.simplememoj.main.adapter.MemoRealmRecyclerAdapter;
import xyz.neonkid.simplememoj.main.adapter.model.Memo;
import xyz.neonkid.simplememoj.main.view.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Neon K.I.D on 2/23/20
 *
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class DefaultTest {
    private MainActivity mActivity;
    private RecyclerView memoView;
    private MemoRealmRecyclerAdapter mAdapter;
    private List<Memo> memoList;
    private int itemCount;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @UiThreadTest
    @Before
    public void setUp() {
        mActivity = this.mActivityTestRule.getActivity();
        memoView = mActivity.findViewById(R.id.memo_view);
        mAdapter = (MemoRealmRecyclerAdapter)(memoView.getAdapter());
        itemCount = mAdapter.getItemCount();
        memoList = new ArrayList<>(mAdapter.getItemList());
    }

    /**
     * 메모 카운터, 메모 뷰 등 MainActivity 에서 필요한
     * 뷰 요소들이 제대로 표시되는지 확인하는 테스트 메소드입니다.
     */
    @Test
    public void TestA() {
        ViewInteraction memoView = onView(allOf(withId(R.id.memo_view), isDisplayed()));
        memoView.check(matches(isDisplayed()));

        ViewInteraction mCounter = onView(allOf(withId(R.id.memo_counter), childAtPosition(
                childAtPosition(withId(R.id.memo_bar), 0), 0), isDisplayed()));
        mCounter.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(allOf(withId(R.id.memo_add), isDisplayed()));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * 메인 화면에서 메모가 나오는지 확인하는 테스트 메소드입니다.
     * 메모가 나오는 것과 더불어, 클릭 이벤트가 이루어지는지도 확인합니다.
     */
    @Test
    public void TestB() {
        for (int i = 0; i < itemCount; i++) {
            onView(new RecyclerViewMatcher(R.id.memo_view).atPositionOnView(i, R.id.memo_card))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.memo_view)).check(matches(isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.click()));

            onView(isRoot()).perform(ViewActions.pressBack());
        }
    }

    /**
     * 메인 화면의 우측 하단에 존재하는 + 버튼을 클릭하면,
     * 메모 에디터 화면이 나타나는지 확인하는 테스트 메소드입니다.
     *
     * 메모 에디터에서 아무런 입력 없이 종료될 경우, 메모의 삭제가
     * 수행되는지를 추가로 확인하게 됩니다.
     */
    @Test
    public void TestC() {
        String curCnt = getText(withId(R.id.memo_counter));

        onView(withId(R.id.memo_add)).perform(ViewActions.click());
        onView(allOf(
                withId(R.id.title_edit), withId(R.id.content_edit),
                withId(R.id.memo_edit_bar), withId(R.id.memo_color_button), isDisplayed()));

        onView(isRoot()).perform(ViewActions.pressBack());
        assertEquals(curCnt, getText(withId(R.id.memo_counter)));
    }

    /**
     * 새로이 작성되는 간단한 테스트 메모의 저장 여부를 확인합니다.
     * 저장 여부의 확인은 Toast 메시지로 이루어집니다.
     *
     * @throws Throwable Thread.sleep 의 예외
     */
    @Test
    public void TestD() throws Throwable {
        onView(withId(R.id.memo_add)).perform(ViewActions.click());
        onView(allOf(
                withId(R.id.title_edit), withId(R.id.content_edit),
                withId(R.id.memo_edit_bar), withId(R.id.memo_color_button), isDisplayed()));

        onView(withId(R.id.title_edit)).check(ViewAssertions.matches(withText(isEmptyString())));
        onView(withId(R.id.content_edit)).check(ViewAssertions.matches(withText(isEmptyString())));

        onView(withId(R.id.title_edit)).perform(ViewActions.typeText("Unit Test Sample"));
        onView(withId(R.id.content_edit)).perform(ViewActions.typeText("Unit Test Sample"));

        Thread.sleep(7500);

        onView(withText(R.string.memo_saved_msg)).inRoot(RootMatchers.withDecorView(Matchers.not(
                Matchers.is(getCurrentActivity().getWindow().getDecorView()))
        )).check(matches(isDisplayed()));
    }

    /**
     * 저장된 메모를 확인하는 테스트 메소드입니다.
     */
    @Test
    public void TestE() {
        assertNotEquals(0, itemCount);

        onView(new RecyclerViewMatcher(R.id.memo_view).atPositionOnView(itemCount - 1, R.id.memo_card))
                .check(matches(isDisplayed())).perform(ViewActions.click());

        onView(allOf(withId(R.id.memo_mod), withId(R.id.memo_delete), isDisplayed()));
        onView(withId(R.id.memoView_title)).check(ViewAssertions.matches(withText("Unit Test Sample")));
        onView(withId(R.id.memoView_content)).check(ViewAssertions.matches(withText("Unit Test Sample")));
    }

    /**
     * E 테스트에 이어서, 메모 편집 여부를 확인하는 테스트 메소드입니다.
     *
     * 1.기존에 입력된 테스트 값인지를 확인한다.
     * 2.URL을 입력하여 사진이 첨부되는지를 확인한다.
     * 3.사진이 첨부되면 자동 저장되는지를 확인한다.
     *
     * @throws Throwable thread 예외
     */
    @Test
    public void TestF() throws Throwable {
        TestE();

        onView(allOf(withId(R.id.memo_mod), withId(R.id.memo_delete), isDisplayed()));

        onView(withId(R.id.memoView_title)).check(ViewAssertions.matches(withText("Unit Test Sample")));
        onView(withId(R.id.memoView_content)).check(ViewAssertions.matches(withText("Unit Test Sample")));

        onView(withId(R.id.memo_mod)).perform(ViewActions.click());

        onView(allOf(
                withId(R.id.title_edit), withId(R.id.content_edit),
                withId(R.id.memo_edit_bar), withId(R.id.memo_color_button), isDisplayed()));

//        ViewInteraction imgAdd = onView(allOf(childAtPosition(withId(R.id.memo_edit_bar), 0),
//                isDisplayed()));
//
//        imgAdd.perform(ViewActions.click());
//
//        onView(withId(R.id.menu_web)).check(matches(isDisplayed())).perform(ViewActions.click());
//
//        onView(withId(R.id.url_input)).check(matches(isDisplayed())).perform(ViewActions.typeText(
//                "https://www.bensound.com/bensound-img/dance.jpg"));
//        onView(withText(R.string.alert_ok)).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()))
//                .perform(ViewActions.click());
//
//        onView(new RecyclerViewMatcher(R.id.memo_img).atPositionOnView(0, R.id.memo_imgModel))
//                .check(matches(isDisplayed()));
//
//        Thread.sleep(1500);
//
//        onView(withText(R.string.memo_saved_msg)).inRoot(RootMatchers.withDecorView(Matchers.not(
//                Matchers.is(getCurrentActivity().getWindow().getDecorView()))
//        )).check(matches(isDisplayed()));
    }

    /**
     * 메모를 삭제하는 테스트 메소드입니다.
     */
    @Test
    public void TestG() {
        onView(new RecyclerViewMatcher(R.id.memo_view).atPositionOnView(itemCount - 1, R.id.memo_card))
                .check(matches(isDisplayed())).perform(ViewActions.click());

        onView(allOf(withId(R.id.memo_mod), withId(R.id.memo_delete), isDisplayed()));
        onView(withId(R.id.memo_delete)).perform(ViewActions.click());
        onView(withText(R.string.alert_ok)).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()))
                .perform(ViewActions.click());

        onView(withText(R.string.memo_deleted_msg)).inRoot(RootMatchers.withDecorView(Matchers.not(
                Matchers.is(getCurrentActivity().getWindow().getDecorView()))
        )).check(matches(isDisplayed()));
    }

    /**
     * 테스트 환경(Espresso)에서 현재 Activity를 반환하는 메소드입니다.
     * @return Current Activity
     */
    private Activity getCurrentActivity() {
        Instrumentation cur = InstrumentationRegistry.getInstrumentation();
        cur.waitForIdleSync();

        final Activity[] activity = new Activity[1];

        cur.runOnMainSync(() -> {
            Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED);
            activity[0] = Iterables.getOnlyElement(activities);
        });

        return activity[0];
    }

    /**
     * TextView 컴포넌트에서 String 형태로 텍스트 값을 추출하는 함수입니다.
     *
     * @param matcher: withId 함수를 이용하여 컴포넌트 ID 값을 사용하십시오.
     * @return TextView 의 값
     */
    private String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view;
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    /**
     * 뷰의 자식 뷰를 가져오는 메소드입니다.
     *
     * @param parentMatcher 자식 뷰를 가진 부모 뷰
     * @param position 자식 뷰의 위치
     * @return 자식 뷰 Matcher
     */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * RecyclerView의 리소스를 확인하기 위해 구현한 메소드입니다.
     * Espresso 의 샘플 코드를 인용하였습니다.
     *
     * espresso sample code
     */
    class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPosition(final int position) {
            return atPositionOnView(position, -1);
        }

        public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)",
                                    recyclerViewId);
                        }
                    }

                    description.appendText("with id: " + idDescription);
                }

                public boolean matchesSafely(View view) {

                    this.resources = view.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            childView = Objects.requireNonNull(recyclerView
                                    .findViewHolderForAdapterPosition(position)).itemView;
                        }
                        else {
                            return false;
                        }
                    }

                    if (targetViewId == -1)
                        return view == childView;
                    else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }

                }
            };
        }
    }
}

package herdenmanagement;

import android.content.Context;
import android.content.Intent;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.ba.herdenmanagement.R;
import herdenmanagement.model.Acker;
import herdenmanagement.model.Rindvieh;
import herdenmanagement.view.AckerView;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class InstrumentedTest {

    private AckerView ackerView;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,    // initialTouchMode
            false);  // launchActivity. False to set intent per method

    @Before
    public void setupAcker() {
        // launch the app
        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        // create an a view
        ackerView = (AckerView) activityRule.getActivity().findViewById(R.id.acker_view);
    }

    @Test
    public void ackerView() {
        // is there a view?
        AckerView ackerView = (AckerView) activityRule.getActivity().findViewById(R.id.acker_view);
        assertNotNull(ackerView);

        final Rindvieh rindvieh = new Rindvieh("Vera");

        ackerView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                assertEquals(child.getId(), rindvieh.gibId());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });

        ackerView.getAcker().lassRindWeiden(rindvieh);
    }

    @Test
    public void acker() {
        Acker acker = new Acker(10, 10);
        ackerView.setAcker(acker);

        assertEquals(acker, ackerView.getAcker());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();

        assertNotNull(appContext.getPackageName());
    }
}

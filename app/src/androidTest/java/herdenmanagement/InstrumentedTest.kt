package herdenmanagement

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import herdenmanagement.view.AckerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import android.view.View
import de.ba.herdenmanagement.R
import herdenmanagement.model.Rindvieh
import android.view.ViewGroup.OnHierarchyChangeListener
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTest {

    private var ackerView: AckerView? = null

    @get:Rule
    public val rule: ActivityScenarioRule<*> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun setupAcker() {
        val scenario = rule.getScenario()
        scenario.onActivity {
            // create an a view
            ackerView = it.findViewById(R.id.acker_view)

            Assert.assertNotNull(ackerView)
            Assert.assertEquals(ackerView!!.acker.kaelber.size.toLong(), 0)
            Assert.assertEquals(ackerView!!.acker.viecher.size.toLong(), 0)

            var rindvieh: Rindvieh? = null

            ackerView!!.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
                override fun onChildViewAdded(parent: View, child: View) {
                    Assert.assertEquals(child.id.toLong(), rindvieh?.id?.toLong())
                }

                override fun onChildViewRemoved(parent: View, child: View) {}
            })

            rindvieh = ackerView!!.acker.lassRindWeiden("Vera")
        }

        // launch the app
        // rule.scenario.onActivity(ActivityAction { activity: Activity ->
    }

    /* @Test
    public void ackerView() {
        // is there a view?
        AckerView ackerView = (AckerView) activityRule.getActivity().findViewById(R.id.acker_view);

    }

    @Test
    public void acker() {
        Acker acker = new Acker(10, 10);
        ackerView.setAcker$herde_manager_app(acker);

        assertEquals(acker, ackerView.getAcker$herde_manager_app());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();

        assertNotNull(appContext.getPackageName());
    }*/
}
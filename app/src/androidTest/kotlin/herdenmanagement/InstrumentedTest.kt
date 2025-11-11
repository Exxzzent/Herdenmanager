package herdenmanagement

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import herdenmanagement.view.AckerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import de.dhsn.herdenmanagement.R
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

            ackerView!!.acker.lassRindWeiden("Vera")

            Assert.assertEquals(ackerView!!.acker.viecher.size.toLong(), 1)
        }
    }
}
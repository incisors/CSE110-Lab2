package edu.ucsd.cse110.lab2;

import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void test_one_plus_one_equals_two() {
        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
           Button btn1 = activity.findViewById(R.id.btn_one);
           Button btnPlus = activity.findViewById(R.id.btn_plus);
           Button btnEq = activity.findViewById(R.id.btn_equals);
           TextView display = activity.findViewById(R.id.display);

           btn1.performClick();
           btnPlus.performClick();
           btn1.performClick();
           btnEq.performClick();

           assertEquals("2", display.getText());
        });
    }
}

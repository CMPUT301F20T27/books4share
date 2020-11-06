package com.example.books4share;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for NotificationActivity. All the UI tests are written here.
 */
public class NotificationActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<NotificationActivity> rule =
            new ActivityTestRule<NotificationActivity>(NotificationActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Check the name of book, borrower and status using assertTrue.
     */
    @Test
    public void checkIncomingList() {
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
        assertTrue(solo.searchText("book1"));
        assertTrue(solo.searchText("borrower1"));
        assertTrue(solo.searchText("Request from:"));
        assertTrue(solo.searchText("Pending"));
    }

    /**
     * Check the name of book, status and owner using assertTrue.
     */
    @Test
    public void checkOutgoingList() {
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
        assertTrue(solo.searchText("book1"));
        assertTrue(solo.searchText("Pending"));
        assertTrue(solo.searchText("Owner:"));
        assertTrue(solo.searchText("owner1"));
    }

    /**
     * Check whether switch to AcceptActivity.
     */
    @Test
    public void checkIncomingPending() {
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
        solo.clickOnText("borrower1");
        solo.assertCurrentActivity("Wrong activity", AcceptActivity.class);
    }

    /**
     * Check whether switch to ViewRequestActivity.
     */
    @Test
    public void checkOutgoingAccepted() {
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
        solo.clickOnText("owner2");
        solo.assertCurrentActivity("Wrong activity", ViewRequestActivity.class);
    }

    /**
     * Check whether back to the last activity.
     */
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
        solo.clickOnText("borrower1");
        solo.assertCurrentActivity("Wrong activity", AcceptActivity.class);
        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Wrong activity", NotificationActivity.class);
    }

    /**
     * Closes the activity after each test.
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}

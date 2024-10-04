package com.main.climbingdiary.activities

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import com.main.climbingdiary.helper.TestHelper
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
        Intents.release()
    }

    @Before
    fun setUp(){
        activityScenario = TestHelper.initDefaultScenario()
        Intents.init()
    }

    @Test
    fun onCreate() {
        activityScenario.moveToState(Lifecycle.State.CREATED)
    }
}
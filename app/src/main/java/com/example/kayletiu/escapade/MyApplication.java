package com.example.kayletiu.escapade;

import android.app.Application;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kayle Tiu on 08/04/2018.
 */

public class MyApplication extends Application {
    public Timer getmActivityTransitionTimer() {
        return mActivityTransitionTimer;
    }

    public void setmActivityTransitionTimer(Timer mActivityTransitionTimer) {
        this.mActivityTransitionTimer = mActivityTransitionTimer;
    }

    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;

    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                MyApplication.this.wasInBackground = true;
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        this.wasInBackground = false;
    }
}

package com.example.kayletiu.escapade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoryActivity extends AppCompatActivity {
    private ImageButton backBtn;
    private List<World> worldList = new ArrayList<>();
    private RecyclerView recyclerView;
    private WorldsAdapter mAdapter;
    private final static String TAG = "StoryActivity";
    private final static String CURRENT_LEVEL = "currentLevel";
    private final static String CURRENT_LIVES = "currentLives";
    private final static String IS_MUSIC_ON = "isMusicOn";
    private final static String IS_SFX_ON = "isSfxOn";
    private final static int STORY_MODE = 1;
    public boolean isSfxOn = true;
    public boolean isMusicOn = true;
    private int currentLevel;
    private Intent svc;
    private int currentLives = 5;
    private int highScore;
    private boolean paused = false;
    private boolean scheduled = false;
    private TextView lives;
    SharedPreferences preferencesSettings;
    SharedPreferences.Editor preferenceEditor;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        this.pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        this.alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.slime);
        return builder.build();
    }

    @Override
    protected void onPause() {
        stopService(svc);
        paused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWorld();
        paused = false;
        if (scheduled) {
            this.alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            this.alarmManager.cancel(this.pendingIntent);
            scheduled = false;
        }
        if (this.isMusicOn){
            startService(svc);
        }
        this.lives.setText("LIVES " + Integer.toString(this.preferencesSettings.getInt("currentLives", 5)));
//
        delayedHide(0);
    }

    private void loadWorld(){
        worldList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int level1 = 0;
        int level2 = 0;
        int level3 = 0;
        this.currentLevel = preferencesSettings.getInt("currentLevel", 1);
        Log.d("finishedlevel", "saload" + this.currentLevel);
        if (this.currentLevel < 5){
            level1 = this.currentLevel;
            level2 = 0;
            level3 = 0;
        }
        else if (this.currentLevel < 10){
            level1 = 5;
            level2 = this.currentLevel % 5;
            level3 = 0;
        }
        else if (this.currentLevel < 15){
            level1 = 5;
            level2 = 5;
            level3 = currentLevel % 5;
        }
        else {
            level1 = 5;
            level2 = 5;
            level3 = 5;
        }

        worldList.add(new World(1, R.drawable.story_world1_bg, R.drawable.world1_level1, R.drawable.world1_level2, R.drawable.world1_level3, R.drawable.world1_level4, R.drawable.world1_level5, level1, R.drawable.world1_locked));
        worldList.add(new World(2, R.drawable.story_world2_bg, R.drawable.world2_level1, R.drawable.world2_level2, R.drawable.world2_level3, R.drawable.world2_level4, R.drawable.world2_level5, level2, R.drawable.world2_locked));
        worldList.add(new World(3, R.drawable.story_world3_bg, R.drawable.world3_level1, R.drawable.world3_level2, R.drawable.world3_level3, R.drawable.world3_level4, R.drawable.world3_level5, level3, R.drawable.world3_locked));
        worldList.add(new World(R.drawable.world_soon));
        mAdapter = new WorldsAdapter(worldList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final Context context = this;

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        if(position == worldList.size() - 1){
//
//                            AlertDialog.Builder b1 = new AlertDialog.Builder(context);
//                            b1.setMessage("This world is not yet unlocked!");
//                            b1.setCancelable(true);
//                            b1.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                }
//                            });
//                            AlertDialog alert = b1.create();
//                            alert.show();
//
//
//                        }
//                    }
//
//                })
//        );
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK){
//           this.currentLevel = data.getExtras().getInt("currentLevel");
//            Log.d("finishedlevel", currentLevel + "beforeload");
//            loadWorld();
//            Log.d("finishedlevel", "loaded");
//        }
//        if (resultCode == Activity.RESULT_CANCELED){
//            currentLives -= 1;
//            String sLives = "Lives: " + currentLives;
//            TextView tvlives = findViewById(R.id.tv_lives);
//            tvlives.setText(sLives.toString());
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            loadWorld();
            Log.d("CHECKCHECK", "RESULT OK!");
        }
    }

    public void updateLives(){
        final SharedPreferences sharedPreferences = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor preferenceEditor = getSharedPreferences("MySettings", 0).edit();
        final int[] lives = {0};
        lives[0] = sharedPreferences.getInt("currentLives", 0);
        Log.d("StoryActivity", lives[0] + "");
        if (lives[0] < 5) {
            lives[0]++;
            preferenceEditor.putInt("currentLives", lives[0]);
            preferenceEditor.apply();
            TextView tvLives = findViewById(R.id.tv_lives);
            tvLives.setText("LIVES "  + Integer.toString(lives[0]));
        }
        else {
            if (paused && !scheduled) {
                scheduleNotification(getNotification("Your Lives are Refilled"), 0);
                scheduled = true;
            }
            preferenceEditor.putInt("currentLives", 5);
            preferenceEditor.apply();
            TextView tvLives = findViewById(R.id.tv_lives);
            tvLives.setText("LIVES "  + Integer.toString(lives[0]));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
//        Bundle bundle = getIntent().getExtras();
//        lives = findViewById(R.id.tv_lives);
//        if (bundle != null){
//            currentLevel = bundle.getInt("currentLevel");
//        }
//        else {
//        }
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                updateLives();
                h.postDelayed(this, 300000);
            }
        });
        lives = findViewById(R.id.tv_lives);

        preferencesSettings = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesSettings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        currentLevel = preferencesSettings.getInt("currentLevel", 1);
        this.isMusicOn = preferencesSettings.getBoolean("isMusicOn", false);
        Log.d("StoryActivity", "isMusicOn" + this.isMusicOn);
        isSfxOn = preferencesSettings.getBoolean("isSfxOn", true);
        svc =new Intent(this, MusicService.class);
        Log.d("LEVEL", "current" + currentLevel);
        if (isSfxOn) {
        }
        else {
        }
        if (this.isMusicOn){
            startService(svc);
        }
        else {
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        loadWorld();

        backBtn = findViewById(R.id.button_story_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(12, intent);
                finish();
            }
        });



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(0);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

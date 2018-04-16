package com.example.kayletiu.escapade;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kayletiu.escapade.Canvas.MainActivity;

import org.w3c.dom.Text;

public class StartActivity extends AppCompatActivity implements PlaySongListener {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
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
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private final static String TAG = "StartActivity";
    private final static String CURRENT_LEVEL = "currentLevel";
    private final static String CURRENT_LIVES = "currentLives";
    private final static String IS_MUSIC_ON = "isMusicOn";
    private final static String IS_SFX_ON = "isSfxOn";
    private final static int STORY_MODE = 1;
    public boolean isSfxOn = true;
    public boolean isMusicOn = true;
    private int currentLevel;
    private Intent svc;
    private int currentLives;
    private int highScore;
    private TextView tvHighScore;
    SharedPreferences preferencesSettings;
    SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        preferencesSettings = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesSettings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        currentLevel = preferencesSettings.getInt("currentLevel", 1);
        isMusicOn = preferencesSettings.getBoolean("isMusicOn", true);
        isSfxOn = preferencesSettings.getBoolean("isSfxOn", true);
        highScore = preferencesSettings.getInt("highScore", 0);
        this.tvHighScore = findViewById(R.id.highScore);
        String sHighScore = Integer.toString(this.highScore);
        tvHighScore.setText(sHighScore.toString());

        svc =new Intent(this, MusicService.class);
        if (isMusicOn) {
            startService(svc);
        }

        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.movingPikoy);

        View v1 = getWindow().getDecorView();
        v1.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageView moving = (ImageView) findViewById(R.id.movingPikoy);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                moving,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(500);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        ImageButton play = (ImageButton) findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceEditor = preferencesSettings.edit();
                preferenceEditor.putInt("world",0);
                preferenceEditor.putInt("level",0);
                preferenceEditor.apply();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("requestCode", 1);
                bundle.putInt("highScore", highScore);
                startActivityForResult(intent, 1);
            }
        });

        ImageButton story = (ImageButton) findViewById(R.id.storyButton);
        story.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, StoryActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(IS_MUSIC_ON, isMusicOn);
//                bundle.putBoolean(IS_SFX_ON, isSfxOn);
//                bundle.putInt(CURRENT_LEVEL, currentLevel);
//                bundle.putInt(CURRENT_LIVES, currentLives);
//                intent.putExtras(bundle);
                startActivityForResult(intent, STORY_MODE);
            }
        });

        ImageButton sfx = (ImageButton) findViewById(R.id.sfxButton);
        if(isSfxOn){
            sfx.setImageResource(R.drawable.button_sfx_off);
            isSfxOn = false;

        }
        else{
            sfx.setImageResource(R.drawable.button_sfx_on);
            isSfxOn = true;

        }
        sfx.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageButton sfx = (ImageButton) findViewById(R.id.sfxButton);
                if(isSfxOn){
                    sfx.setImageResource(R.drawable.button_sfx_off);
                    Log.d("SFX", "SFX OFF");
                    isSfxOn = false;
                }
                else{
                    sfx.setImageResource(R.drawable.button_sfx_on);
                    Log.d("SFX", "SFX ON");
                    isSfxOn = true;

                }
            }
        });

        ImageButton music = (ImageButton) findViewById(R.id.musicButton);
        if(isMusicOn){
            startService(svc);
            music.setImageResource(R.drawable.button_music_on);
        }
        else{
            music.setImageResource(R.drawable.button_music_off);
        }
        music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageButton music = (ImageButton) findViewById(R.id.musicButton);
                if(isMusicOn){
                    stopService(svc);
                    music.setImageResource(R.drawable.button_music_off);
                    isMusicOn = false;
                    Log.d("Music", "Music turned off");
                }
                else{
                    startService(svc);
                    music.setImageResource(R.drawable.button_music_on);
                    isMusicOn = true;
                    Log.d("Music", "Music turned on");
                }
            }
        });

        ImageButton help = findViewById(R.id.helpButton);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_instruction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final ImageButton backback = dialog.findViewById(R.id.backback);
        backback.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                dialog.hide();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.show();
            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 12) {
//
//        }
//        else {
//            Bundle bundle = data.getExtras();
//            int score = bundle.getInt("score");
//            Log.d("StartActivity", "score: " + score);
//            if (this.highScore < score) {
//                this.highScore = score;
//                this.preferenceEditor = preferencesSettings.edit();
//                this.preferenceEditor.putInt("highScore", highScore);
//                preferenceEditor.apply();
//                String sHighScore = Integer.toString(this.highScore);
//                tvHighScore.setText(sHighScore.toString());
//                Log.d("StartActivity", "new high score: " + this.highScore);
//            }
//
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (isMusicOn) {
            stopService(svc);
        }
        this.preferenceEditor = preferencesSettings.edit();
        this.preferenceEditor.putBoolean("isMusicOn", isMusicOn);
//        this.preferenceEditor.putBoolean("isSfxOn", isSfxOn);
//        this.preferenceEditor.putInt("currentLevel", currentLevel);
//        this.preferenceEditor.putInt("currentLives", currentLives);
        this.preferenceEditor.putInt("highScore", highScore);
        preferenceEditor.apply();
        Log.d("StartActivity", "sent isMusicOn: " + isMusicOn);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        preferencesSettings = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesSettings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        highScore = preferencesSettings.getInt("highScore", 0);
        this.tvHighScore = findViewById(R.id.highScore);
        String sHighScore = Integer.toString(this.highScore);
        tvHighScore.setText(sHighScore.toString());

        Log.d("QWEQWEQWEQWE", "HIGHSCORE: " + highScore);

        if (isMusicOn) {
            startService(svc);
        }
        View v1 = getWindow().getDecorView();
        v1.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onDestroy() {
        stopService(svc);
        super.onDestroy();
    }

    private Activity getActivity() {
        return this;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            //show();
        }
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


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("EXIT GAME")
                .setMessage("Are you sure you want to exit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void onPlayRequested(int songIndex) {

    }

    @Override
    public void onSongUpdated(int songIndex) {

    }
}

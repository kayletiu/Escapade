package com.example.kayletiu.escapade.Canvas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.kayletiu.escapade.R;

public class MainActivity extends Activity {
    private MainThread mt;
//    private int highScore = 0;
//    private SharedPreferences preferencesSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null){
//                highScore = bundle.getInt("highScore");
//                Log.d("MainActivity", "highScore: " + highScore);
//        }



        //setFullScreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setNoTitle
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        FrameLayout game = new FrameLayout(this);


        final GameSurface gs = new GameSurface(this,this);

        final ViewDialog alert = new ViewDialog(gs);
        LinearLayout gameWidgets = new LinearLayout(this);

        ImageButton pauseButton = new ImageButton(this);

        pauseButton.setImageResource(R.drawable.button_pause);
        pauseButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        pauseButton.setBackground(null);
        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        pauseButton.setX(deviceWidth - 250);

        pauseButton.setLayoutParams(new ViewGroup.LayoutParams(300,200));
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.pause(true);
                alert.showDialog(MainActivity.this);
            }
        });

        gameWidgets.addView(pauseButton);

        game.addView(gs);
        game.addView(gameWidgets);

        mt = gs.getThread();




        this.setContentView(game);
    }

    public void quit(){
        finish();
    }

    @Override
    protected void onResume() {


        super.onResume();

    }

    @Override
    protected void onPause() {
        boolean retry = true;
        while (retry){
            try {
                mt.setRunning(false);
                mt.join();
                retry = false;
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        super.onPause();
    }

}

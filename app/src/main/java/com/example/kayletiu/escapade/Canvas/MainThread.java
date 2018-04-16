package com.example.kayletiu.escapade.Canvas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kayletiu.escapade.R;

import org.w3c.dom.Text;

/**
 * Created by Jayvee Gabriel on 29/03/2018.
 */

public class MainThread extends Thread {
    private boolean running;

    private SurfaceHolder surfaceHolder;
    private GameSurface gameSurface;
    private Activity activity;
    private final static int MAX_FPS = 50;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000/MAX_FPS;
    private Handler handler;
    private Handler winHandler;
    private int ctr = 1;
    private int ctr2 = 1;

    public MainThread(SurfaceHolder surfaceHolder, final GameSurface gameSurface, final Activity activity){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameSurface = gameSurface;
        this.activity = activity;
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if(msg.arg1==1)
                {
                    if(ctr == 0){

                        gameSurface.pause(true);


                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.layout_died);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView tv2 = dialog.findViewById(R.id.score_tv);
                        tv2.setText(gameSurface.getScore() + "".toString());
                        TextView tv1 = dialog.findViewById(R.id.best_tv);

                        TextView tv3 = dialog.findViewById(R.id.world_textview_died);
                        tv3.setText("World " + gameSurface.getCurrentWorld() + "-" + gameSurface.getCurrentLevel() + "".toString());


                        if(gameSurface.isInfinite()){
                            SharedPreferences preferencesSettings = PreferenceManager.getDefaultSharedPreferences(activity);
                            preferencesSettings = activity.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
                            int high = preferencesSettings.getInt("highScore", 0);
                            tv1.setText(high + "".toString());
                        }
                        else{
                            ImageView i = dialog.findViewById(R.id.best_image);
                            i.setBackgroundResource(R.drawable.goal);
                            int goal = gameSurface.getScores()[gameSurface.getCurrentWorld() - 1][gameSurface.getCurrentLevel() - 1];
                            tv1.setText(goal +"".toString());
                        }


                        ImageButton dead = dialog.findViewById(R.id.back_died);
                        dead.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                dialog.dismiss();

                                activity.finish();

                            }
                        });

                        ImageButton reset = dialog.findViewById(R.id.reset_died);
                        reset.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences preferencesSettings = PreferenceManager.getDefaultSharedPreferences(activity);
                                preferencesSettings = activity.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
                                if (preferencesSettings.getInt("currentLives", 5) == 0){
                                    final Dialog dialog2 = new Dialog(view.getContext());
                                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog2.setCancelable(false);
                                    dialog2.setContentView(R.layout.reminder_no_lives);
                                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    ImageButton i1 = dialog2.findViewById(R.id.button_exit);
                                    i1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog2.dismiss();
                                            activity.finish();
                                        }
                                    });


                                    dialog.dismiss();
                                    dialog2.show();
                                }
                                else{

                                    gameSurface.reset();

                                    dialog.dismiss();
                                }
                            }
                        });


                        dialog.show();
                        ctr = 1;
                    }

                }
                return false;
            }
        });

        winHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if(msg.arg1==1)
                {
                    if(ctr2 == 0){
                        if(gameSurface.getCurrentLevel() == 5){
                            gameSurface.pause(true);
                            final Dialog dialog = new Dialog(activity);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.layout_win_fifth);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tv1 = dialog.findViewById(R.id.world_textview_win);
                            tv1.setText("World " + gameSurface.getCurrentWorld() + "-" + gameSurface.getCurrentLevel() + "".toString());
                            ImageButton back = dialog.findViewById(R.id.button_exit);
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    dialog.dismiss();
                                    activity.setResult(Activity.RESULT_OK);

                                    activity.finish();

                                }
                            });



                            dialog.show();
                        }
                        else{

                            gameSurface.pause(true);
                            final Dialog dialog = new Dialog(activity);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.layout_win);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tv1 = dialog.findViewById(R.id.world_textview);
                            tv1.setText("World " + gameSurface.getCurrentWorld() + "-" + gameSurface.getCurrentLevel() + "".toString());
                            ImageButton back = dialog.findViewById(R.id.button_exit);
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    dialog.dismiss();
                                    activity.setResult(Activity.RESULT_OK);

                                    activity.finish();

                                }
                            });

                            ImageButton next = dialog.findViewById(R.id.button_next);
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialog.dismiss();
                                    gameSurface.nextLevel();

                                }
                            });

                            dialog.show();
                        }
                        ctr2 = 1;
                    }

                }
                return false;
            }
        });
    }
    public void setRunning(boolean running){
        this.running = running;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        long beginTime;
        long timeDiff;
        int sleepTime;
        int framesSkipped;

        sleepTime = 0;
        while (running){
            canvas = null;

            if (!gameSurface.isDead) {
                ctr = 0;
            }
            if(!gameSurface.isWin()){
                ctr2 = 0;
            }
            if(gameSurface.isDead()) {
                Message m = new Message();
                m.arg1 = 1;
                handler.sendMessage(m);
            }
            if(gameSurface.isWin()){
                Message m = new Message();
                m.arg1 = 1;
                winHandler.sendMessage(m);
            }
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;


                    this.gameSurface.update();
                    this.gameSurface.onDraw(canvas);



                    timeDiff = System.currentTimeMillis() - beginTime;
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);
                    if(sleepTime > 0 ){
                        //WE ARE OKAY DAPAT.
                        try{
                            //useful sa battery saving
                            Thread.sleep(sleepTime);

                        }catch (InterruptedException e){

                        }
                        while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS){
                            this.gameSurface.update();
                            sleepTime += FRAME_PERIOD;
                            framesSkipped++;
                        }
                    }

                }
            }
            finally {
                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }



    }
}

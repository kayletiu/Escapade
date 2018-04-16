package com.example.kayletiu.escapade.Canvas;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kayletiu.escapade.R;
import com.example.kayletiu.escapade.StartActivity;
import com.example.kayletiu.escapade.StoryActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Jayvee Gabriel on 29/03/2018.
 */

public class GameSurface extends android.view.SurfaceView implements SurfaceHolder.Callback{


    private MainThread thread;
    private Slime slime;
    private Wall leftWall;
    private Wall rightWall;
    private Wall secondLeftWall;
    private Wall secondRightWall;
    private  Timer t;
    private  Timer t2;
    private TimerTask timerTask;
    private TimerTask timerTask2;
    private ArrayList<Spike> spikes;
    private LinkedList<Boolean> booleanList;
    private boolean isReversed;


    private int deviceHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    private int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private double widthRatio = deviceWidth / 1080.0;
    private double heightRatio = deviceHeight / 2046.0;

    private Bitmap treeTemp = BitmapFactory.decodeResource(getResources(), R.drawable.tree_world_one);
    private Bitmap leftSpikeTemp = BitmapFactory.decodeResource(getResources(),R.drawable.left_branch_world_one);
    private Bitmap rightSpikeTemp = BitmapFactory.decodeResource(getResources(),R.drawable.right_branch_world_one);

    private Bitmap firstBG = BitmapFactory.decodeResource(getResources(), R.drawable.first_bg_world_one);
    private Bitmap secondBG = BitmapFactory.decodeResource(getResources(),R.drawable.second_bg_world_one);
    private Bitmap thirdBG = BitmapFactory.decodeResource(getResources(),R.drawable.third_bg_world_one);


    private Bitmap slimePic1Temp = BitmapFactory.decodeResource(getResources(),R.drawable.slime);
    private Bitmap slimePic2Temp = BitmapFactory.decodeResource(getResources(),R.drawable.slime_world3);
    private Bitmap slimePic3Temp = BitmapFactory.decodeResource(getResources(),R.drawable.slime_world2);

    private Bitmap rocketPicTemp = BitmapFactory.decodeResource(getResources(),R.drawable.rocket);
    private Bitmap warningPicTemp = BitmapFactory.decodeResource(getResources(), R.drawable.warning);

    private Bitmap tree = Bitmap.createScaledBitmap(treeTemp, (int)(widthRatio * treeTemp.getWidth()), (int)(heightRatio * treeTemp.getHeight()), false);
    private Bitmap leftSpike = Bitmap.createScaledBitmap(leftSpikeTemp, (int)(widthRatio * leftSpikeTemp.getWidth()), (int)(heightRatio * leftSpikeTemp.getHeight()), false);
    private Bitmap rightSpike = Bitmap.createScaledBitmap(rightSpikeTemp, (int)(widthRatio * rightSpikeTemp.getWidth()), (int)(heightRatio * rightSpikeTemp.getHeight()), false);

    private Bitmap slimePic1 = Bitmap.createScaledBitmap(slimePic1Temp, (int)(widthRatio * slimePic1Temp.getWidth()), (int)(heightRatio * slimePic1Temp.getHeight()), false);
    private Bitmap slimePic2 = Bitmap.createScaledBitmap(slimePic2Temp, (int)(widthRatio * slimePic2Temp.getWidth()), (int)(heightRatio * slimePic2Temp.getHeight()), false);
    private Bitmap slimePic3 = Bitmap.createScaledBitmap(slimePic3Temp, (int)(widthRatio * slimePic3Temp.getWidth()), (int)(heightRatio * slimePic3Temp.getHeight()), false);

    private Bitmap rocketPic = Bitmap.createScaledBitmap(rocketPicTemp, (int)(widthRatio * rocketPicTemp.getWidth()), (int)(heightRatio * rocketPicTemp.getHeight()), false);
    private Bitmap warningPic = Bitmap.createScaledBitmap(warningPicTemp, (int)(widthRatio * warningPicTemp.getWidth()), (int)(heightRatio * warningPicTemp.getHeight()), false);

    private Random r1;

    private int score;
    boolean isDead = false;

    private int backgroundY;
    private int backgroundY2;

    private Rocket rocket1;

    private boolean isPaused = false;
    private int currentWorld;
    private int currentLevel;
    private int finalLevel;
    private int highScore;
    private Warning warning;

    private Activity activity;

    private int[][] scores;
    private boolean isInfinite;
    private boolean isWin;

    private boolean isWarningDone;

    SharedPreferences preferencesSettings;
    SharedPreferences.Editor preferenceEditor;

    public GameSurface(Context context,Activity activity) {
        super(context);
//        this.highScore = highScore;
        getHolder().addCallback(this);
        preferencesSettings = PreferenceManager.getDefaultSharedPreferences(activity);
        preferencesSettings = activity.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        this.highScore = preferencesSettings.getInt("highScore", 0);
        this.currentLevel = preferencesSettings.getInt("level", 0);
        this.currentWorld = preferencesSettings.getInt("world", 0);

        isWin = false;
        isWarningDone = true;
        isReversed = false;
        scores = new int[3][5];
        int wow = 10;
        for(int qwe = 0; qwe < 3; qwe++){
            for(int asd = 0; asd < 5; asd++){
                scores[qwe][asd] = wow;
                wow += 10;
            }
        }


        if(currentWorld == 0 && currentLevel == 0){
            isInfinite = true;
        }
        else{
            isInfinite = false;
        }


        if(getCurrentWorld() == 3){


            t = new Timer();
            final GameSurface gs = this;
              timerTask = new TimerTask() {
                @Override
                public void run() {
                    gs.isReversed = !isReversed;
                    gs.isWarningDone = true;
                }
            };
            t.scheduleAtFixedRate(timerTask, 5000,5000);


            t2 = new Timer();
             timerTask2 = new TimerTask() {
                @Override
                public void run(){
                    gs.isWarningDone = false;
                }
            };
            t2.scheduleAtFixedRate(timerTask2, 4000,5000);
        }


        this.activity = activity;


//      slime = new Slime(this, BitmapFactory.decodeResource(getResources(),R.drawable.slime), deviceWidth/ 2,deviceHeight - 100);
        slime = new Slime(this, slimePic1, tree.getWidth(),deviceHeight / 2 + 300, tree);
        thread = new MainThread(getHolder(),this,activity);

        leftWall = new Wall(tree,true, false);
        rightWall = new Wall(tree, false, false);

        secondLeftWall = new Wall(tree, true, true);
        secondRightWall = new Wall(tree, false, true);

        spikes = new ArrayList<>();
        booleanList = new LinkedList<>();

        score = 0;


        r1 = new Random();
        int y = deviceHeight / 2;
//        double y = heightRatio * (2160.0 / 2) ;
        for(int i = 0; i < 12; i++){
            boolean isLeftTemp = r1.nextBoolean();
            if(isLeftTemp){
                spikes.add(new Spike(this,leftSpike, leftWall.getImage().getWidth(), y,isLeftTemp));
            }
            else{
                spikes.add(new Spike(this,rightSpike, deviceWidth - rightWall.getImage().getWidth() - rightSpike.getWidth(), y, isLeftTemp));
            }
            booleanList.offer(isLeftTemp);
//            y -= deviceHeight / 5.115 ;
            y -= 400;
        }


        warning = new Warning(this,warningPic, 0,100);

        rocket1 = new Rocket(this,rocketPic,deviceWidth / 2,deviceHeight * 3);

        if(!isInfinite()){

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.reminder);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView i1 = dialog.findViewById(R.id.image_reminder);
            if(getCurrentWorld() == 1){

                i1.setBackgroundResource(R.drawable.reminder_world_one);
            }
            else if(getCurrentWorld() == 2){

                i1.setBackgroundResource(R.drawable.reminder_world_two);
            }
            else if(getCurrentWorld() == 3){
                i1.setBackgroundResource(R.drawable.reminder_world_three);
            }
            isPaused = true;

            ImageButton i2 = dialog.findViewById(R.id.button_exit);
            i2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    isPaused = false;
                }
            });



            dialog.show();

        }
        backgroundY = 100;
        backgroundY2 = 100 - thirdBG.getHeight();
        setFocusable(true);
    }


    public int getScore(){
        return score;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread.setRunning(true);
        if(thread.isAlive()){
            boolean retry = true;
            while (retry){
                try {
                    thread.setRunning(false);
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!slime.isMoving() && !spikes.get(0).isMoving()) {


            backgroundY += 5;
            backgroundY2 += 5;
            if(backgroundY >= deviceHeight){
                backgroundY = backgroundY2 - thirdBG.getHeight();
            }
            if(backgroundY2 >= deviceHeight){
                backgroundY2 = backgroundY - thirdBG.getHeight();
            }

            leftWall.setMoving(true);
            rightWall.setMoving(true);
            secondLeftWall.setMoving(true);
            secondRightWall.setMoving(true);

            slime.setMoving(true);

            isDead = false;
            float x = event.getX();


            if(isReversed){
                if (x > deviceWidth / 2) {
                    //JUMP RIGHT
                    slime.setJumpLeft(true);

                    if (!spikes.get(0).isMoving()) {
                        if (booleanList.poll()) {
                            isDead = true;
                            lose();
                        } else {
                            score++;
                        }

                    }


                } else {
                    //JUMP RIGHT

                    slime.setJumpLeft(false);
                    if (!spikes.get(0).isMoving()) {
                        if (!booleanList.poll()) {
                            isDead = true;
                            lose();
                        } else {
                            score++;
                        }

                    }


                }
            }
            else{

                if (x <= deviceWidth / 2) {
                    //JUMP RIGHT
                    slime.setJumpLeft(true);

                    if (!spikes.get(0).isMoving()) {
                        if (booleanList.poll()) {
                            isDead = true;
                            lose();
                        } else {
                            score++;
                        }

                    }


                } else {
                    //JUMP RIGHT

                    slime.setJumpLeft(false);
                    if (!spikes.get(0).isMoving()) {
                        if (!booleanList.poll()) {
                            isDead = true;
                            lose();
                        } else {
                            score++;
                        }

                    }


                }
            }
            if(!isInfinite){
                int max = scores[currentWorld - 1][currentLevel - 1];
                if(max <= score){
                    isWin = true;
                    this.preferenceEditor = preferencesSettings.edit();
                    int qwe = preferencesSettings.getInt("currentLevel",0);

                    int maxWorld = qwe / 5 + 1;
                    int maxLevel = qwe % 5;
                    if(currentWorld * 5 -5 + currentLevel >= qwe){

                        this.preferenceEditor.putInt("currentLevel", currentWorld * 5 - 5 + currentLevel + 1);


                        preferenceEditor.apply();

                    }



                }
            }


            if (leftWall.getY() >= deviceHeight) {
                leftWall.setY((int) leftWall.getY() - leftWall.getImage().getHeight() * 2);
            }
            if (secondLeftWall.getY() >= deviceHeight) {
                secondLeftWall.setY((int) secondLeftWall.getY() - secondLeftWall.getImage().getHeight() * 2);
            }
            if (rightWall.getY() >= deviceHeight) {
                rightWall.setY((int) rightWall.getY() - rightWall.getImage().getHeight() * 2);
            }
            if (secondRightWall.getY() >= deviceHeight) {
                secondRightWall.setY((int) secondRightWall.getY() - secondRightWall.getImage().getHeight() * 2);
            }

            for (int i = 0; i < spikes.size(); i++) {
                spikes.get(i).setMoving(true);
//            spikes.get(i).goDown();
                if (spikes.get(i).getY() >= deviceHeight) {
                    boolean isLeftTemp = r1.nextBoolean();
                    if (isLeftTemp) {
                        spikes.get(i).setImage(leftSpike);
                        spikes.get(i).setX(leftWall.getImage().getWidth());
                        spikes.get(i).setLeft(true);
                        booleanList.offer(true);
                    } else {
                        spikes.get(i).setImage(rightSpike);
                        spikes.get(i).setX(deviceWidth - rightWall.getImage().getWidth() - rightSpike.getWidth());
                        spikes.get(i).setLeft(false);
                        booleanList.offer(false);
                    }

                    spikes.get(i).setY(spikes.get(i).getY() -  400 * spikes.size());
                }
            }
            long time = System.currentTimeMillis();
        }
            return super.onTouchEvent(event);

    }

    public boolean isWin(){
        return isWin;
    }
    public void setWin(boolean win){
        this.isWin = win;
    }
    public int randomize(int min, int max){
        int randomNumber = (r1.nextInt((max - min) + 1) + min);
        return randomNumber;
    }
    public void update(){


        if(!isInfinite && currentWorld == 1){
            slime.setImage(slimePic1);
        }
        else if(!isInfinite && currentWorld == 2){
            slime.setImage(slimePic2);
        }
        else if(!isInfinite && currentWorld == 3){
            slime.setImage(slimePic3);
        }

        if(isInfinite && score > highScore){
            highScore = score;
        }
//        slime.setY(slime.getY() + 1);
        if(!isPaused()){

            slime.update();
            for(int i = 0; i < spikes.size(); i++){
                spikes.get(i).update();

            }
            leftWall.update();
            secondLeftWall.update();
            rightWall.update();
            rocket1.update(leftWall.getImage().getWidth());
            secondRightWall.update();

            float leftXRocket = rocket1.getX();
            float rightXRocket = rocket1.getX() + rocket1.getImage().getWidth();
            float downYRocket = rocket1.getY() + rocket1.getImage().getHeight();
            float upYRocket = rocket1.getY();

            float leftXSlime = slime.getX();
            float rightXSlime = slime.getX() + slime.getImage().getWidth();
            float downYSlime = slime.getY() + slime.getImage().getHeight();
            float upYSlime = slime.getY();

            if(getCurrentWorld() == 0 || getCurrentWorld() == 2 || getCurrentWorld() == 3){
                if(rocket1.getY() >= -5000 && rocket1.getY() <= 0){
                    warning.setX(rocket1.getX());
                    warning.setVisible(true);
                }
            }
            else if(getCurrentWorld() == 1){
                if(rocket1.getY() >= -3000 && rocket1.getY() <= 0){
                    warning.setX(rocket1.getX());
                    warning.setVisible(true);
                }
            }

            if(rocket1.getY() > 0){
                warning.setVisible(false);
            }

//            x+= right, x-= left
//             leftXSlime <= rightXRocket; <= left nung
//             rightXSlime >= rightXRocket; >= right nung
//            y+= down, y-= up
            if(downYRocket >= upYSlime && upYSlime >= upYRocket && (leftXSlime <= rightXRocket && rightXSlime >= leftXRocket )){
                if(!isDead){
                    isDead = true;
                    lose();
                }

            }
//            if(leftXSlime <= rightXRocket && upYSlime <= downYRocket && downYSlime >= upYRocket && rightXSlime >= rightXRocket){
//                isDead = true;
//                lose();
//                Log.d("ROCKET", "TUMAMA SA RIGHT");
//            }
//            if(leftXSlime >= leftXRocket && upYSlime <= downYRocket && downYSlime >= upYRocket && rightXSlime <= rightXRocket){
//                isDead = true;
//                lose();
//                Log.d("ROCKET", "TUMAMA SA LEFT");
//            }
            if(isDead){
//            slime.setX(600);
//                lose();

            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if(isReversed){
            canvas.drawColor(android.graphics.Color.rgb(100,14,14));
        }
        else{

            canvas.drawColor(Color.BLACK);

            canvas.drawBitmap(thirdBG,0,backgroundY,null);
            canvas.drawBitmap(secondBG, 0, backgroundY2, null);
        }
        leftWall.draw(canvas);
        rightWall.draw(canvas);
        secondLeftWall.draw(canvas);
        secondRightWall.draw(canvas);

        for(int i = 0; i < spikes.size(); i++){
            spikes.get(i).draw(canvas);
        }
        slime.draw(canvas);
        rocket1.draw(canvas);
        Paint p = new Paint();

        p.setColor(Color.WHITE);
        p.setTextSize(48);
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        if(isInfinite){

            canvas.drawText("HI " + highScore, tree.getWidth() + 20 , 80, p);
        }
        else {
            canvas.drawText("GOAL: " + scores[currentWorld - 1][currentLevel - 1],tree.getWidth() + 20, 80,p);
        }

        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        p.setTextAlign(Paint.Align.LEFT);
        String text = "" + score;
        p.getTextBounds(text, 0,text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;


        p.setTextSize(100);
        canvas.drawText(text, x, 160, p);

        if(warning.isVisible()){

            warning.draw(canvas);
        }

        if(!isWarningDone){
            Paint paint = new Paint();
            paint.setStrokeWidth(30);
            Rect r2 = new Rect(30, 30, deviceWidth - 30, deviceHeight - 30);


            // border
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            canvas.drawRect(r2, paint);
        }
        //HIGH SCORE:
//        if (this.currentWorld == 0 && currentLevel == 0){
//            canvas.drawText("" + highScore,deviceWidth / 2 + 48 / 2,80,p);
//        }
    }
    public MainThread getThread(){
        return thread;
    }

    public void pause(boolean pause){
        this.isPaused = pause;
    }
    public boolean isPaused(){
        return isPaused;
    }

    public boolean isDead(){
        return isDead;
    }

    public Activity getActivity(){
        return activity;
    }

    public void lose(){
//        Intent intent = new Intent((Activity)getContext(), StartActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("score", score);
//        intent.putExtras(bundle);
//        ((Activity)getContext()).setResult(Activity.RESULT_CANCELED, intent);
//        ((Activity)getContext()).finish();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences = activity.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor;
        if(this.isInfinite){
            int highestScore = preferences.getInt("highScore", 0);
            if(score > highestScore){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("highScore",score);
                editor.apply();
            }
        }
        else {
            int currentLives = preferences.getInt("currentLives", 5);
            currentLives -= 1;
            preferenceEditor = preferences.edit();
            preferenceEditor.putInt("currentLives", currentLives);
            preferenceEditor.apply();
        }


    }

    public boolean isInfinite(){
        return isInfinite;
    }
    public void quit(){
        activity.finish();
//        Intent intent = new Intent(this.getContext(), StoryActivity.class);
//        Bundle bundle = new Bundle();
//        if (currentLevel < 5) {
//            bundle.putInt("currentLevel", (currentLevel + 1));
//        }
//        else {
//            bundle.putInt("currentLevel", 5);
//        }
//        Log.d("finishedlevel", "Passing from GS" + (currentLevel + 1)+ "");
//        intent.putExtras(bundle);
//        ((Activity)getContext()).setResult(Activity.RESULT_OK, intent);
//        ((Activity)getContext()).finish();

    }

    public void nextLevel(){
        this.score = 0;

        spikes.clear();
        booleanList.clear();

        rocket1.setY(deviceHeight * 3);
        int y = (deviceHeight / 2) ;
        for(int i = 0; i < 12; i++) {
            boolean isLeftTemp = r1.nextBoolean();
            if (isLeftTemp) {
                spikes.add(new Spike(this, leftSpike, leftWall.getImage().getWidth(), y, isLeftTemp));
            } else {

                spikes.add(new Spike(this, rightSpike, deviceWidth - rightWall.getImage().getWidth() - rightSpike.getWidth(), y, isLeftTemp));
            }
            booleanList.offer(isLeftTemp);
            y -= 400;
        }


        slime.setX(tree.getWidth());
        isDead = false;
        isPaused = false;
        isWin = false;
        preferencesSettings = PreferenceManager.getDefaultSharedPreferences(activity);
        preferencesSettings = activity.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        if(currentLevel + 1== 6){
            preferenceEditor.putInt("level", 1);
            preferenceEditor.putInt("world", currentWorld +1);
        }
        else{
            preferenceEditor.putInt("level", currentLevel + 1);
            preferenceEditor.putInt("world", currentWorld);
        }
        preferenceEditor.apply();
        this.highScore = preferencesSettings.getInt("highScore", 0);
        this.currentLevel = preferencesSettings.getInt("level", 0);
        this.currentWorld = preferencesSettings.getInt("world", 0);


    }


    public void reset(){
        if(currentWorld == 3){

            isWarningDone = true;
            isReversed = false;
        }
        this.score = 0;
        spikes.clear();
        booleanList.clear();
        rocket1.setY(-deviceHeight * 3);
        int y = (deviceHeight / 2) ;
        for(int i = 0; i < 12; i++) {
            boolean isLeftTemp = r1.nextBoolean();
            if (isLeftTemp) {
                spikes.add(new Spike(this, leftSpike, leftWall.getImage().getWidth(), y, isLeftTemp));
            } else {

                spikes.add(new Spike(this, rightSpike, deviceWidth - rightWall.getImage().getWidth() - rightSpike.getWidth(), y, isLeftTemp));
            }
            booleanList.offer(isLeftTemp);
            y -= 400;
        }

        backgroundY = 100;
        backgroundY2 = 100 - thirdBG.getHeight();
        slime.setX(tree.getWidth());
        isDead = false;
        isPaused = false;
    }

    public int[][] getScores(){
        return scores;
    }
    public int getCurrentWorld(){
        return currentWorld;
    }
    public int getCurrentLevel(){
        return currentLevel;
    }

}

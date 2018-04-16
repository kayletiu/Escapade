package com.example.kayletiu.escapade.Canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Jayvee Gabriel on 29/03/2018.
 */

public class Slime extends GameObject{


    public boolean isMoving = false;
    public boolean isOkayToClick = true;
    public boolean isGoingUp = false;
    public boolean isPreviousLeft = true;



    private static final float VELOCITY = 1.8f;
    private long lastDrawNanoTime =-1;


    private int deviceHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    private int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int leftMovingVectorX;
    private int rightMovingVectorX;
    private Bitmap imageWall;

    public boolean goUp = true;
    public boolean isJumpLeft = true;

    private int whiteLength;

    public Slime(GameSurface gs, Bitmap image, float x, float y, Bitmap imageWall) {
        this.image = image;
        this.gameSurface = gs;
        this.x = x;
        this.y = y;
        this.imageWall = imageWall;
        this.leftMovingVectorX = imageWall.getWidth();
        this.rightMovingVectorX = deviceWidth - imageWall.getWidth() - image.getWidth();
        whiteLength = deviceWidth - imageWall.getWidth() - imageWall.getWidth() - image.getWidth();
//        this.x = x - (image.getWidth() / 2);
//        this.y = y - (image.getHeight() / 2);
    }

    public void jumpLeft(int x){
        this.setX(x);
        this.setY(600);
    }

    public void jumpRight(int x){
        this.setX(x);
        this.setY(600);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, getX(),getY(),null);
        lastDrawNanoTime = System.nanoTime();
    }

    public boolean isGoingUp() {
        return isGoingUp;
    }

    public void setGoingUp(boolean goingUp) {
        isGoingUp = goingUp;
    }

    public boolean isJumpLeft() {
        return isJumpLeft;

    }

    public void setJumpLeft(boolean jumpLeft) {

            this.isPreviousLeft = this.isJumpLeft;
            isJumpLeft = jumpLeft;

    }

    public void update(){
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        // Distance moves
        float distance = VELOCITY * deltaTime;

        if(isJumpLeft() && isMoving){
            if(isPreviousLeft){
                x = leftMovingVectorX;
                setMoving(false);
            }
            else {
//                double movingVectorLength = Math.sqrt(leftMovingVectorX * leftMovingVectorX + leftMovingVectorY * leftMovingVectorY);
//                this.x = x - (int) (distance * leftMovingVectorX / movingVectorLength);
//                this.y = y - (int) (distance * leftMovingVectorY / movingVectorLength);
//                if (x <= imageWall.getWidth()) {
//                    setMoving(false);
//                    Log.d("HAHAHAHA","HEHEHEHEHEHEHEHEHEHE");
//                }
                float wew = whiteLength / 5;
                if(x - wew <= leftMovingVectorX){
                    x = leftMovingVectorX;
                    setMoving(false);
                }
                else {
                    this.x = x - wew;
                    if (this.getX() + 1 <= leftMovingVectorX) {
                        setMoving(false);
                    }
                }
            }
        }
        else if(!isJumpLeft() && isMoving){
            if(!isPreviousLeft){
                x = rightMovingVectorX;
                setMoving(false);
            }
            else{

                float wew = whiteLength / 5;
                if(x + wew >= rightMovingVectorX){
                    x = rightMovingVectorX;
                    setMoving(false);
                }
                else {
                    this.x = x + wew;
                    if (this.getX() - 1 >= rightMovingVectorX) {
                        setMoving(false);
                    }
                }
//                double movingVectorLength = Math.sqrt(rightMovingVectorX* rightMovingVectorX + rightMovingVectorY*rightMovingVectorY);
//                this.x = x +  (int)(distance* rightMovingVectorX / movingVectorLength);
//                this.y = y -  (int)(distance* rightMovingVectorY / movingVectorLength);
//                if(x >= deviceWidth - imageWall.getWidth() - image.getWidth()){
//                    setMoving(false);
//                    Log.d("HAHAHAHA","HAHAHAHHAHAHAHA");
//                }
            }
        }


    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
    public boolean isOkayToClick() {
        return isOkayToClick;
    }

    public void setOkayToClick(boolean okayToClick) {
        isOkayToClick = okayToClick;
    }

}

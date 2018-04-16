package com.example.kayletiu.escapade.Canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Jayvee Gabriel on 29/03/2018.
 */

public class Spike extends GameObject{


    private boolean isLeft;
    private int counter = 0;
    private int sum = (int)(deviceHeight / 5.115);
    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isMoving = false;
    public Spike(GameSurface gs, Bitmap image, float x, float y,boolean isLeft) {
        super(gs, image,x,y);
        this.isLeft = isLeft;
    }


    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public void update(){
        if(isMoving){

            this.setY(this.getY() + 40);
            counter += 40;
            if(counter >= 400){
                setMoving(false);
                counter = 0;
            }
        }
    }
}

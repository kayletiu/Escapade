package com.example.kayletiu.escapade.Canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Jayvee Gabriel on 29/03/2018.
 */

public class Wall extends GameObject{

    private boolean isLeft;
    private boolean isSecond;
    private boolean isMoving = false;
    private int number;
    private int counter = 0;
    public Wall(Bitmap image, boolean isLeft, boolean isSecond) {

        super();
        this.image = image;

        this.isLeft = isLeft;
        this.isSecond = isSecond;

        int deviceHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        if(isLeft){
            this.x = 0;
            if(!isSecond){
                this.y = deviceHeight - image.getHeight();
            }
            else{
                this.y = deviceHeight - image.getHeight() * 2;
            }
        }
        else{
            this.x = deviceWidth - image.getWidth();
            if(!isSecond){
                this.y = deviceHeight - image.getHeight();
            }
            else{
                this.y = deviceHeight - image.getHeight() * 2;
            }
        }
    }


    public void draw(Canvas canvas){
        canvas.drawBitmap(image, getX(),getY(),null);
    }



    public void goDown(){
        this.setY(this.getY() + 200);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
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

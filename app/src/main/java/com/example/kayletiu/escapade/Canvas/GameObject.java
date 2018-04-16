package com.example.kayletiu.escapade.Canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Jayvee Gabriel on 29/03/2018.
 */

public abstract class GameObject {

    protected Bitmap image;
    protected float x;
    protected float y;
    protected GameSurface gameSurface;

    protected int deviceHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    protected int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public GameObject(){

    }
    public GameObject(GameSurface gs, Bitmap image, float x, float y){
        this.image = image;
        this.gameSurface = gs;
        this.x = x;
        this.y = y;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    public void draw(Canvas canvas){

        canvas.drawBitmap(image, getX(),getY(),null);
    }




}

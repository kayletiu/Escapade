package com.example.kayletiu.escapade.Canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Jayvee Gabriel on 03/04/2018.
 */

public class Rocket extends GameObject{

    private boolean isMoving = false;
    private Random r1;
    private int counter = 0;
    private int deviceHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
    private int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public Rocket(GameSurface gs, Bitmap image, float x, float y){
        super(gs,image,x,y);
        r1 = new Random();
    }


    public void goDown(){
        if(gameSurface.getCurrentWorld() == 0 || gameSurface.getCurrentWorld() == 2 || gameSurface.getCurrentWorld() == 3){
            this.setY(this.getY() + 110);
        }
        else if(gameSurface.getCurrentWorld() == 1){

            this.setY(this.getY() + 40);
        }

    }

    public void update(int wall){
        goDown();

        if(isMoving){
            goDown();
            this.setY(this.getY() + 40);
            counter += 40;
            if(counter >= 400){
                isMoving = false;
                counter = 0;
            }
        }
        if(y >= deviceHeight){

            setY(getY() - randomize(8000,12000) - deviceHeight);
            setX(randomize(wall, deviceWidth - wall - image.getWidth()));
        }

    }

    public int randomize(int min, int max){
        int randomNumber = (r1.nextInt((max - min) + 1) + min);
        return randomNumber;
    }


}

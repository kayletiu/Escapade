package com.example.kayletiu.escapade.Canvas;

import android.graphics.Bitmap;

/**
 * Created by Kayle Tiu on 08/04/2018.
 */

public class Warning extends GameObject {
    private boolean isVisible;
    public Warning(GameSurface gs, Bitmap image, float x, float y){
        super(gs,image,x,y);
        isVisible = false;

    }
    public boolean isVisible(){
        return isVisible;
    }
    public void setVisible(boolean isVisible){
        this.isVisible = isVisible;
    }

}

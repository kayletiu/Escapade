package com.example.kayletiu.escapade.Canvas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kayletiu.escapade.R;

/**
 * Created by Jayvee Gabriel on 31/03/2018.
 */

public class ViewDialog {
    private final GameSurface gs;
    public ViewDialog(GameSurface gs){
        this.gs = gs;
    }

    public void showDialog(Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv1 = dialog.findViewById(R.id.world_textview_paused);
        if(!gs.isInfinite()){


            tv1.setText("World " + gs.getCurrentWorld() + "-" + gs.getCurrentLevel() + "".toString());
        }
        else {
            tv1.setText("".toString());
        }

        ImageButton quitButton = dialog.findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.quit();
                dialog.dismiss();
            }
        });

        ImageButton resumeButton =dialog.findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.pause(false);
                dialog.dismiss();

            }
        });
        ImageButton resetButton =dialog.findViewById(R.id.retryButton);
        resetButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                gs.pause(false);
                gs.reset();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

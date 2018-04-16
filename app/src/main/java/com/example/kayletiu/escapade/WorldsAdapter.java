package com.example.kayletiu.escapade;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kayletiu.escapade.Canvas.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayvee Gabriel on 14/03/2018.
 */

public class WorldsAdapter extends RecyclerView.Adapter<WorldsAdapter.MyViewHolder> {

    private List<World> worldList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView background;
        public ArrayList<ImageButton> buttons;
        SharedPreferences preferencesSettings;
        SharedPreferences.Editor preferenceEditor;

        public MyViewHolder(View view) {
            super(view);
            preferencesSettings = PreferenceManager.getDefaultSharedPreferences((Activity) view.getContext());
            preferencesSettings = ((Activity) view.getContext()).getSharedPreferences("MySettings", Context.MODE_PRIVATE);
//            background = (ImageView) view.findViewById(R.id.background_item);
            buttons = new ArrayList<ImageButton>();
            ImageButton button = view.findViewById(R.id.level1);
            buttons.add(button);
            button = view.findViewById(R.id.level2);
            buttons.add(button);
            button = view.findViewById(R.id.level3);
            buttons.add(button);
            button = view.findViewById(R.id.level4);
            buttons.add(button);
            button = view.findViewById(R.id.level5);
            buttons.add(button);

        }

        public void setupListener(ImageButton imageButton, final int world, final int level){
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preferencesSettings = PreferenceManager.getDefaultSharedPreferences((Activity) view.getContext());
                    preferencesSettings = ((Activity) view.getContext()).getSharedPreferences("MySettings", Context.MODE_PRIVATE);
                    if (preferencesSettings.getInt("currentLives", 5) == 0){
                        final Dialog dialog = new Dialog(view.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.reminder_no_lives);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton i1 = dialog.findViewById(R.id.button_exit);
                        i1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });



                        dialog.show();
                    }
                    else {
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        preferenceEditor = preferencesSettings.edit();
                        preferenceEditor.putInt("world", world);
                        preferenceEditor.putInt("level", level);
                        preferenceEditor.apply();
                        ((Activity) view.getContext()).startActivityForResult(intent, world);
                    }
                }
            });
        }
    }


    public WorldsAdapter(List<World> worldList) {
        this.worldList = worldList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.world_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final World w1 = worldList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackground(holder.itemView.getContext().getResources().getDrawable(w1.getImgUrl(), null));
        }
        if (w1.getLockedButton() != 0) {
                for (int i = 0; i < w1.getCurrentLevel(); i++) {
                    holder.buttons.get(i).setBackgroundResource(w1.getButtons().get(i));
                    ImageButton imageButton = holder.buttons.get(i);
                    holder.setupListener(imageButton, w1.getWorld(), i + 1);
//                    final int finalI = i;
//                    final int finalI1 = i;
//                    holder.buttons.get(i).setOnTouchListener(new OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            Log.d("WorldsAdapter", "touched");
//                            view.performContextClick();
//                            return false;
//                        }
//                    });
//                    holder.buttons.get(i).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Log.d("WorldsAdapter", "clicked");
//                            Intent intent = new Intent(view.getContext(), MainActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("world", w1.getWorld());
//                            bundle.putInt("level", finalI + 1);
//                            intent.putExtras(bundle);
//                            ((Activity) view.getContext()).startActivityForResult(intent, w1.getWorld());
//                        }
//                    });

                }
                for (int j = 4; j >= w1.getCurrentLevel(); j--){
                    holder.buttons.get(j).setBackgroundResource(w1.getLockedButton());
                }
        }
        else {
            for (int i = 0; i < 5; i++) {
                holder.buttons.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return worldList.size();
    }
}
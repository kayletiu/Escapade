package com.example.kayletiu.escapade;

import java.util.ArrayList;

/**
 * Created by Jayvee Gabriel on 14/03/2018.
 */

public class World {
    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    private int world;
    private int imgUrl;
    private ArrayList<Integer> buttons;
    private int lockedButton;

    public int getLockedButton() {
        return lockedButton;
    }

    public void setLockedButton(int lockedButton) {
        this.lockedButton = lockedButton;
    }


    public ArrayList<Integer> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Integer> buttons) {
        this.buttons = buttons;
    }


    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    private int currentLevel;


    public World(int world, int imgUrl, int button1, int button2, int button3, int button4, int button5, int currentLevel, int lockedButton) {
        this.world = world;
        this.imgUrl = imgUrl;
        this.buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        this.currentLevel = currentLevel;
        this.lockedButton = lockedButton;
    }
    public World (int imgUrl){
        this.imgUrl = imgUrl;
        this.lockedButton = 0;
        this.currentLevel = 0;
    }


    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}


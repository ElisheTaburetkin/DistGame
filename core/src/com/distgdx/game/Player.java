package com.distgdx.game;

import static com.distgdx.game.MyGame.*;

import com.badlogic.gdx.graphics.Texture;

public class Player {
    String name;
    long time;
    float x, y, width, height;
    int phase = 2;
    float vx, vy;
    boolean isAlive = true;
    Texture imgLive, imgDead;

    public Player(String name) {
        this.name = name;
    }
    void Move(){
        x = 144 + height;

    }

}

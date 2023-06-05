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
    boolean isRight;

    public Player(String name) {
        this.name = name;
        x = 144;
    }

    public Player() {
        y = 137;
        x = SCR_WIDTH/2;
        width = 200;
        height = 200;
        isRight = false;
    }

}

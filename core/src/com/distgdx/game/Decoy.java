package com.distgdx.game;

import static com.distgdx.game.MyGame.SCR_HEIGHT;
import static com.distgdx.game.MyGame.SCR_WIDTH;
import static com.distgdx.game.MyGame.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Decoy extends RegularEnemy {
    public Decoy(Texture live, Texture dead) {
        super(live, dead);
        x = SCR_WIDTH/2f;
        y = SCR_HEIGHT/2f;
        width = height = MathUtils.random(30, 170);
        vx = MathUtils.random(-5f, 5);
        vy = MathUtils.random(-5f, 5);
        imgLive = live;
        imgDead = dead;
        img = imgLive;
    }
    float scrX(){ //экранная Х
        return  x - width/2;
    }
    float scrY(){
        return  y - height/2;
    }

    void move(){
        x += vx;
        y += vy;
        if (isAlive) {
            notoutOfBounds();
        }
    }


    void outOfBounds(){
        if(x<0-width/2) x = SCR_WIDTH+width/2;
        if(x>SCR_WIDTH+width/2) x = 0-width/2;
        if(y<0-height/2) y = SCR_HEIGHT+height/2;
        if(y>SCR_HEIGHT+height/2) y = 0-height/2;
    }
    void notoutOfBounds(){
        if(x>SCR_WIDTH-width/2 || x<0+width/2) vx = -vx;
        if(y>SCR_HEIGHT-height/2 || y<0+height/2) vy = -vy;
    }
    boolean isFlip(){
        return vx > 0;
    }
    boolean hit(float tx, float ty){
        if(x-width/2 < tx && tx < x+width/2 && y-height/2<ty && ty < y + height/2){
            isAlive = false;
            img = imgDead;
            phase = 3;
            vx = MathUtils.random(-6, 6);
            vy = MathUtils.random(-20, -12);
            lifes--;
            return true;
        }
        return false;
    }
    void reBorn(){
        isAlive = true;
        x = SCR_WIDTH/2f;
        y = SCR_HEIGHT/2f;
        width = height = MathUtils.random(50, 150);
        vx = MathUtils.random(-5f, 5);
        vy = MathUtils.random(-5f, 5);
        phase = MathUtils.random(0, nPhases-1);
    }
}

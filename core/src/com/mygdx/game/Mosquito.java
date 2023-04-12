package com.mygdx.game;

import static com.mygdx.game.DistGdxGame.*;
import com.badlogic.gdx.math.MathUtils;

public class Mosquito {
    float x, y;
    float width, height;
    float vx, vy;
    int phase, nPhases = 2;
    boolean isAlive = true;

    public Mosquito(){
        x = SCR_WIDTH/2f;
        y = SCR_HEIGHT/2f;
        width = height = MathUtils.random(50, 150);
        vx = MathUtils.random(-5f, 5);
        vy = MathUtils.random(-5f, 5);
        phase = MathUtils.random(0, nPhases-1);
    }
    float scrX(){ //экранная Х
        return  x - width/2;
    }
    float scrY(){
        return  y - height/2;
    }

    void move(){
        x+=vx;
        y+=vy;
        if (isAlive) {
            changePhase();
            notoutOfBounds();
        }
    }
    void changePhase(){
        if (++phase == nPhases) phase = 0;
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
            phase = 1; //в оригинале было 10, но у меня нету 10 файла
            vx = 0;
            vy = -8;
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
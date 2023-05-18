package com.distgdx.game;

abstract class Enemy {
    abstract float scrX();

    abstract float scrY();

    abstract void move();

    abstract void outOfBounds();

    abstract void notoutOfBounds();

    abstract boolean isFlip();

    abstract boolean hit(float tx, float ty);

    abstract boolean playerDeath();

    abstract void reBorn();
}

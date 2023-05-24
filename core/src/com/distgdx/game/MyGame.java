package com.distgdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class MyGame extends Game {
    public static final int SCR_WIDTH = 1280, SCR_HEIGHT = 720;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font, fontLarge;
    InputKeyboard keyboard;

    ScreenIntro screenIntro;
    ScreenGame screenGame;
    ScreenSettings screenSettings;
    ScreenAbout screenAbout;
    ScreenActual screenActual;

    boolean soundOn = true;
    boolean musicOn = true;
    int numEnemy = 10;
    int numDecoy = 3;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        generateFont();
        keyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 8);

        screenIntro = new ScreenIntro(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenAbout = new ScreenAbout(this);
        setScreen(screenIntro);
    }

    void generateFont(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("text_sample.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = new Color().set(1, 0.9f, 0.3f, 1);
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.borderStraight = true;
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        font = generator.generateFont(parameter);
        parameter.size = 70;
        fontLarge = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}


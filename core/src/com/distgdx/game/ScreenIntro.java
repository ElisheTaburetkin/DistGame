package com.distgdx.game;

import static com.distgdx.game.MyGame.SCR_HEIGHT;
import static com.distgdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;


public class ScreenIntro implements Screen {
    MyGame g;
    Texture imgBG;
    Sound sound;
    Texture texture;

    TextButton btnPlay, btnSettings, btnAbout, btnExit;

    public ScreenIntro(MyGame context){
        g = context;
        btnPlay = new TextButton(g.fontLarge, "PLAY", 500, 600);
        btnSettings = new TextButton(g.fontLarge, "SETTINGS", 500, 500);
        btnAbout = new TextButton(g.fontLarge, "ABOUT", 500, 400);
        btnExit = new TextButton(g.fontLarge, "EXIT", 500, 300);
        imgBG = new Texture("ScreenIntro.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()) {
            g.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            g.camera.unproject(g.touch);
            if(btnPlay.hit(g.touch.x, g.touch.y)){
                g.setScreen(g.screenGame);
            }
            if(btnSettings.hit(g.touch.x, g.touch.y)){
                sound.play(0.4f);
                g.setScreen(g.screenSettings);

            }
            if(btnAbout.hit(g.touch.x, g.touch.y)){
                g.setScreen(g.screenAbout);
            }
            if(btnExit.hit(g.touch.x, g.touch.y)){
                Gdx.app.exit();
            }
        }

        // отрисовка графики
        g.camera.update();
        g.batch.setProjectionMatrix(g.camera.combined);
        g.batch.begin();
        g.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(g.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(g.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(g.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(g.batch, btnExit.text, btnExit.x, btnExit.y);
        g.batch.end();

        sound = Gdx.audio.newSound(Gdx.files.internal("cave-ambience.mp3"));

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgBG.dispose();
        texture.dispose();
    }
}


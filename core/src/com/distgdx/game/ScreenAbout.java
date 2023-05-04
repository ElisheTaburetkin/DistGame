package com.distgdx.game;

import static com.distgdx.game.MyGame.SCR_HEIGHT;
import static com.distgdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    MyGame g;
    Texture imgBG;

    TextButton btnBack;
    String textAbout =  "";

    public ScreenAbout(MyGame context){
        g = context;

        btnBack = new TextButton(g.fontLarge, "BACK", 100, 100);
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

            if(btnBack.hit(g.touch.x, g.touch.y)){
                g.setScreen(g.screenIntro);
            }
        }

        // отрисовка графики
        g.camera.update();
        g.batch.setProjectionMatrix(g.camera.combined);
        g.batch.begin();
        g.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        g.font.draw(g.batch, textAbout, 100, SCR_HEIGHT-100);
        btnBack.font.draw(g.batch, btnBack.text, btnBack.x, btnBack.y);
        g.batch.end();
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
    }
}


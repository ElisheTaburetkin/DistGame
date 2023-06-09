package com.distgdx.game;

import static com.distgdx.game.MyGame.SCR_HEIGHT;
import static com.distgdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MyGame g;
    Texture imgBG;
    Sound sound;

    TextButton btnEnemies, btnSound, btnMusic, btnClearTable, btnBack, btnDecoys;
    //Slider slider;
    // состояние
    boolean enterNumEnemies, enterNumDecoys;

    public ScreenSettings(MyGame context){
        g = context;
        btnEnemies = new TextButton(g.fontLarge, "Enemies: "+g.numEnemy, 200, 600);
        btnDecoys = new TextButton(g.fontLarge, "Decoys: "+g.numDecoy, 200, 500);
        btnSound = new TextButton(g.fontLarge, "SOUND ON", 200, 400);
        btnMusic = new TextButton(g.fontLarge, "MUSIC ON", 200, 300);
        btnClearTable = new TextButton(g.fontLarge, "CLEAR TABLE", 200, 200);
        btnBack = new TextButton(g.fontLarge, "BACK", 200, 100);
        imgBG = new Texture("InkedSettingsScreen.jpg");
        sound = Gdx.audio.newSound(Gdx.files.internal("bonk.mp3"));
        /*Skin skin = new Skin();
        slider = new Slider(1, 100, 1, false, skin);
        slider.setPosition(100, 100);*/
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
            if(enterNumEnemies || enterNumDecoys) {
                if(g.keyboard.endOfEdit(g.touch.x, g.touch.y)) {
                    enterNumEnemies = false;
                    enterNumDecoys = false;
                    String s = g.keyboard.getText();
                    String d = g.keyboard.getText();
                    int x, y;
                    try {
                        x = Integer.parseInt(s);
                        y = Integer.parseInt(d);
                    } catch (Exception e){
                        x = 0;
                        y = 0;
                    }
                    if(x>0 && x<1000 || y > 0 && x < 1000) {
                        g.numEnemy = x;
                        g.numDecoy = y;
                        btnEnemies.setText("ENEMIES: "+ x);
                        btnDecoys.setText("DECOYS: " + y);
                    }
                }
            } else {
                if (btnEnemies.hit(g.touch.x, g.touch.y)) {
                    enterNumEnemies = true;
                }
                if (btnDecoys.hit(g.touch.x, g.touch.y)) {
                    enterNumDecoys = true;
                }
                if (btnSound.hit(g.touch.x, g.touch.y)) {
                    g.soundOn = !g.soundOn;
                    if (g.soundOn) {
                        btnSound.setText("SOUND ON");
                    } else {
                        btnSound.setText("SOUND OFF");
                    }
                }

                if (btnMusic.hit(g.touch.x, g.touch.y)) {
                    g.musicOn = !g.musicOn;
                    btnMusic.setText(g.musicOn ? "MUSIC ON" : "MUSIC OFF");
                }
                if(btnClearTable.hit(g.touch.x, g.touch.y)){
                    for (int i = 0; i < g.screenGame.players.length; i++) {
                        g.screenGame.players[i].name = "Noname";
                        g.screenGame.players[i].time = 0;
                        g.screenGame.saveTableOfRecords();
                    }
                    btnClearTable.text = "TABLE CLEARED";
                }
                if (btnBack.hit(g.touch.x, g.touch.y)) {
                    g.setScreen(g.screenIntro);
                    sound.play(0.4f);
                    btnClearTable.text = "CLEAR TABLE";
                }
            }
        }

        // отрисовка графики
        g.camera.update();
        g.batch.setProjectionMatrix(g.camera.combined);
        g.batch.begin();
        g.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnEnemies.font.draw(g.batch, btnEnemies.text, btnEnemies.x, btnEnemies.y);
        btnDecoys.font.draw(g.batch, btnDecoys.text, btnDecoys.x, btnDecoys.y);
        btnSound.font.draw(g.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(g.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnClearTable.font.draw(g.batch, btnClearTable.text, btnClearTable.x, btnClearTable.y);
        btnBack.font.draw(g.batch, btnBack.text, btnBack.x, btnBack.y);
        if(enterNumEnemies){
            g.keyboard.draw(g.batch);
        }
        if(enterNumDecoys){
            g.keyboard.draw(g.batch);
        }
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


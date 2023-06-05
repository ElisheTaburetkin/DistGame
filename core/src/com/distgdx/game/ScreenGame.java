package com.distgdx.game;

import static com.distgdx.game.MyGame.SCR_HEIGHT;
import static com.distgdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Arrays;
import java.util.Comparator;

public class ScreenGame implements Screen {
    MyGame g;

    Texture[] imgEnemy = new Texture[4];
    Texture imgBG;
    Texture imgPlatform;
    Texture[] imgDecoy = new Texture[2];
    Texture imgPlayer;

    int lifes = 5;

    Sound[] sndEnemy = new Sound[4];
    Sound sndSfx;
    Music music;

    RegularEnemy[] regularEnemy;
    Decoy[] decoy;
    int kills;
    long timeStart, timeFromStart;
    Player[] players = new Player[6];
    Player player;

    TextButton btnRestart, btnBack;

    // состояние игры
    public static final int PLAY_GAME = 0, ENTER_NAME = 1, SHOW_TABLE = 2;
    int state = PLAY_GAME;

    public ScreenGame (MyGame context) {
        g = context;
        regularEnemy = new RegularEnemy[g.numEnemy];
        decoy = new Decoy[g.numDecoy];
        //отрисовка кнопок
        btnRestart = new TextButton(g.font, "RESTART", 10, 50);
        btnBack = new TextButton(g.font, "BACK", SCR_WIDTH-220, 50);
        //Текстуры Enemy
        imgBG = new Texture("img.png");
        imgPlayer = new Texture("shtpst4.png");
        imgPlatform = new Texture("platform.jpg");
        for (int i = 0; i < imgEnemy.length; i++) {
            imgEnemy[i] = new Texture("shtpst"+i+".png");
        }
        for (int i = 0; i < imgDecoy.length; i++) {
            imgDecoy[i] = new Texture("decoy"+i+".png");
        }
        //звуки
        for (int i = 0; i < sndEnemy.length; i++) {
            sndEnemy[i] = Gdx.audio.newSound(Gdx.files.internal("snd"+i+".mp3"));
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.01f);
        sndSfx = Gdx.audio.newSound(Gdx.files.internal("bruh.mp3"));
        //задаем дефолтное имя игроку
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname");
        }
        player = new Player();
        loadTableOfRecords();
    }

    void gameStart(){
        state = PLAY_GAME;
        for (int i = 0; i < regularEnemy.length; i++) {
            regularEnemy[i] = new RegularEnemy(imgEnemy[MathUtils.random(0, 2)], imgEnemy[3]);
        }
        for (int i = 0; i < decoy.length; i++) {
            decoy[i] = new Decoy(imgDecoy[0], imgDecoy[1]);
        }
        kills = 0;
        if(g.musicOn) music.play();
        timeStart = TimeUtils.millis();
    }

    void gameOver(){
        state = SHOW_TABLE;
        players[players.length-1].time = timeFromStart;
        players[players.length-1].name = g.keyboard.getText();
        sortTable();
        saveTableOfRecords();
    }

    String timeToString(long time){
        return time/1000/60/60+":"+time/1000/60%60/10+time/1000/60%60%10+":"+time/1000%60/10+time/1000%60%10;
    }

    void sortTable(){
        for (int i = 0; i < players.length; i++) {
            if(players[i].time == 0) players[i].time = Long.MAX_VALUE;
        }
        class Cmp implements Comparator<Player>{
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.time>p2.time) return 1;
                if(p1.time<p2.time) return -1;
                return 0;
            }
        }
        Arrays.sort(players, new Cmp());
      /*boolean flag = true;
      while (flag) {
         flag = false;
         for (int i = 0; i < players.length - 1; i++) {
            if (players[i].time > players[i + 1].time) {
               flag = true;
               Player p = players[i];
               players[i] = players[i + 1];
               players[i + 1] = p;
            }
         }
      }*/
        for (int i = 0; i < players.length; i++) {
            if(players[i].time == Long.MAX_VALUE) players[i].time = 0;
        }
    }

    String showTableOfRecords(){
        String s = "  Israel black list:\n\n";
        for (int i = 0; i < players.length-1; i++) {
            s += i+1+" "+players[i].name+"......."+timeToString(players[i].time)+"\n";
        }
        return s;
    }

    void saveTableOfRecords(){
        try {
            Preferences prefs = Gdx.app.getPreferences("Table Of Records");
            for (int i = 0; i < players.length; i++) {
                prefs.putString("name" + i, players[i].name);
                prefs.putLong("time" + i, players[i].time);
            }
            prefs.flush();
        } catch (Exception ignored){
        }
    }

    void loadTableOfRecords(){
        try {
            Preferences prefs = Gdx.app.getPreferences("Table Of Records");
            for (int i = 0; i < players.length; i++) {
                players[i].name = prefs.getString("name" + i, "No info");
                players[i].time = prefs.getLong("time" + i, 0);
            }
        } catch (Exception ignored){
        }
    }

    String generateRndName(){
        String name = "";
        name += (char)MathUtils.random('A', 'Z');
        String s = "bcdfghjklmnpqrstvwxz";
        String g = "aeiouy";
        for (int i = 0; i < 3; i++) {
            name += g.charAt(MathUtils.random(g.length()-1));
            name += s.charAt(MathUtils.random(s.length()-1));
        }
        return name;
    }

    @Override
    public void show() {
        gameStart();
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()) {
            g.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            g.camera.unproject(g.touch);
            if(state == SHOW_TABLE){
                if(btnRestart.hit(g.touch.x, g.touch.y)) {
                    gameStart();
                    music.stop();
                    music.play();
                }
                if(btnBack.hit(g.touch.x, g.touch.y)) {
                    g.setScreen(g.screenIntro);
                    sndSfx.play(0.4f);
                }
            }
            if(state == ENTER_NAME) {
                if (g.keyboard.endOfEdit(g.touch.x, g.touch.y)) gameOver();
            }
            if(state == PLAY_GAME) {
                if (lifes == 0) g.setScreen(g.screenIntro);
                for (int i = regularEnemy.length - 1; i >= 0; i--) {
                    if (regularEnemy[i].isAlive && regularEnemy[i].hit(g.touch.x, g.touch.y)) {
                        kills++;
                        if(g.soundOn) sndEnemy[MathUtils.random(sndEnemy.length - 1)].play();
                        if (kills == regularEnemy.length) state = ENTER_NAME;
                        break;
                    }
                }
                for (int i = decoy.length - 1; i >= 0; i--) {
                    if(decoy[i].x == player.x || decoy[i].y == player.y) lifes --;
                    if (decoy[i].isAlive && decoy[i].hit(g.touch.x, g.touch.y)) {
                        if(g.soundOn) sndEnemy[MathUtils.random(sndEnemy.length - 1)].play();
                        lifes--;
                        break;
                    }
                }
                if(btnBack.hit(g.touch.x, g.touch.y)) {
                    g.setScreen(g.screenIntro);
                    sndSfx.play(0.4f);
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.x -= 10;
            player.isRight = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.x += 10;
            player.isRight = true;
        }

        // игровые события
        for (int i = 0; i < regularEnemy.length; i++) regularEnemy[i].move();
        for (int i = 0; i < decoy.length; i++) decoy[i].move();

        if(state == PLAY_GAME) timeFromStart = TimeUtils.millis() - timeStart;

      /*for (int i = 0; i < regularEnemy.length; i++) {
         if(!regularEnemy[i].isAlive) {
            if(MathUtils.random(1000) == 5) regularEnemy[i].reBorn();
         }
      }*/

        // отрисовка графики
        g.camera.update();
        g.batch.setProjectionMatrix(g.camera.combined);
        g.batch.begin();
        g.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        g.batch.draw(imgPlatform, 0, 0, SCR_WIDTH, 144);
        for (int i = 0; i < regularEnemy.length; i++) {
            g.batch.draw(regularEnemy[i].img, regularEnemy[i].scrX(), regularEnemy[i].scrY(), regularEnemy[i].width, regularEnemy[i].height, 0, 0, 500, 500, regularEnemy[i].isFlip(), false);
        }
        for (int i = 0; i < decoy.length; i++) {
            g.batch.draw(decoy[i].img, decoy[i].scrX(), decoy[i].scrY(), decoy[i].width, decoy[i].height, 0, 0, 678, 600, decoy[i].isFlip(), false);
        }

        g.batch.draw(imgPlayer, player.x, player.y, player.width, player.height, 0, 0, 188, 160, !player.isRight, false);

        g.font.draw(g.batch, "Cringe streak: "+kills, 10, SCR_HEIGHT-10);
        g.font.draw(g.batch, "lifes: " + lifes, SCR_WIDTH / 2 + 60, SCR_HEIGHT - 10);
        g.font.draw(g.batch, timeToString(timeFromStart), SCR_WIDTH-300, SCR_HEIGHT-10);
        if(state == PLAY_GAME) {
            g.font.draw(g.batch, btnBack.text, btnBack.x, btnBack.y);
        }
        if(state == SHOW_TABLE){
            g.font.draw(g.batch, showTableOfRecords(), SCR_WIDTH/4f, SCR_HEIGHT/4f*3);
            g.font.draw(g.batch, btnRestart.text, btnRestart.x, btnRestart.y);
            g.font.draw(g.batch, btnBack.text, btnBack.x, btnBack.y);
        }
        if(state == ENTER_NAME){
            g.keyboard.draw(g.batch);
        }
        if (state == SHOW_TABLE || state == ENTER_NAME) {
            sndSfx.stop();
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
        music.stop();
        sndSfx.stop();
    }

    @Override
    public void dispose () {
        for (int i = 0; i < imgEnemy.length; i++) {
            imgEnemy[i].dispose();
        }
        imgBG.dispose();
        for (int i = 0; i < sndEnemy.length; i++) {
            imgEnemy[i].dispose();
        }
        music.dispose();
        sndSfx.dispose();
        g.keyboard.dispose();
        imgPlatform.dispose();
        imgPlayer.dispose();
        imgBG.dispose();
    }
}


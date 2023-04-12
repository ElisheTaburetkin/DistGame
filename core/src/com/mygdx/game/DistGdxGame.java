package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class DistGdxGame extends Game {
	public static final int SCR_WIDTH = 1280, SCR_HEIGHT = 720;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;
	InputKeyboard keyboard;

	ScreenIntro screenIntro;
	ScreenGame screenGame;
	ScreenSettings screenSettings;
	ScreenAbout screenAbout;

	boolean soundOn = true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH,SCR_HEIGHT);
		touch = new Vector3();
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
		fontLarge = generator.generateFont(parameter);
		generator.dispose();
	}
/*
	@Override
	public void render () {
		//обработка касаний
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			if(gameOver){
				gameRestart();
			}
			for (int i = mosq.length-1; i >= 0; i--) {
				if(mosq[i].isAlive && mosq[i].hit(touch.x, touch.y)){
					kills++;
					sndMosq[MathUtils.random(sndMosq.length-1)].play();
					if (kills == mosq.length) gameOver();
					break;
				}
			}
		}
		for (Mosquito value : mosq) value.move();

		//игровые события
		for (int i = 0; i < mosq.length; i++) mosq[i].move();

		if(!gameOver) timeFromStart = TimeUtils.millis() - timeStart;

		timeFromStart = TimeUtils.millis() - timeStart;

		//отрисовка графики
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < mosq.length; i++) {
			batch.draw(imgMosq[mosq[i].phase], mosq[i].scrX(), mosq[i].scrY(), mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
		}
		font.draw(batch, "Cringe streak: "+"x"+kills, 10, SCR_HEIGHT-10);
		font.draw(batch, timeToString(timeFromStart), SCR_WIDTH-250, SCR_HEIGHT-10);
		if(gameOver){
			font.draw(batch, showTableOfRecords(), SCR_WIDTH/4f, SCR_HEIGHT/4f*3);
		}
		batch.end();
	}
	void gameRestart(){
		gameOver = false;
		for (int i = 0; i < mosq.length; i++) {
			mosq[i] = new Mosquito();
		}
		kills = 0;
		timeStart = TimeUtils.millis();
	}

	void gameOver() {
		gameOver = true;
		players[players.length-1].time = timeFromStart;
		players[players.length-1].name = generateRndName();
		sortTable();

	}

	String timeToString(long time){
		return time/1000/60/60+":"+time/1000/60%60/10+time/1000/60%60%10+":"+time/1000%60/10+time/1000%60%10;
	}
	void sortTable(){
		for (int i = 0; i < players.length; i++) {
			if(players[i].time == 0) players[i].time = Long.MAX_VALUE;
		}
		boolean flag = true;
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
		}
		for (int i = 0; i < players.length; i++) {
			if(players[i].time == Long.MAX_VALUE) players[i].time = 0;
		}
	}

	String showTableOfRecords() {
		String s = " Black list of Israel:\n\n";
		for (int i = 0; i < players.length - 1; i++) {
			s += i + 1 + " " + players[i].name + "....." + timeToString(players[i].time) + "\n";
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
*/
	@Override
	public void dispose () {
		batch.dispose();
		/*for (int i = 0; i < imgMosq.length; i++) {
			imgMosq[i].dispose();
		}
		imgBG.dispose();
		for (int i = 0; i < sndMosq.length; i++) {
			imgMosq[i].dispose();
		}*/
		font.dispose();
	}
}
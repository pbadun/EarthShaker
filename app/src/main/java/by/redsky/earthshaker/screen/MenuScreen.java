package by.redsky.earthshaker.screen;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import by.redsky.earthshaker.Assets;
import by.redsky.earthshaker.IGameObjectsPix;
import by.redsky.earthshaker.Settings;
import by.redsky.earthshaker.screen.draw.DrawMap;
import by.redsky.framework.interfaces.Game;
import by.redsky.framework.interfaces.Graphics;
import by.redsky.framework.interfaces.Screen;
import by.redsky.framework.interfaces.Input.TouchEvent;

/**
 * Старовое меню
 * 
 * @author Jb
 *
 */
public class MenuScreen extends Screen implements IGameObjectsPix {
	enum state {
		ST_WAIT, ST_PLAY, ST_HIGHSCORES, ST_HELP, ST_EXIT
	};

	int width2;
	int width;
	DrawMap dMap;
	int maxWidth;
	int map[][];
	state st;
	float time;

	public MenuScreen(Game game) {
		super(game);
		this.width2 = (game.getGraphics().getWidth() - 477) / 2;
		this.width = game.getGraphics().getWidth();
		this.maxWidth = width / 64;
		this.dMap = new DrawMap(game.getGraphics(), width);
		this.map = new int[maxWidth][11];
		random();
		st = state.ST_WAIT;
		this.time = 0;
		// Assets.menuMusic.setLooping(true);
		Assets.menuMusic.play();
		game.getGraphics().clear(0xFF000000);
	}

	/**
	 * Герерируем рандомную карту по размеру экрана, не больше )
	 */
	private void random() {
		Random rand = new Random();
		int[] a = { ES_ZEMLIY, ES_ALMAZ, ES_ZEMLIY, ES_KAMEN, ES_ZEMLIY };

		for (int y = 0; y < 11; y++) {
			for (int x = 0; x < maxWidth; x++) {
				map[x][y] = a[rand.nextInt(a.length)];
				if (x == 0 || x == maxWidth - 1 || y == 10) {
					map[x][y] = ES_STENA;
				}
			}
		}
		map[4][2] = ES_HIRO;
	}

	@Override
	public void update(float deltaTime) {
		// Обновление анимации
		dMap.upadateAnimation(deltaTime);
		if (st != state.ST_WAIT) {
			time += deltaTime;
			if (time > 1.2f) {
				if (st == state.ST_PLAY) {
					
					game.setScreen(new GameScreen(game));
				}
				if (st == state.ST_HIGHSCORES) {
					game.setScreen(new HighscoresScreen(game));
				}
				if (st == state.ST_HELP) {
					game.setScreen(new HelpScreen(game));
				}
				if (st == state.ST_EXIT) {
					((Activity) game).finish();
				}
			}
		}
		
		game.isBackKey();
		game.isMenuKey();
		game.getInput().getKeyEvents(); // кнопки устройства
		List<TouchEvent> tEvent = game.getInput().getTouchEvents();
		if (st == state.ST_WAIT) {
			getKeyMenu(tEvent);
		}
	}

	@Override
	public boolean present(float deltaTime) {
		if (dMap.isUpdate) {
			Graphics g = game.getGraphics();
			drawMap();
			g.drawPixmap(Assets.menuKey, width2, 72, 0, 116 * 3, 477, 116); // play
			g.drawPixmap(Assets.menuKey, width2, 220, 0, 116 * 4, 477, 116); // Рекорды
			g.drawPixmap(Assets.menuKey, width2, 368, 0, 116 * 5, 477, 116); // Справка
			g.drawPixmap(Assets.menuKey, width2, 516, 0, 116 * 6, 477, 116); // Выход
			return true;
		}

		return false;
	}

	/**
	 * Ловим нажатия клавишь.
	 * 
	 * @param tEvent
	 */
	private void getKeyMenu(List<TouchEvent> tEvent) {
		int len = tEvent.size();
		boolean play = false;
		for (int i = 0; i < len; i++) {
			TouchEvent event = tEvent.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > width2 && event.x < width2 + 477) {
					if (event.y > 72 && event.y < 188) {
						play = true;
						st = state.ST_PLAY;
					}
					if (event.y > 220 && event.y < 336) {
						play = true;
						st = state.ST_HIGHSCORES;
					}
					if (event.y > 368 && event.y < 484) {
						play = true;
						st = state.ST_HELP;
					}
					if (event.y > 516 && event.y < 632) {
						play = true;
						st = state.ST_EXIT;
					}
				}
			}
		}
		if (play && Settings.soundEnable) {
			Assets.menuMusic.stop();
			Assets.select.play(1);
		}
	}

	/**
	 * Выводим карту
	 */
	private void drawMap() {
		for (int y = 0; y < 11; y++) {
			for (int x = 0; x < maxWidth; x++) {
				dMap.drawPix(x, y, map[x][y]);
			}
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Assets.menuMusic.stop();
	}
}

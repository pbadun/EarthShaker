package by.redsky.earthshaker.screen;

import java.util.List;
import by.redsky.earthshaker.Assets;
import by.redsky.earthshaker.Settings;
import by.redsky.earthshaker.games.World;
import by.redsky.earthshaker.games.livel.ILivels;
import by.redsky.earthshaker.games.livel.Livel1;
import by.redsky.earthshaker.games.livel.Livel2;
import by.redsky.earthshaker.games.livel.Livel3;
import by.redsky.earthshaker.games.livel.Livel4;
import by.redsky.earthshaker.screen.draw.DrawMap;
import by.redsky.earthshaker.screen.draw.DrawMenu;
import by.redsky.framework.impl.AndroidGame;
import by.redsky.framework.interfaces.Game;
import by.redsky.framework.interfaces.Graphics;
import by.redsky.framework.interfaces.Input.TouchEvent;
import by.redsky.framework.interfaces.Screen;

/**
 * Класс игрового процеса.
 * 
 * @author Jb
 *
 */
public class GameScreen extends Screen {

	// Состояния игры
	enum GameState {
		Ready, Paysa, Play, GameOver, VaitNextLeve, HeroDied
	}

	// --------------------------------------------------------
	int width; // ширина экрана
	GameState gameState;

	World world; // игровой мир

	DrawMap drawMap;
	DrawMenu drawMenu;

	int life;
	int level;
	// --------------------------------------------------------
	float startWaitTime;
	final float START_WAIT_TIME = 5.0f;

	// --------------------------------------------------------

	/**
	 * 
	 * @param game
	 */
	public GameScreen(Game game) {
		super(game);

		this.life = 5;
		this.width = ((AndroidGame) game).getFrameBufferWidth();

		this.gameState = GameState.Ready;
		this.startWaitTime = 0;

		this.drawMap = new DrawMap(game.getGraphics(), width);
		this.drawMenu = new DrawMenu(game.getGraphics(), width);

		this.world = new World(width / 64, 10);
		this.level = 1;

		nextLevel();
	}

	/**
	 * Новый уровень.
	 */
	void nextLevel() {
		ILivels l = null;
		switch (level) {
		case 1:
			l = new Livel1();
			break;
		case 2:
			l = new Livel2();
			break;
		case 3:
			l = new Livel3();
			break;
		case 4:
			l = new Livel4();
			break;
		}

		world.setLevel(l, level);
		gameState = GameState.Ready;
		this.startWaitTime = 0;
		Assets.startMusic.play();
	}

	// --------------------------------------------------------------
	@Override
	public void update(float deltaTime) {
		// --------------------------------------------------------
		// Обновление анимации - всегда )
		drawMap.upadateAnimation(deltaTime);

		if (gameState == GameState.Play) {
			game.setAdModVisibility(false);
		} else {
			game.setAdModVisibility(true);
		}

		// --------------------------------------------------------
		// Ловик кнопки управления игрой...
		game.getInput().getKeyEvents(); // кнопки

		List<TouchEvent> touchEvent = game.getInput().getTouchEvents();
		// Обрабатываем мир только в режиме игры
		if (gameState == GameState.Play) {
			key(touchEvent);
			if (game.isBackKey() || game.isMenuKey()) {
				gameState = GameState.Paysa;
			}
			// Обновление мира
			if (world.update(deltaTime)) {
				musikPlay();
				// Если конец игры
				if (world.gameOver) {
					gameState = GameState.HeroDied;
					if (life == 0) {
						gameState = GameState.GameOver;
						// Все жизни закончились, складываем все очки
						world.scoredTotal += world.scoredGame;
					}
					life--;
				}
				if (world.gameOk) {
					gameState = GameState.VaitNextLeve;
					level++;
				}
			}
		}
		// Нажатие клавиши
		if (gameState == GameState.Ready && presTouch(touchEvent)
				&& startWaitTime > START_WAIT_TIME) {
			gameState = GameState.Play;
			Assets.startMusic.stop();
		}
		// ----------------
		// Тут меню паузы.
		if (gameState == GameState.Paysa) {
			pauseMenu(touchEvent);
		}
		// -----------------
		if (gameState == GameState.HeroDied && presTouch(touchEvent)) {
			nextLevel();
		}
		// ----------------
		if (gameState == GameState.GameOver && presTouch(touchEvent)) {
			// тут бы выгодить в главное меню....
			Settings.addScore(world.scoredTotal);
			game.setAdModVisibility(false);
			game.setScreen(new MenuScreen(game));
		}
		// ----------------
		if (gameState == GameState.VaitNextLeve && presTouch(touchEvent)) {
			nextLevel();
		}

		// Сбрасываем положение кнопок
		game.isBackKey();
		game.isMenuKey();
	}

	/**
	 * Обработка меню ожидания/паузы.
	 * 
	 * @param tEvent
	 */
	private void pauseMenu(List<TouchEvent> tEvent) {
		try {
			int len = tEvent.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = tEvent.get(i);
				if (event.x > width / 2 - 238 && event.x < width / 2 + 238) {
					if (event.y > 146 && event.y < 262) {
						// Continue
						gameState = GameState.Play;
					}
					if (event.y > 294 && event.y < 410) {
						// Restart
						nextLevel();
					}
					if (event.y > 442 && event.y < 558) {
						// Main menu
						game.setAdModVisibility(false);
						game.setScreen(new MenuScreen(game));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Звуки игры
	 */
	private void musikPlay() {
		if (Settings.soundEnable) {
			if (world.map.isDown()) {
				Assets.padenia.play(1);
			}
			if (world.map.isDownEnd()) {
				Assets.upal.play(1);
			}
			if (world.snZemlia) {
				Assets.zemlia.play(1);
			}
			if (world.snGameDie) {
				Assets.gameDie.play(1);
			}
			if (world.snAlmazOk) {
				Assets.vseAlmazi.play(1);
			}
			if (world.snStep) {
				Assets.step.play(1);
			}
			if (world.snAlmaz) {
				Assets.getAlmaz.play(1);
			}
			if (world.gameOk) {
				Assets.gameOk.play(1);
			}
		}
	}

	/**
	 * Нажатие к приделах окна информации..
	 * 
	 * @param tEvent
	 * @return
	 */
	private boolean presTouch(List<TouchEvent> tEvent) {
		int len = tEvent.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = tEvent.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > (width / 2 - 358) && event.x < (width / 2 + 358)
						&& event.y > 236 && event.y < 468) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Поиск нажатия кнопок
	 * 
	 * @param tEvent
	 */
	private void key(List<TouchEvent> tEvent) {
		// Сбрасывае напровление, нельзя хранить - иначе будем перебегать...
		world.hiro.setDirection();
		int len = tEvent.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = tEvent.get(i);
			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				if (event.x > 20 && event.x < 148) {
					if (event.y > 396 && event.y < 524) {
						world.hiro.stepUP();
					}
					if (event.y > 556 && event.y < 684) {
						world.hiro.stepDoun();
					}
				}
				if (event.y > 556 && event.y < 684) {
					if (event.x > width - 308 && event.x < width - 180) {
						world.hiro.stepLeft();
					}
					if (event.x > width - 148 && event.x < width - 20) {
						world.hiro.stepRight();
					}
				}
			}
		}
	}

	// --------------------------------------------------------------
	/**
	 * Выводим на экран данные...
	 */
	@Override
	public boolean present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.clear(0xFF000000);
		// if (drawMap.isUpdate) {
		drawWorld();
		if (gameState != GameState.Play) {

			if (gameState == GameState.Ready) {
				if (startWaitTime > START_WAIT_TIME) {
					drawMenu.drawIsReady(true, world.level);
				} else {
					drawMenu.drawIsReady(false, world.level);
					startWaitTime += deltaTime;
				}
			}
			if (gameState == GameState.Paysa) {
				drawMenu.drawPauseMenu();
			}
			if (gameState == GameState.GameOver) {
				drawMenu.drawGameOver(world.scoredTotal, world.level);
			}
			if (gameState == GameState.HeroDied) {
				drawMenu.drawRestart(life);
			}
			if (gameState == GameState.VaitNextLeve) {
				drawMenu.drawNext(world.scoredTotal, world.level);
			}
		} else {
			drawMenu.drawStatusBar(world.level, world.scored,
					world.l.totalAlmaz(), life, world.time, world.scoredTotal
							+ world.scoredGame);
			drawMenu.drawKey();
		}
		return true;
		// }
		// return false;
	}

	// Выводим мир на экран. только видимую его часть.
	private void drawWorld() {
		for (int y = 0; y < world.screenY; y++) {
			for (int x = 0; x < world.screenX; x++) {
				drawMap.drawPix(x, y, world.getPix(x, y));
			}
		}
	}

	// --------------------------------------------------------------
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}

package by.redsky.earthshaker.screen;

import by.redsky.earthshaker.Assets;
import by.redsky.earthshaker.Settings;
import by.redsky.framework.interfaces.Game;
import by.redsky.framework.interfaces.Graphics;
import by.redsky.framework.interfaces.Screen;
import by.redsky.framework.interfaces.Graphics.PixmapFormat;

/**
 * Загрузка art и музыки
 * 
 * @author Jb
 *
 */
public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);
		Graphics g = game.getGraphics();
		Assets.art = g.newPixmap("art.png", PixmapFormat.RGB565);
		Assets.joistic = g.newPixmap("joistic.png", PixmapFormat.RGB565);

		Assets.cifri = g.newPixmap("cifri.png", PixmapFormat.RGB565);
		Assets.cifri2 = g.newPixmap("cifri2.png", PixmapFormat.RGB565);
		Assets.gameover = g.newPixmap("gameover.png", PixmapFormat.RGB565);
		Assets.next_level = g.newPixmap("nextlevel.png", PixmapFormat.RGB565);
		Assets.time_bar = g.newPixmap("time_bar.png", PixmapFormat.RGB565);
		Assets.please_touch_to_start_level = g.newPixmap(
				"please_touch_to_start_level.png", PixmapFormat.RGB565);
		Assets.please_touch_to_continue = g.newPixmap(
				"please_touch_to_continue.png", PixmapFormat.RGB565);
		// Assets.pause = g.newPixmap("pause.png", PixmapFormat.RGB565);
		Assets.statusBar = g.newPixmap("status_bar.png", PixmapFormat.RGB565);
		Assets.menuKey = g.newPixmap("menu_key.png", PixmapFormat.RGB565);
		Assets.bg = g.newPixmap("bg.png", PixmapFormat.RGB565);
		Assets.highscores = g.newPixmap("highscores.png", PixmapFormat.RGB565);
		Assets.restartLivel = g.newPixmap("restart.png", PixmapFormat.RGB565);
		Assets.help = g.newPixmap("help.png", PixmapFormat.RGB565);

		Assets.zemlia = game.getAudio().newSound("zemlia.wav");
		Assets.padenia = game.getAudio().newSound("padenie.wav");
		Assets.upal = game.getAudio().newSound("upal.wav");
		Assets.select = game.getAudio().newSound("select.wav");
		Assets.gameDie = game.getAudio().newSound("game_die.wav");
		Assets.gameOk = game.getAudio().newSound("game_ok.wav");
		Assets.getAlmaz = game.getAudio().newSound("getalmaz.wav");
		Assets.vseAlmazi = game.getAudio().newSound("vse_almazi.wav");
		Assets.step = game.getAudio().newSound("step.wav");

		Assets.menuMusic = game.getAudio().newMusic("music_menu.wav");
		Assets.startMusic = game.getAudio().newMusic("play_music.wav");

		Settings.load(game.getFileIO());
	}

	@Override
	public void update(float deltaTime) {
		game.setScreen(new MenuScreen(game));// new GameScreen(game));
		// game.setScreen(new GameScreen(game));
	}

	@Override
	public boolean present(float deltaTime) {
		return false;
	}

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

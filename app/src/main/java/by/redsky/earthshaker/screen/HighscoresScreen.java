package by.redsky.earthshaker.screen;

import java.util.List;
import by.redsky.earthshaker.Assets;
import by.redsky.earthshaker.Settings;
import by.redsky.framework.interfaces.Game;
import by.redsky.framework.interfaces.Graphics;
import by.redsky.framework.interfaces.Screen;
import by.redsky.framework.interfaces.Input.TouchEvent;

/**
 * Скрин рекордов. Рекорды храним в Settings
 * 
 * @author Jb
 *
 */
public class HighscoresScreen extends Screen {
	int srcLeft;
	int srcPres;
	int srcRec;

	String lines[] = new String[10];

	public HighscoresScreen(Game game) {
		super(game);
		this.srcLeft = (game.getFrameBufferWidth() - 477) / 2;
		this.srcPres = (game.getFrameBufferWidth() - 591) / 2;
		this.srcRec = (game.getFrameBufferWidth() - 304) / 2;

		for (int i = 0; i < 10; i++) {
			lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
		}
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public boolean present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.bg, 0, 0);
		g.drawPixmap(Assets.highscores, srcLeft, 89);
		g.drawPixmap(Assets.please_touch_to_continue, srcPres, 637);

		drawHighscores(g);

		game.getInput().getKeyEvents(); // кнопки устройства
		List<TouchEvent> touchEvent = game.getInput().getTouchEvents();
		if (presTouch(touchEvent)) {
			game.setScreen(new MenuScreen(game));
		}
		return true;
	}

	private void drawHighscores(Graphics g) {
		int y = 141;
		for (int i = 0; i < 10; i++) {
			drawText(g, lines[i], srcRec, y);
			y += 44;
		}

	}

	/**
	 * Вывод текстовых строк
	 * @param g
	 * @param line
	 * @param x
	 * @param y
	 */
	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			if (character == ' ') {
				x += 20;
				continue;
			}
			int srcX = 0;
			if (character == '.') {
				srcX = 310;
			} else {
				srcX = (character - '0') * 31;
			}
			g.drawPixmap(Assets.cifri2, x, y, srcX, 0, 31, 34);
			x += 31;
		}
	}

	/**
	 * Ждем нажатия на экран
	 * @param tEvent
	 * @return
	 */
	private boolean presTouch(List<TouchEvent> tEvent) {
		int len = tEvent.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = tEvent.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				return true;
			}
		}
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

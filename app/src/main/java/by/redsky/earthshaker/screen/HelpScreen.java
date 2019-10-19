package by.redsky.earthshaker.screen;

import java.util.List;

import by.redsky.earthshaker.Assets;
import by.redsky.framework.interfaces.Game;
import by.redsky.framework.interfaces.Graphics;
import by.redsky.framework.interfaces.Screen;
import by.redsky.framework.interfaces.Input.TouchEvent;

/**
 * Справка. Как играть...
 * 
 * @author Jb
 *
 */
public class HelpScreen extends Screen {

	int width;
	int screenId;
	int screenIdTm;

	public HelpScreen(Game game) {
		super(game);
		this.width = game.getFrameBufferWidth();
		this.screenId = 0;
	}

	@Override
	public void update(float deltaTime) {
		if (game.isBackKey() || game.isMenuKey()) {
			game.setScreen(new MenuScreen(game));
		}

		game.getInput().getKeyEvents(); // кнопки
		List<TouchEvent> touchEvent = game.getInput().getTouchEvents();
		int len = touchEvent.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvent.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.y > 556 && event.y < 684) {
					if (event.x > 20 && event.x < 148) {
						screenIdTm = screenId;
						screenId = (screenId - 1) <= 0 ? 0 : (screenId - 1);
						if (screenIdTm != screenId) {
							Assets.zemlia.play(1);
						}
					} else if (event.x > width - 148 && event.x < width - 20) {
						screenIdTm = screenId;
						screenId = screenId >= 8 ? 8 : (screenId + 1);
						if (screenIdTm != screenId) {
							Assets.zemlia.play(1);
						}
					} else if (event.x > width / 2 - 64
							&& event.x < width / 2 + 64) {
						game.setScreen(new MenuScreen(game));
					}
				}
			}
		}

	}

	@Override
	public boolean present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.bg, 0, 0);
		g.drawRect(0, 0, game.getFrameBufferWidth() + 1,
				game.getFrameBufferHeight(), 0x80000000);

		int xS = screenId % 2 == 0 ? 0 : 1;
		int yS = screenId / 2;

		g.drawPixmap(Assets.help, width / 2 - 236, 176, 473 * xS, 352 * yS,
				473, 352);

		if (screenId < 8) {
			g.drawPixmap(Assets.joistic, width - 148, 556, 129, 0, 128, 128); // RIGHT

		}
		if (screenId > 0) {
			g.drawPixmap(Assets.joistic, 20, 556, 129, 129, 128, 128); // LEFT
		}
		g.drawPixmap(Assets.joistic, width / 2 - 64, 556, 0, 257, 128, 128); // back
		return true;
	}

	// ----------------------------------------------------
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

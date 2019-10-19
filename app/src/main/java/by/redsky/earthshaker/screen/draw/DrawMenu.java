package by.redsky.earthshaker.screen.draw;

import by.redsky.earthshaker.Assets;
import by.redsky.framework.interfaces.Graphics;

/**
 * ����� ��������� ������ ���� � ����.
 * 
 * @author Jb
 *
 */
public class DrawMenu {
	Graphics g;
	int width;

	/**
	 * �����������
	 * 
	 * @param g
	 * @param width
	 */
	public DrawMenu(Graphics g, int width) {
		this.g = g;
		this.width = width;
	}

	/**
	 * ���������� ����������� ������
	 * 
	 * @param isTach
	 *            - �������� ��������� � ������������� ���������
	 */
	public void drawIsReady(boolean isTach, int level) {
		g.drawPixmap(Assets.please_touch_to_start_level, width / 2 - 358, 236);
		drawText((level < 10 ? "0" : "") + Integer.toString(level),
				width / 2 + 74, 327);
		if (isTach) {
			g.drawPixmap(Assets.please_touch_to_continue, (width - 591) / 2,
					637);
		}
	}

	/**
	 * ���� �����/������ � ������� ����
	 */
	public void drawPauseMenu() {
		g.drawPixmap(Assets.menuKey, width / 2 - 238, 146, 0, 0, 477, 116);
		g.drawPixmap(Assets.menuKey, width / 2 - 238, 294, 0, 116, 477, 116);
		g.drawPixmap(Assets.menuKey, width / 2 - 238, 442, 0, 232, 477, 116);
	}

	/**
	 * ������� ����� ����.
	 * 
	 * @param score
	 * @param level
	 */
	public void drawGameOver(int score, int level) {
		g.drawPixmap(Assets.gameover, width / 2 - 358, 236);
		drawText(Integer.toString(score), width / 2 + 54, 336);
		drawText(Integer.toString(level), width / 2 + 254, 400);
		g.drawPixmap(Assets.please_touch_to_continue, (width - 591) / 2, 637);
	}

	/**
	 * ����� ������ ����� �� ���� ���� ��� �����.
	 * 
	 * @param life
	 */
	public void drawRestart(int life) {
		g.drawPixmap(Assets.restartLivel, width / 2 - 358, 236);
		drawText(Integer.toString(life), width / 2 + 200, 336);
		g.drawPixmap(Assets.please_touch_to_continue, (width - 591) / 2, 637);
	}

	/**
	 * ������� ����� ����.
	 * 
	 * @param score
	 * @param level
	 */
	public void drawNext(int score, int level) {
		g.drawPixmap(Assets.next_level, width / 2 - 358, 236);
		drawText(Integer.toString(score), width / 2 + 54, 336);
		drawText(Integer.toString(level), width / 2 + 254, 400);
		g.drawPixmap(Assets.please_touch_to_continue, (width - 591) / 2, 637);
	}

	/**
	 * ������� ������ ���.
	 * 
	 * @param live
	 *            - �������
	 * @param score
	 *            - ����� �������
	 * @param scoreTotal
	 *            - ����� ������� ���� �������
	 * @param life
	 *            - ����� ������ ��������
	 * @param timeBar
	 *            - ����� ������� ��������
	 * @param totalScored
	 *            - ����� ������� ����� �������
	 */
	public void drawStatusBar(int live, int score, int scoreTotal, int life,
			float timeBar, int totalScored) {
		g.drawPixmap(Assets.statusBar, width / 2 - 256, 640);

		int stBar = (int) ((500 / 120.0f) * timeBar);

		g.drawPixmap(Assets.time_bar, width / 2 - 256, 640, 0, 0, stBar, 32);
		String tt = "  " + (live < 10 ? ("0" + live) : (live)) + "  "
				+ (scoreTotal < 10 ? ("0" + scoreTotal) : (scoreTotal)) + " "
				+ (score < 10 ? ("0" + score) : (score)) + "  "
				+ (life < 10 ? ("0" + life) : (life));
		drawText(tt, width / 2 - 256, 672);
		totalScored(totalScored);
	}

	/**
	 * ������� ���� ����� - �� ������ � �����.
	 * 
	 * @param tScored
	 */
	private void totalScored(int tScored) {
		String tt = Integer.toString(tScored);
		drawText(tt, width / 2 - (tt.length() / 2) * 32, 16);
	}

	/**
	 * ����� �����
	 * 
	 * @param line
	 *            - ������ ���� � �������
	 * @param x
	 *            - ���������� � ������
	 * @param y
	 *            - y���������� � ������
	 */
	private void drawText(String line, int x, int y) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			if (character == ' ') {
				x += 32;
				continue;
			}
			int srcX = 0;
			if (character == '.') {
				srcX = 310;
			} else {
				srcX = (character - '0') * 32;
			}
			g.drawPixmap(Assets.cifri, x, y, srcX, 0, 32, 32);
			x += 31;
		}
	}

	/**
	 * ������� ������ ����������
	 */
	public void drawKey() {
		g.drawPixmap(Assets.joistic, 20, 396, 0, 0, 128, 128);// UP
		g.drawPixmap(Assets.joistic, 20, 556, 0, 129, 128, 128); // DOWN
		g.drawPixmap(Assets.joistic, width - 148, 556, 129, 0, 128, 128); // RIGHT
		g.drawPixmap(Assets.joistic, width - 308, 556, 129, 129, 128, 128); // LEFT
	}

}

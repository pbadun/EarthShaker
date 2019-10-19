package by.redsky.earthshaker.screen.draw;

import by.redsky.earthshaker.Assets;
import by.redsky.earthshaker.IGameObjectsPix;
import by.redsky.framework.interfaces.Graphics;

/**
 * Отрисовка элементов карты
 * 
 * @author Jb
 *
 */
public class DrawMap implements IGameObjectsPix {
	Graphics g;

	int shiftX; // сдвиг от начала координат

	int stepAnime; // Шаг анимации
	float stepTime; // Время анимации
	static float STEP_TEME = 0.15f; // Промежуток времени

	public boolean isUpdate = false;

	public DrawMap(Graphics g, int width) {
		int s = width / 64;
		this.shiftX = (width - s * 64) / 2;

		this.g = g;
		this.stepAnime = 0;
		this.stepTime = 0;
	}

	// -------------------------------------------------
	/**
	 * Обновление шага анимации
	 * 
	 * @param deltaTime
	 */
	public void upadateAnimation(float deltaTime) {
		stepTime += deltaTime;
		if (stepTime > STEP_TEME) {
			stepTime -= STEP_TEME;
			stepAnime = (stepAnime + 1) >= 4 ? 0 : (stepAnime + 1);
			isUpdate = true;
		} else {
			isUpdate = false;
		}
	}

	// -------------------------------------------------
	/**
	 * Выводим элемента карты
	 * 
	 * @param x
	 * @param y
	 * @param pix
	 */
	public void drawPix(int x, int y, int pix) {
		int srcY = 0;
		int srcX = 0;
		switch (pix) {
		case ES_NULL:
			srcY = 0;
			srcX = 0;
			break;
		case ES_ZEMLIY:
			srcY = 0;
			srcX = 64;
			break;
		case ES_STENA:
			srcY = 0;
			srcX = 128;
			break;
		case ES_KAMEN:
			srcY = 0;
			srcX = 192;
			break;
		case ES_KAMEN_FLY:
			srcY = 0;
			srcX = 192;
			break;
		case ES_HIRO:
			srcY = 128;
			srcX = stepAnime * 64;
			break;
		case ES_OGON:
			srcY = 192;
			srcX = stepAnime * 64;
			break;
		case ES_VADA:
			srcY = 256;
			srcX = stepAnime * 64;
			break;
		case ES_TIME:
			srcY = 320;
			srcX = stepAnime * 64;
			break;
		case ES_ALMAZ:
			srcY = 384;
			srcX = stepAnime * 64;
			break;
		case ES_PORTAL_ACTIVE:
			srcY = 448;
			srcX = stepAnime * 64;
			break;
		case ES_PORTAL_PASIVE:
			srcY = 0;
			srcX = 256;
			break;
		case ES_HIRO_DIA:
			srcY = 0;
			srcX = 320;
			break;
		case ES_KAMEN_GORIT1:
			srcY = 128;
			srcX = 256;
			break;
		case ES_KAMEN_GORIT2:
			srcY = 128;
			srcX = 256 + 64;
			break;
		case ES_KAMEN_GORIT3:
			srcY = 128;
			srcX = 256 + 64 + 64;
			break;
		case ES_KAMEN_GORIT4:
			srcY = 128;
			srcX = 256 + 64 + 64 + 64;
			break;
		case ES_KAMEN_GORIT5:
			srcY = 128;
			srcX = 256 + 64 + 64 + 64 + 64;
			break;
		case ES_KAMEN_GORIT6:
			srcY = 128;
			srcX = 256 + 64 + 64 + 64 + 64 + 64;
			break;

		}
		g.drawPixmap(Assets.art, x * 64 + shiftX, y * 64, srcX, srcY, 65, 65);
	}

}

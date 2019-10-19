package by.redsky.earthshaker.games;

import android.annotation.SuppressLint;

/**
 * Обьект гироя. упровляем позициями
 * 
 * @author Jb
 *
 */
@SuppressLint("RtlHardcoded")
public class Hiro {
	public enum stepHiro {
		UP, DOWN, LEFT, RIGHT
	};

	public int positionX;
	public int positionY;

	public int positionXback;
	public int positionYback;

	int maxWidth;
	int maxHeidth;

	boolean keyA; // кнопка А - стоять на месте
	boolean keyB; // кнопка B - стоять на месте
	stepHiro directionKey; // Напровление движения
	public stepHiro step; // последнее напровление движения гироя...

	/**
	 * Конструктор, установка начальных значений.
	 * 
	 * @param maxWidth
	 *            - макс ширина игрового поля
	 * @param maxHeidth
	 *            - макс висотаполя
	 * @param positionX
	 *            - текущая позиция по X
	 * @param positionY
	 *            - текущая позиция по Y
	 */
	public Hiro(int maxWidth, int maxHeidth, int positionX, int positionY) {
		this.maxHeidth = maxHeidth;
		this.maxWidth = maxWidth;
		this.positionX = positionX;
		this.positionY = positionY;
		this.positionXback = positionX;
		this.positionYback = positionY;
		this.directionKey = null;
	}

	/**
	 * Делаем шаг
	 */
	public void update() {
		if (directionKey == null) {
			return;
		}

		switch (directionKey) {
		case UP:
			if (positionY > 0) {
				positionY--;
			}
			break;
		case DOWN:
			if (positionY < maxHeidth - 1) {
				positionY++;
			}
			break;
		case LEFT:
			if (positionX > 0) {
				positionX--;
			}
			break;
		case RIGHT:
			if (positionX < maxWidth - 1) {
				positionX++;
			}
			break;
		}
		step = directionKey;
		directionKey = null;
	}

	/**
	 * Сбрасываем нажатую кнопку..
	 */
	public void setDirection() {
		directionKey = null;
	}

	/**
	 * Откат в случае неудачи.
	 */
	public void hiroBreak() {
		positionX = positionXback;
		positionY = positionYback;
	}

	/**
	 * Фиксируем позицию гироя.
	 */
	public void hiroFix() {
		positionXback = positionX;
		positionYback = positionY;
	}

	/**
	 * Шаг в верх
	 */
	public void stepUP() {
		directionKey = stepHiro.UP;
	}

	/**
	 * Шаг в низ
	 */
	public void stepDoun() {
		directionKey = stepHiro.DOWN;
	}

	/**
	 * Шаг в лева
	 */
	public void stepLeft() {
		directionKey = stepHiro.LEFT;
	}

	/**
	 * Шаг в право
	 */
	public void stepRight() {
		directionKey = stepHiro.RIGHT;
	}

	public void setKeyA() {
		this.keyA = true;
	}

	public void setKeyB() {
		this.keyB = true;
	}
}

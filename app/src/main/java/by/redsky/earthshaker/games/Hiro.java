package by.redsky.earthshaker.games;

import android.annotation.SuppressLint;

/**
 * ������ �����. ��������� ���������
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

	boolean keyA; // ������ � - ������ �� �����
	boolean keyB; // ������ B - ������ �� �����
	stepHiro directionKey; // ����������� ��������
	public stepHiro step; // ��������� ����������� �������� �����...

	/**
	 * �����������, ��������� ��������� ��������.
	 * 
	 * @param maxWidth
	 *            - ���� ������ �������� ����
	 * @param maxHeidth
	 *            - ���� ����������
	 * @param positionX
	 *            - ������� ������� �� X
	 * @param positionY
	 *            - ������� ������� �� Y
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
	 * ������ ���
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
	 * ���������� ������� ������..
	 */
	public void setDirection() {
		directionKey = null;
	}

	/**
	 * ����� � ������ �������.
	 */
	public void hiroBreak() {
		positionX = positionXback;
		positionY = positionYback;
	}

	/**
	 * ��������� ������� �����.
	 */
	public void hiroFix() {
		positionXback = positionX;
		positionYback = positionY;
	}

	/**
	 * ��� � ����
	 */
	public void stepUP() {
		directionKey = stepHiro.UP;
	}

	/**
	 * ��� � ���
	 */
	public void stepDoun() {
		directionKey = stepHiro.DOWN;
	}

	/**
	 * ��� � ����
	 */
	public void stepLeft() {
		directionKey = stepHiro.LEFT;
	}

	/**
	 * ��� � �����
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

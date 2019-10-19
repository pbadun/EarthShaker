package by.redsky.earthshaker.games;

import by.redsky.earthshaker.IGameObjectsPix;
import by.redsky.earthshaker.games.livel.ILivels;

/**
 * ������� ��� ...
 * 
 * @author Jb
 *
 */
public class Maps implements IGameObjectsPix {
	// ������� �����
	int world[][];
	public int maxX; // ������������ �������� �������� �����
	public int maxY;
	// --------------------------------------------
	boolean down = false; // ���� ������ �������
	boolean downEnd = false; // ���� �������� �������
	// --------------------------------------------
	// ���� ����������� ������� ����-�����...
	boolean lr = false;
	// ������ � ����� �� ���� �� ����� , ��� ����� ������� �������������
	// �������� ��������
	boolean lineFl = false;
	// --------------------------------------------
	public boolean portalActive = false;
	// --------------------------------------------
	boolean hrPlus;
	boolean hrMinus;
	boolean hrStopped;
	int hrX;
	// --------------------------------------------
	public int kamenIsFair;

	// --------------------------------------------
	/**
	 * 
	 * @param maxX
	 * @param maxY
	 */
	public Maps(int maxX, int maxY, ILivels livel) {

		this.maxX = maxX;
		this.maxY = maxY;
		this.world = new int[maxX][maxY]; // ������� ���

		// ����������� �����.
		int w[][] = livel.livel();
		for (int y = 0; y < maxY; y++) {
			for (int x = 0; x < maxX; x++) {
				world[x][y] = w[y][x];
			}
		}

	}

	// ---------------------------------------------------------------------
	/**
	 * ������ ���� ��������� �������� ����
	 */
	public void update() {
		kamenIsFair = 0;
		down = false;
		downEnd = false;
		hrMinus = false;
		hrPlus = false;
		hrStopped = false;
		for (int y = maxY - 1; y >= 0; y--) {
			if (lineFl) {
				for (int x = 0; x < maxX; x++) {
					swither(x, y);
				}
			} else {
				for (int x = maxX - 1; x >= 0; x--) {
					swither(x, y);
				}
			}
			lineFl = !lineFl;
		}
	}

	/**
	 * ��������� ������ ��� ��������� �� ������� ��������� ����������. ������ ��
	 * ��������� �� �������� ������.
	 * 
	 * @param x
	 * @param y
	 */
	private void swither(int x, int y) {
		switch (world[x][y]) {
		case ES_ALMAZ:
			downObj(x, y);
			break;
		case ES_OGON:
			downObj(x, y);
			break;
		case ES_TIME:
			downObj(x, y);
			break;
		case ES_PORTAL_PASIVE:
			if (portalActive) {
				world[x][y] = ES_PORTAL_ACTIVE;
			}
			break;
		case ES_KAMEN:
			fair(x, y);
			downObj(x, y);
			break;
		case ES_KAMEN_GORIT1:
			world[x][y] = ES_KAMEN_GORIT2;
			break;
		case ES_KAMEN_GORIT2:
			world[x][y] = ES_KAMEN_GORIT3;
			break;
		case ES_KAMEN_GORIT3:
			world[x][y] = ES_KAMEN_GORIT4;
			break;
		case ES_KAMEN_GORIT4:
			world[x][y] = ES_KAMEN_GORIT5;
			break;
		case ES_KAMEN_GORIT5:
			world[x][y] = ES_KAMEN_GORIT6;
			break;
		case ES_KAMEN_GORIT6:
			world[x][y] = ES_NULL;
			break;
		case ES_HIRO:
			hrX = x;
			hrFlag(x, y);
			break;
		}
	}

	/**
	 * ����������� �����
	 * 
	 * @param x
	 * @param y
	 */
	private void hrFlag(int x, int y) {
		if (y < 1) {
			return;
		}
		if (x < (maxX - 1) && world[x + 1][y - 1] == ES_NULL) {
			hrPlus = true;
		}
		if (x > 0 && world[x - 1][y - 1] == ES_NULL) {
			hrMinus = true;
		}
		if (world[x][y - 1] == ES_NULL) {
			hrStopped = true;
		}

	}

	/**
	 * �������� ����� �� ����
	 * 
	 * @param x
	 * @param y
	 */
	private void fair(int x, int y) {
		if (y >= (maxY - 1)) {
			return;
		}
		if (world[x][y + 1] == ES_OGON) {
			world[x][y] = ES_KAMEN_GORIT1;
			kamenIsFair++;
		}
	}

	/**
	 * ��������� ���������� �������� �������...
	 * 
	 * @param x
	 * @param y
	 */
	private void downObj(int x, int y) {
		if (y >= (maxY - 1)) {
			return;
		}
		// ������� ����� � ���
		if (world[x][y + 1] == ES_NULL) {
			world[x][y + 1] = world[x][y];

			if (world[x][y] == ES_KAMEN && (y + 2) < maxY
					&& world[x][y + 2] != ES_NULL && world[x][y + 2] != ES_FLAG
					&& world[x][y + 2] != ES_HIRO) {
				// ���� ������� ����� �� �����...
				downEnd = true;
			} else {
				// ���� ������...
				down = true;
			}

			// ������ ���� ���-�� �� ��������� ��� ������������ ��� ���������...
			world[x][y] = ES_FLAG;
			return;
		}
		// ������ ����� �� �������� ������ - ����������� � ���� ��� � �����...
		// �� ��������.. ))
		if (world[x][y + 1] != ES_ZEMLIY && world[x][y + 1] != ES_FLAG
				&& world[x][y + 1] != ES_HIRO && world[x][y + 1] != ES_OGON) {
			boolean a = false, b = false;
			if (x >= 1 && world[x - 1][y] == ES_NULL
					&& world[x - 1][y + 1] == ES_NULL) {
				a = true;
			}
			if (x < (maxX - 1) && world[x + 1][y] == ES_NULL
					&& world[x + 1][y + 1] == ES_NULL) {
				b = true;
			}
			if (a || b) {
				if (a && b) {
					lr = !lr;
					if (lr) {
						a = false;
					} else {
						b = false;
					}
				}

				if (a) {
					world[x - 1][y] = world[x][y];
					world[x - 1][y + 1] = ES_FLAG;

				}
				if (b) {
					world[x + 1][y] = world[x][y];
					world[x + 1][y + 1] = ES_FLAG;
				}
				down = true;
				world[x][y] = ES_FLAG;
				return;
			}
		}

	}

	/**
	 * ������� ����� �� ������.
	 */
	void udtateClear() {
		// ������� �� ������ ������...
		for (int y = 0; y < maxY - 1; y++) {
			for (int x = 0; x < maxX; x++) {
				if (world[x][y] == ES_FLAG) {
					world[x][y] = ES_NULL;
				}
			}
		}
	}

	// ---------------------------------------------------------------------
	/**
	 * ������� ������ ����� �� �������� �����...
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hiroIsDown(int x, int y) {
		boolean fl = false;
		if (y < 2) {
			return false;
		}
		if (x < hrX && hrMinus) {
			fl = true;
		}
		if (x > hrX && hrPlus) {
			fl = true;
		}
		if (x == hrX && hrStopped) {
			fl = true;
		}

		if (world[x][y - 2] == ES_FLAG && fl) {
			if (world[x][y - 1] == ES_ALMAZ || world[x][y - 1] == ES_OGON
					|| world[x][y - 1] == ES_TIME
					|| world[x][y - 1] == ES_KAMEN) {
				world[x][y] = ES_HIRO_DIA;
				return true;
			}
		}
		return false;
	}

	// ---------------------------------------------------------------------

	public int getWorld(int x, int y) {
		return world[x][y];
	}

	public void setWorld(int x, int y, int obj) {
		this.world[x][y] = obj;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isDownEnd() {
		return downEnd;
	}

}

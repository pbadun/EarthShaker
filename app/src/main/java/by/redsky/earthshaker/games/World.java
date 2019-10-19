package by.redsky.earthshaker.games;

import by.redsky.earthshaker.IGameObjectsPix;
import by.redsky.earthshaker.games.Hiro.stepHiro;
import by.redsky.earthshaker.games.livel.ILivels;

/**
 * ��������� ���� � �����, ������� � �������� ����� ����.
 * 
 * @author Jb
 *
 */
public class World implements IGameObjectsPix {

	final float ADD_TIME = 0.3f; // �������������� �����...

	public int screenX; // ������� �������� �������
	public int screenY; // ������� ����� - 11 - ���������

	public Hiro hiro; // ������ �����
	public Maps map;
	public ILivels l;
	// --------------------------------------------
	public float time; // ������� ���������� �������
	public int scored; // ����� �������
	public int scoredTotal; // ����� �����, ������ �� ������ ���������� ������.
	public int scoredGame; // � ������� ����, ��� ������ - ��������...
	public int level;
	public boolean gameOver = false;
	public boolean gameOk = false;
	// ----------------------------------------------------------------
	public boolean snAlmaz; // ����� �����
	public boolean snTime; // ����� ���. �����.
	public boolean snZemlia; // ���� ����� �����
	public boolean snGameDie; // ����� ���������
	public boolean snStep;
	public boolean snAlmazOk;
	// ----------------------------------------------------------------
	float worldTime; // �����

	// ----------------------------------------------------------------
	/**
	 * �����������.
	 * 
	 * @param screenX
	 *            - ����������� ��������� ��������
	 * @param screenY
	 *            - ����������� ��������� �����
	 */
	public World(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.scoredTotal = 0;
		this.worldTime = 0;
	}

	public void setLevel(ILivels level, int ledelId) {
		this.scored = 0;
		this.scoredGame = 0;
		this.gameOver = false;
		this.gameOk = false;
		this.time = 120.0f;

		this.level = ledelId;
		this.l = level;
		this.map = new Maps(l.maxX(), l.mayY(), l);
		// �������� - �����
		this.hiro = new Hiro(map.maxX, map.maxY, l.hiroX(), l.HiroY());
		// -------------------------------------------
		map.setWorld(hiro.positionX, hiro.positionY, ES_HIRO);
	}

	// ----------------------------------------------------------------
	/**
	 * ���������� �����. ���� ������ �����.
	 * 
	 * @param deltaTime
	 */
	public boolean update(float deltaTime) {
		time -= deltaTime;
		worldTime += deltaTime;
		if (worldTime > 0.15f) {
			worldTime -= 0.15f;

			// ������ �����
			map.update();
			scoredGame += map.kamenIsFair * 5;
			// ��� �������
			hiro.update();

			snAlmaz = false;
			snTime = false;
			snZemlia = false;
			snGameDie = false;
			snStep = false;
			snAlmazOk = false;

			if (upHiro()) {
				map.setWorld(hiro.positionXback, hiro.positionYback, ES_NULL);
				map.setWorld(hiro.positionX, hiro.positionY, ES_HIRO);
				hiro.hiroFix();
				// ������ ����� �� ������� ���-���� � ������...
			} else {
				hiro.hiroBreak();
			}
			if (gameOver) {
				snGameDie = true;
				map.setWorld(hiro.positionX, hiro.positionY, ES_HIRO_DIA);
			}
			if (map.hiroIsDown(hiro.positionX, hiro.positionY)) {
				gameOver = true;
				snGameDie = true;
			}
			if (time <= 0.0f) {
				gameOver = true;
			}
			// ������� ����� �� ������ ������������ ��������.
			map.udtateClear();
			return true;
		}
		return false;

	}

	/**
	 * ��������� ����� �� ���� � ���� ����������� � � ���� ������������ ���
	 * ��������. ������ ��������� �� �������� ������ �� ���.
	 * 
	 * @return
	 */
	private boolean upHiro() {
		switch (map.getWorld(hiro.positionX, hiro.positionY)) {
		case ES_NULL: // ���� �� ������� ������������
			snStep = true;
			return true;
		case ES_ALMAZ:
			scored++; // + 1 ����� � �������
			snAlmaz = true;
			if (scored >= l.totalAlmaz()) {
				map.portalActive = true;
				snAlmazOk = true;
			}
			scoredGame += 100;
			return true;
		case ES_OGON: // ���������� � ����...
			gameOver = true;
			return true;
		case ES_PORTAL_ACTIVE: // ������� � �������� ������
			scoredTotal += scoredGame;
			gameOk = true;
			return true;
		case ES_TIME:
			time = (time + 15.0f) > 120.0f ? 120.0f : (time + 15.0f);
			snTime = true;
			scoredGame += 10;
			return true;
		case ES_VADA:
			return isVater();
		case ES_KAMEN:
			return isKamen();
		case ES_STENA:
			return false;
		case ES_ZEMLIY:
			snZemlia = true;
			return true;
		}
		return false;
	}

	/**
	 * ������� �����. ������ � ���� � � �����.
	 * 
	 * @return
	 */
	private boolean isKamen() {
		if (hiro.step == stepHiro.LEFT && hiro.positionX >= 1) {
			if (map.getWorld(hiro.positionX - 1, hiro.positionY) == ES_NULL) {
				map.setWorld(hiro.positionX - 1, hiro.positionY, ES_KAMEN);
				return true;
			}
		}
		if (hiro.step == stepHiro.RIGHT && hiro.positionX < map.maxX - 1) {
			if (map.getWorld(hiro.positionX + 1, hiro.positionY) == ES_NULL) {
				map.setWorld(hiro.positionX + 1, hiro.positionY, ES_KAMEN);
				return true;
			}
		}
		return false;
	}

	/**
	 * �������� �������� ���� � ������� ����.
	 * 
	 * @return
	 */
	private boolean isVater() {
		if (hiro.step == stepHiro.UP && hiro.positionY >= 1) {
			if (map.getWorld(hiro.positionX, hiro.positionY - 1) == ES_ZEMLIY
					|| map.getWorld(hiro.positionX, hiro.positionY - 1) == ES_NULL) {
				map.setWorld(hiro.positionX, hiro.positionY - 1, ES_VADA);
				return true;
			} else if (map.getWorld(hiro.positionX, hiro.positionY - 1) == ES_OGON) {
				map.setWorld(hiro.positionX, hiro.positionY - 1, ES_NULL);
				scoredGame += 5;
				return true;
			}
		}
		if (hiro.step == stepHiro.DOWN && hiro.positionY < map.maxY - 1) {
			if (map.getWorld(hiro.positionX, hiro.positionY + 1) == ES_ZEMLIY
					|| map.getWorld(hiro.positionX, hiro.positionY + 1) == ES_NULL) {
				map.setWorld(hiro.positionX, hiro.positionY + 1, ES_VADA);
				return true;
			} else if (map.getWorld(hiro.positionX, hiro.positionY + 1) == ES_OGON) {
				map.setWorld(hiro.positionX, hiro.positionY + 1, ES_NULL);
				scoredGame += 5;
				return true;
			}
		}
		if (hiro.step == stepHiro.LEFT && hiro.positionX >= 1) {
			if (map.getWorld(hiro.positionX - 1, hiro.positionY) == ES_ZEMLIY
					|| map.getWorld(hiro.positionX - 1, hiro.positionY) == ES_NULL) {
				map.setWorld(hiro.positionX - 1, hiro.positionY, ES_VADA);
				return true;
			} else if (map.getWorld(hiro.positionX - 1, hiro.positionY) == ES_OGON) {
				map.setWorld(hiro.positionX - 1, hiro.positionY, ES_NULL);
				scoredGame += 5;
				return true;
			}
		}
		if (hiro.step == stepHiro.RIGHT && hiro.positionX < map.maxX - 1) {
			if (map.getWorld(hiro.positionX + 1, hiro.positionY) == ES_ZEMLIY
					|| map.getWorld(hiro.positionX + 1, hiro.positionY) == ES_NULL) {
				map.setWorld(hiro.positionX + 1, hiro.positionY, ES_VADA);
				return true;
			} else if (map.getWorld(hiro.positionX + 1, hiro.positionY) == ES_OGON) {
				map.setWorld(hiro.positionX + 1, hiro.positionY, ES_NULL);
				scoredGame += 5;
				return true;
			}
		}

		return false;
	}

	// ----------------------------------------------------------------
	/**
	 * �������� ������� �����, � ������ ���������. ������������ � ���� - ���
	 * ������, � ����� � ���� - ���� ������ �����, ���� �� ������������
	 * ������������� �������� � ��������� �����.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getPix(int x, int y) {
		int xx = hiro.positionX - screenX / 2 + x;
		int yy = hiro.positionY - screenY / 2 + y;

		if (-2 > (hiro.positionX - screenX / 2)) {
			xx = x - 2;
		}
		if (xx < 0) {
			return ES_STENA;
		}

		if (-1 > (hiro.positionY - screenY / 2)) {
			yy = y - 1;
		}
		if (yy < 0) {
			return ES_STENA;
		}

		if (hiro.positionX + screenX / 2 >= l.maxX()) {
			xx = l.maxX() - screenX + x;
		}

		if (hiro.positionY + screenY / 2 >= l.mayY() + 1) {
			yy = l.mayY() - screenY + y + 1;
		}
		if (yy >= l.mayY()) {
			return ES_STENA;
		}

		return map.getWorld(xx, yy);
	}
}

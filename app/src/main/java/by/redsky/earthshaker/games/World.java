package by.redsky.earthshaker.games;

import by.redsky.earthshaker.IGameObjectsPix;
import by.redsky.earthshaker.games.Hiro.stepHiro;
import by.redsky.earthshaker.games.livel.ILivels;

/**
 * Обработка мира и гироя, времени и текущего счета игры.
 * 
 * @author Jb
 *
 */
public class World implements IGameObjectsPix {

	final float ADD_TIME = 0.3f; // дополнительное время...

	public int screenX; // сколько столбцов выводим
	public int screenY; // сколько строк - 11 - константа

	public Hiro hiro; // Объект гироя
	public Maps map;
	public ILivels l;
	// --------------------------------------------
	public float time; // Счетчик прошедшего времени
	public int scored; // Всего алмазов
	public int scoredTotal; // Всего очков, только за удачно пройденные уровни.
	public int scoredGame; // В текущей игре, при сметри - зануляем...
	public int level;
	public boolean gameOver = false;
	public boolean gameOk = false;
	// ----------------------------------------------------------------
	public boolean snAlmaz; // Взяли алмаз
	public boolean snTime; // взяли доп. время.
	public boolean snZemlia; // ижем через землю
	public boolean snGameDie; // героя раздавило
	public boolean snStep;
	public boolean snAlmazOk;
	// ----------------------------------------------------------------
	float worldTime; // время

	// ----------------------------------------------------------------
	/**
	 * Конструктор.
	 * 
	 * @param screenX
	 *            - колличество выводимых столбцов
	 * @param screenY
	 *            - колличество выводимых строк
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
		// Создание - героя
		this.hiro = new Hiro(map.maxX, map.maxY, l.hiroX(), l.HiroY());
		// -------------------------------------------
		map.setWorld(hiro.positionX, hiro.positionY, ES_HIRO);
	}

	// ----------------------------------------------------------------
	/**
	 * Обновление карты. Если пришло время.
	 * 
	 * @param deltaTime
	 */
	public boolean update(float deltaTime) {
		time -= deltaTime;
		worldTime += deltaTime;
		if (worldTime > 0.15f) {
			worldTime -= 0.15f;

			// Прощет карты
			map.update();
			scoredGame += map.kamenIsFair * 5;
			// Тут процесс
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
				// Гибель гироя от падения чем-либо с высоты...
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
			// Очистка карты от флагов передвижения обьектов.
			map.udtateClear();
			return true;
		}
		return false;

	}

	/**
	 * Проверяем можно ли идти в этом напровлении и к каим последствиям это
	 * приведет. Физика зависящая от действий игрока на мир.
	 * 
	 * @return
	 */
	private boolean upHiro() {
		switch (map.getWorld(hiro.positionX, hiro.positionY)) {
		case ES_NULL: // идем по пустому пространству
			snStep = true;
			return true;
		case ES_ALMAZ:
			scored++; // + 1 алмаз в капилку
			snAlmaz = true;
			if (scored >= l.totalAlmaz()) {
				map.portalActive = true;
				snAlmazOk = true;
			}
			scoredGame += 100;
			return true;
		case ES_OGON: // зажарились в огне...
			gameOver = true;
			return true;
		case ES_PORTAL_ACTIVE: // Переход в активный портал
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
	 * Двигаем камни. Только в лево и в право.
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
	 * Действие пузырька воды в игровом мире.
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
	 * Получаем элемент карты, с учетом координат. Дарисовываем с лева - две
	 * строки, с верху и низу - одна строка стены, учет на пространство
	 * перекрываемое кнопками и счетчиком очков.
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

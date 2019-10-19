package by.redsky.earthshaker.games.livel;

public class LivelTest implements ILivels{

	@Override
	public int[][] livel() {
		int[][] l = {
				{0,0,0,0,0,0,0,2,3,3,3,3,2,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,2,3,3,2,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,3,2,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,3,2,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5},
		};
		return l;
	}

	@Override
	public int totalAlmaz() {
		return 5;
	}

	@Override
	public int hiroX() {
		return 9;
	}

	@Override
	public int HiroY() {
		return 4;
	}

	@Override
	public int maxX() {
		return 19;
	}

	@Override
	public int mayY() {
		return 10;
	}

}

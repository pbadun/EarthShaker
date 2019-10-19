package by.redsky.earthshaker;

import by.redsky.earthshaker.screen.LoadingScreen;
import by.redsky.framework.impl.AndroidGame;
import by.redsky.framework.interfaces.Screen;

public class EarthShaker extends AndroidGame {

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

}

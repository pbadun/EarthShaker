package by.redsky.framework.interfaces;

import android.app.Activity;

public interface Game {

	public Input getInput();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public Audio getAudio();

	public void setScreen(Screen screen);

	public Screen getCurrentScreen();

	public Screen getStartScreen();

	// ----------------------------------------
	public int getFrameBufferWidth();

	public int getFrameBufferHeight();

	// ��� ������� � �������� Android
	public Activity getActivity();

	public boolean isBackKey();

	public boolean isMenuKey();

	// ������� / ������� ��������� ����
	public void setAdModVisibility(boolean fl);
}

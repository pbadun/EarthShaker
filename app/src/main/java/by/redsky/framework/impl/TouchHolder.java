package by.redsky.framework.impl;

import java.util.List;

import by.redsky.framework.interfaces.Input.TouchEvent;
import android.view.View.OnTouchListener;

public interface TouchHolder extends OnTouchListener {

	public boolean isTouchDown(int pointer);

	public int getTouchX(int pointer);

	public int getTouchY(int pointer);

	public List<TouchEvent> getTouchEvents();
}

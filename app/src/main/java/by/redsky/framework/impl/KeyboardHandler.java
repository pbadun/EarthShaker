package by.redsky.framework.impl;

import java.util.ArrayList;
import java.util.List;

import by.redsky.framework.interfaces.Pool;
import by.redsky.framework.interfaces.Input.KeyEvent;
import by.redsky.framework.interfaces.Pool.PoolObjectFactory;
import android.view.View;
import android.view.View.OnKeyListener;

public class KeyboardHandler implements OnKeyListener {
	boolean[] pressedKey = new boolean[128];
	Pool<KeyEvent> keyEventPool;
	List<KeyEvent> keyEventBuffer = new ArrayList<KeyEvent>();
	List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();

	public KeyboardHandler(View view) {
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
			@Override
			public KeyEvent createObject() {
				return new KeyEvent();
			}
		};
		keyEventPool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

	@Override
	public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE) {
			return false;
		}
		synchronized (this) {
			KeyEvent keyEvent = keyEventPool.newObject();
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) event.getUnicodeChar();
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				keyEvent.type = KeyEvent.KEY_DOWN;
				if (keyCode > 0 && keyCode < 127) {
					pressedKey[keyCode] = true;
				}
			}
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				keyEvent.type = KeyEvent.KEY_UP;
				if (keyCode > 0 && keyCode < 127) {
					pressedKey[keyCode] = false;
				}
			}
		}
		return false;
	}

	public boolean isKeyPressed(int keyCode) {
		if (keyCode < 0 || keyCode > 127) {
			return false;
		}
		return pressedKey[keyCode];
	}

	public List<KeyEvent> getKeyEvents() {
		synchronized (this) {
			int len = keyEvents.size();
			for (int i = 0; i < len; i++) {
				keyEventPool.free(keyEvents.get(i));
			}
			keyEvents.clear();
			keyEvents.addAll(keyEventBuffer);
			keyEventBuffer.clear();
			return keyEvents;
		}
	}
}

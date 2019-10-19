package by.redsky.framework.interfaces;

import by.redsky.framework.interfaces.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();

	public int getHeight();

	public PixmapFormat getFormat();

	public void dispose();
}

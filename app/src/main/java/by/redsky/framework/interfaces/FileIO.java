package by.redsky.framework.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Работа с файлами
 * 
 * @author jb
 *
 */
public interface FileIO {

	public InputStream readAsset(String name) throws IOException;

	public InputStream readFile(String name) throws IOException;

	public OutputStream writeFile(String name) throws IOException;
}

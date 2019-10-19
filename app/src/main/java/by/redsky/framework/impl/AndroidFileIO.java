package by.redsky.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;
import by.redsky.framework.interfaces.FileIO;

/**
 * Класс для работы с файлами (обьектами, картинками) из хранилища программы. А
 * так же с внешним накопителем (external flash)
 * 
 * @author jb
 *
 */
public class AndroidFileIO implements FileIO {
	AssetManager asset;
	String externalStoragePAth;

	public AndroidFileIO(AssetManager asset) {
		this.asset = asset;
		this.externalStoragePAth = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
	}

	@Override
	public InputStream readAsset(String name) throws IOException {
		return asset.open(name);
	}

	@Override
	public InputStream readFile(String name) throws IOException {
		return new FileInputStream(externalStoragePAth + name);
	}

	@Override
	public OutputStream writeFile(String name) throws IOException {
		return new FileOutputStream(externalStoragePAth + name);
	}

}

package by.redsky.framework.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import by.redsky.framework.interfaces.Audio;
import by.redsky.framework.interfaces.Music;
import by.redsky.framework.interfaces.Sound;

/**
 * Класс отвечающий за загрузку музыки и звуковых эффектов
 * 
 * @author jb
 *
 */
public class AndroidAudio implements Audio {
	AssetManager asset;
	SoundPool soundPuol;

	@SuppressWarnings("deprecation")
	public AndroidAudio(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.asset = activity.getAssets();
		this.soundPuol = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	@Override
	public Music newMusic(String fileName) {
		try {
			AssetFileDescriptor assetFileDescriptor = asset.openFd(fileName);
			return new AndroidMusic(assetFileDescriptor);
		} catch (IOException e) {
			throw new RuntimeException(
					"Не возможно загрузить музыкальный файл '" + fileName + "'");
		}
	}

	@Override
	public Sound newSound(String fileName) {
		try {
			AssetFileDescriptor assetFileDescriptor = asset.openFd(fileName);
			int soundid = soundPuol.load(assetFileDescriptor, 0);
			return new AndroidSound(soundPuol, soundid);
		} catch (IOException e) {
			throw new RuntimeException("Не возможно загрузить звуковой файл '"
					+ fileName + "'");
		}
	}

}

package by.redsky.framework.impl;

import com.google.android.gms.ads.*;

import by.redsky.earthshaker.Assets;
import by.redsky.earthshaker.R;
import by.redsky.earthshaker.Settings;
import by.redsky.framework.interfaces.Audio;
import by.redsky.framework.interfaces.FileIO;
import by.redsky.framework.interfaces.Game;
import by.redsky.framework.interfaces.Graphics;
import by.redsky.framework.interfaces.Input;
import by.redsky.framework.interfaces.Screen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * @author Jb
 */
public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;

    // -----------------------------
    // �������
    int frameBufferWidth;
    int frameBufferHeight;
    // -----------------------------
    //
    boolean backKey = false;
    boolean menuKey = false;

    // -----------------------------
    private AdView adView;
    private AdRequest adRequest;

    // -----------------------------
    private boolean isAdVisible = false;
    private final int VISIBLE = 1;
    private final int INVISIBLE = 2;

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VISIBLE:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case INVISIBLE:
                    adView.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    // -----------------------------

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // --------------------------------------------------------------
        // Adsense
        this.adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-4157203429915172/8676083182");
        adView.setAdSize(AdSize.BANNER);
        // --------------------------------------------------------------
        // Analitic
        //Assets.tracker = GoogleAnalytics.getInstance(this).newTracker(
        //		"UA-61103902-2");
        // --------------------------------------------------------------
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getWindow().getDecorView().setSystemUiVisibility(
        // View.SYSTEM_UI_FLAG_FULLSCREEN
        // | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dispMetrics = new DisplayMetrics();
        display.getMetrics(dispMetrics);
        // ------------------------------
        boolean isLandsape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        // ------------------------------
        int heightScreen = dispMetrics.heightPixels > dispMetrics.widthPixels ? dispMetrics.widthPixels
                : dispMetrics.heightPixels;
        int widthScreen = 704 * dispMetrics.widthPixels / heightScreen;
        // ------------------------------
        this.frameBufferWidth = isLandsape ? widthScreen : 704;
        this.frameBufferHeight = isLandsape ? 704 : widthScreen;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        float scaleX = (float) frameBufferWidth / dispMetrics.widthPixels;
        float scaleY = (float) frameBufferHeight / dispMetrics.heightPixels;

        // ------------------------------
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getStartScreen();
        // ------------------------------
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
                "GLGame");
        setContentView(renderView);
        //TODO Remove ADS
        //addAdMod();
    }

    @SuppressLint("Wakelock")
    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
        adView.resume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();
        if (isFinishing()) {
            screen.dispose();
        }
        Settings.save(fileIO);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null) {
            throw new RuntimeException("Screen mast not be null");
        }
        if (this.screen != null) {
            this.screen.pause();
            this.screen.dispose();
        }
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    // ------------------------------------------------
    @Override
    public int getFrameBufferWidth() {
        return frameBufferWidth;
    }

    @Override
    public int getFrameBufferHeight() {
        return frameBufferHeight;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    // ------------------------------------------------
    // ������� ��������� ������ �� ����
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        backKey = true;
    }

    /**
     * ������ �� ������� ������ �� ����. ���� ���.
     *
     * @return
     */
    @Override
    public boolean isBackKey() {
        boolean tm = backKey;
        backKey = false;
        return tm;
    }

    // ------------------------------------------------
    // ������� ������� ����, ����� HOME
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            event.startTracking();
            menuKey = true;
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            event.startTracking();
            menuKey = true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean isMenuKey() {
        boolean tm = menuKey;
        menuKey = false;
        return tm;
    }

    // ---------------------------------------------------------------------------

    /**
     * ����������� ����� �������.
     */
    private void addAdMod() {
        this.adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getResources().getString(R.string.ad_test_device)).build();

        adView.loadAd(adRequest);

        // ------------------------------
        RelativeLayout rl = new RelativeLayout(this);
        // ������������� �� ��� ������� ������ ����
        rl.addView(renderView);

        // ������������� ��������� ����������� ���������� ������.
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // adParams.addRule(RelativeLayout.ALIGN_TOP);
        adParams.addRule(RelativeLayout.BELOW, renderView.getId());

        rl.addView(adView, adParams);

        setContentView(rl);
        // �����-�� ������.
        adView.setVisibility(View.INVISIBLE);
    }

    /**
     * ��������/ ������ ���� �������.
     *
     * @param fl
     */
    @Override
    public void setAdModVisibility(boolean fl) {

        // ������ �� ���������� ������������...
        if (fl == isAdVisible) {
            return;
        }
        isAdVisible = fl;

        Message msg = new Message();
        // ������ ��������� �����...
        if (fl) {
            msg.what = VISIBLE;
        } else {
            msg.what = INVISIBLE;
        }
        handler.sendMessage(msg);

    }
}

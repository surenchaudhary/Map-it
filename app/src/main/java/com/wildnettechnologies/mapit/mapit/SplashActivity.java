package com.wildnettechnologies.mapit.mapit;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.skobbler.ngx.SKDeveloperKeyException;
import com.skobbler.ngx.SKMaps;
import com.skobbler.ngx.SKMapsInitSettings;
import com.skobbler.ngx.SKPrepareMapTextureListener;
import com.skobbler.ngx.SKPrepareMapTextureThread;
import com.skobbler.ngx.map.SKMapViewStyle;
import com.skobbler.ngx.navigation.SKAdvisorSettings;
import com.wildnettechnologies.mapit.mapit.routeModule.RouteActivity;

import java.io.File;

public class SplashActivity extends Activity implements SKPrepareMapTextureListener {

    private String mapResDirPath;
    public static final long KILO = 1024;
    public static final long MEGA = KILO * KILO;
    int a =5;

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String applicationPath = chooseStoragePath();

        if (applicationPath != null) {
            mapResDirPath = applicationPath + "/" + "SkMaps" + "/";
        } else {
            Toast.makeText(this, "Sorry there is no storage path available. (Consider freeing some internal memory)", Toast.LENGTH_LONG).show();
            finish();
        }

         SKPrepareMapTextureThread prepThread = new SKPrepareMapTextureThread(this, mapResDirPath, "SKMaps.zip", this);
        prepThread.start();

        try {
            prepThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String chooseStoragePath() {
        if (getAvailableMemorySize(Environment.getDataDirectory().getPath()) >= 50 * MEGA) {
            return this.getFilesDir().getPath();
        } else if (this.getExternalFilesDir(null) != null) {
            if (getAvailableMemorySize(this.getExternalFilesDir(null).toString()) >= 50 * MEGA) {
                return this.getExternalFilesDir(null).toString();
            }
        }

        Toast.makeText(this, "There is not enough memory on any storage, but return internal memory", Toast.LENGTH_LONG);

        if (this.getFilesDir() != null) {
            return this.getFilesDir().getPath();
        } else {
            if (this.getExternalFilesDir(null) != null) {
                return this.getExternalFilesDir(null).toString();
            } else {
                return null;
            }
        }
    }


    /***
     * changes have been made.... (not the same as demo app's getAvailableMemorySize(..) method).
     *
     * @param path
     * @return
     */

    public long getAvailableMemorySize(String path) {
        StatFs statfs = null;
        long availableMemorySize = 0;
        try {
            statfs = new StatFs(path);
            if (statfs != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                    availableMemorySize = statfs.getAvailableBytes();
                else {
                    availableMemorySize = new File(path.toString()).getFreeSpace();
                }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return availableMemorySize;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapTexturesPrepared(boolean statusOk) {
        if (statusOk) {
            initializeSkMaps();
//            splashDisplay();
            goToMap();
        }
    }

    private void splashDisplay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, RouteActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    private void goToMap() {
        finish();
        startActivity(new Intent(this, RouteActivity.class));
//        Toast.makeText(this, "Hshdsakjdsa", Toast.LENGTH_LONG).show();
    }


    private void initializeSkMaps() {
        SKMapsInitSettings initMapSettings = new SKMapsInitSettings();

        initMapSettings.setMapResourcesPaths(mapResDirPath,
                new SKMapViewStyle(mapResDirPath + "daystyle/", "daystyle.json"));

        final SKAdvisorSettings advisorSettings = initMapSettings.getAdvisorSettings();
        advisorSettings.setAdvisorConfigPath(mapResDirPath + "/Advisor");
        advisorSettings.setResourcePath(mapResDirPath + "/Advisor/Languages");
        advisorSettings.setLanguage(SKAdvisorSettings.SKAdvisorLanguage.LANGUAGE_EN);
        advisorSettings.setAdvisorVoice("en");
        initMapSettings.setAdvisorSettings(advisorSettings);
        try {
            SKMaps.getInstance().initializeSKMaps(this, initMapSettings);
        } catch (SKDeveloperKeyException exception) {
            exception.printStackTrace();
        }
    }

}

package app.opengl.com.ponoramaapp;

import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.ScreenParams;

import org.r3d.cardboard.RCardboardRenderer;
import org.r3d.cardboard.RCardboardView;

import java.io.File;
import java.io.InputStream;

/**
 * Created by ll on 2016/3/30.
 */
public class VRVideoActivity extends CardboardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RCardboardView view = new RCardboardView(VRVideoActivity.this);
        view.setSettingsButtonEnabled(false);
        view.setVignetteEnabled(false);
        view.setAlignmentMarkerEnabled(false);
        ScreenParams params = view.getScreenParams();
        params.setBorderSizeMeters(0.001f);
        view.updateScreenParams(params);

        setContentView(view);
        setCardboardView(view);
        String path = Environment.getExternalStorageDirectory()+ File.separator + "360Videos"+ File.separator+"jungle.mp4";
        InputStream is = getResources().openRawResource(R.raw.jungle);
        try {
            byte b[] = new byte[is.available()];
            is.read(b);
            path = new String(b);
        }catch (Exception e){}
        RCardboardRenderer renderer = new VideoRenderer(VRVideoActivity.this, path);
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);
        Log.e("1111=====",path);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.e("2222", "on");
    }
}
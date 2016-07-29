package app.opengl.com.ponoramaapp;

import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.ScreenParams;

import org.r3d.cardboard.RCardboardRenderer;
import org.r3d.cardboard.RCardboardView;

/**
 * Created by liuliang on 2016/4/27.
 */
public class VRImageActivity extends CardboardActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();  //初始化数据

    }
    private void init() {
        RCardboardView view = new RCardboardView(VRImageActivity.this);
        view.setSettingsButtonEnabled(false);
        view.setVignetteEnabled(false);
        view.setAlignmentMarkerEnabled(false);
        ScreenParams params = view.getScreenParams();
        params.setBorderSizeMeters(0.001f);
        view.updateScreenParams(params);

        setContentView(view);
        setCardboardView(view);
        RCardboardRenderer renderer = new ImageRenderer(VRImageActivity.this);
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);
    }
}

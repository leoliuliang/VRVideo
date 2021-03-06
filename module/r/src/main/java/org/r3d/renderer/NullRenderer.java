package org.r3d.renderer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.MotionEvent;

import org.r3d.util.RajLog;

/**
 * Minimal {@link RRenderer} implementation which will cause no rendering to occur.
 *
 * @author Ian Thomas (toxicbakery@gmail.com)
 */
public final class NullRenderer extends RRenderer {

    public NullRenderer(Context context) {
        super(context);
        RajLog.w(this + ": Fragment created without renderer!");
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    protected void initScene() {

    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surface) {
        super.onRenderSurfaceDestroyed(surface);
        stopRendering();
    }
}
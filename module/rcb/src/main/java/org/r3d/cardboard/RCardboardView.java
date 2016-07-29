package org.r3d.cardboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.vrtoolkit.cardboard.CardboardView;

import org.r3d.surface.IRSurface;
import org.r3d.surface.IRSurfaceRenderer;

public class RCardboardView extends CardboardView implements IRSurface {

    private IRSurfaceRenderer renderer;

    public RCardboardView(Context context) {
        super(context);
    }

    public RCardboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            onPause();
        } else {
            onResume();
        }
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onResume();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        renderer.onRenderSurfaceDestroyed(null);
    }

    @Override
    public void setFrameRate(double rate) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setAntiAliasingMode(ANTI_ALIASING_CONFIG config) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setSampleCount(int count) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setSurfaceRenderer(IRSurfaceRenderer renderer) throws IllegalStateException {

        if (this.renderer != null)
            throw new IllegalStateException("A renderer has already been set for this view.");

        renderer.setRenderSurface(this);

        this.renderer = renderer;

        onPause(); // We want to halt the surface view until we are ready
    }

    @Override
    public void requestRenderUpdate() {
        requestRender();
    }
}

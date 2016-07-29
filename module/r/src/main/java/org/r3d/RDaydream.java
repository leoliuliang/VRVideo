/**
 * Copyright 2013 Dennis Ippel
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.r3d;

import android.annotation.TargetApi;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import org.r3d.surface.IRSurface;
import org.r3d.surface.IRSurfaceRenderer;
import org.r3d.surface.RSurfaceView;
import org.r3d.util.Capabilities;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public abstract class RDaydream extends DreamService implements IRDisplay {

    protected RSurfaceView mSurfaceView;
    protected FrameLayout mLayout;

    private IRSurfaceRenderer mRajawaliRenderer;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        mSurfaceView = new RSurfaceView(this);
        mSurfaceView.setEGLContextClientVersion(Capabilities.getGLESMajorVersion());

        setInteractive(false);
        setFullscreen(true);

        mLayout = new FrameLayout(this);
        mLayout.addView(mSurfaceView);

        setContentView(mLayout);

        setRenderer(createRenderer());
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mSurfaceView.onResume();
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
        mSurfaceView.onPause();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRajawaliRenderer.onRenderSurfaceDestroyed(null);
        unbindDrawables(mLayout);
        System.gc();
    }

    @Override
    public int getLayoutID() {
        // We dont need this for daydreams, just return 0;
        return 0;
    }

    protected void setAntiAliasingConfig(IRSurface.ANTI_ALIASING_CONFIG config) {
        mSurfaceView.setAntiAliasingMode(config);
    }

    protected void setRenderer(IRSurfaceRenderer renderer) {
        mRajawaliRenderer = renderer;
        mSurfaceView.setSurfaceRenderer(mRajawaliRenderer);
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}

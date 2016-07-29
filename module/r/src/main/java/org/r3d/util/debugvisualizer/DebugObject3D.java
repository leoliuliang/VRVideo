package org.r3d.util.debugvisualizer;

import android.graphics.Color;

import org.r3d.primitives.Line3D;
import org.r3d.renderer.RRenderer;

/**
 * @author dennis.ippel
 */
public class DebugObject3D extends Line3D {
    protected RRenderer mRenderer;

    public DebugObject3D() {
        this(Color.YELLOW, 1);
    }

    public DebugObject3D(int color, int lineThickness) {
        setColor(color);
        mLineThickness = lineThickness;
    }

    public void setRenderer(RRenderer renderer) {
        mRenderer = renderer;
    }
}

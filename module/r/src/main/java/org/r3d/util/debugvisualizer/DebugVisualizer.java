package org.r3d.util.debugvisualizer;

import org.r3d.Object3D;
import org.r3d.renderer.RRenderer;

/**
 * @author dennis.ippel
 */
public class DebugVisualizer extends Object3D {
    private RRenderer mRenderer;

    public DebugVisualizer(RRenderer renderer) {
        mRenderer = renderer;
    }

    public void addChild(DebugObject3D child) {
        super.addChild(child);
        child.setRenderer(mRenderer);
    }
}

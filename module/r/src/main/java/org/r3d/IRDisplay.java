package org.r3d;

import android.opengl.GLSurfaceView;
import android.widget.FrameLayout;

import org.r3d.surface.IRSurfaceRenderer;

/**
 * Interface defining some common methods which all Rajawali displays methods must implement.
 *
 * @author Jared Woolston (jwoolston@idealcorp.com)
 */
public interface IRDisplay {

    /**
     * Creates the {@link IRSurfaceRenderer} to use in this display. Optionally null can be returned by displays
     * that do not intend to display a rendered scene. Returning null will cause a warning to be
     * logged to the console in the event null is in error.
     *
     * @return {@link IRSurfaceRenderer} The renderer which will be assigned to the {@link GLSurfaceView} in this display, or null.
     */
    public IRSurfaceRenderer createRenderer();

    /**
     * Retrieves the layout resource ID to inflate when creating the display's view. If you handle creating the view
     * yourself (such as with a DayDream), this method can return any integer. If you allow this display to create the
     * view, the resource is expected to have a {@link FrameLayout} as its root layout.
     *
     * @return {@code int} The layout resource ID to use.
     */
    public int getLayoutID();
}

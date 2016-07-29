package org.r3d.loader.async;

import org.r3d.loader.ALoader;
import org.r3d.scene.RScene;

/**
 * Interface for defining a asynchronous loader callback. This will be provided
 * to the {@link RScene#loadModel(ALoader, IAsyncLoaderCallback, int)} and
 * related calls.
 *
 * @author Jared Woolston (jwoolston@idealcorp.com)
 * @author Ian Thomas (toxicbakery@gmail.com)
 */
public interface IAsyncLoaderCallback {

    /**
     * Callback listener indicating the returned loader successfully parsed the model resource.
     *
     * @param loader
     */
    public void onModelLoadComplete(ALoader loader);

    /**
     * Callback listener indicating the returned loader failed to parse the model resource.
     *
     * @param loader
     */
    public void onModelLoadFailed(ALoader loader);
}

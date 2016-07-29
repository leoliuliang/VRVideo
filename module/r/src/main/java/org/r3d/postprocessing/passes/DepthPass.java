package org.r3d.postprocessing.passes;

import android.opengl.GLES20;

import org.r3d.cameras.Camera;
import org.r3d.materials.Material;
import org.r3d.materials.plugins.DepthMaterialPlugin;
import org.r3d.postprocessing.APass;
import org.r3d.primitives.ScreenQuad;
import org.r3d.renderer.RRenderer;
import org.r3d.renderer.RenderTarget;
import org.r3d.scene.RScene;


public class DepthPass extends APass {
	protected RScene mScene;
	protected Camera mCamera;
	protected Camera mOldCamera;
	protected DepthMaterialPlugin mDepthPlugin;

	public DepthPass(RScene scene, Camera camera) {
		mPassType = PassType.DEPTH;
		mScene = scene;
		mCamera = camera;
		
		mEnabled = true;
		mClear = true;
		mNeedsSwap = true;
		
		Material mat = new Material();
		mDepthPlugin = new DepthMaterialPlugin();
		mat.addPlugin(mDepthPlugin);
		setMaterial(mat);
	}
	
	@Override
	public void render(RScene scene, RRenderer renderer, ScreenQuad screenQuad, RenderTarget writeTarget,
			RenderTarget readTarget, long ellapsedTime, double deltaTime) {
		GLES20.glClearColor(0, 0, 0, 1);
		mDepthPlugin.setFarPlane((float)mCamera.getFarPlane());
		mOldCamera = mScene.getCamera();
		mScene.switchCamera(mCamera);
		mScene.render(ellapsedTime, deltaTime, writeTarget, mMaterial);
		mScene.switchCamera(mOldCamera);
	}
}

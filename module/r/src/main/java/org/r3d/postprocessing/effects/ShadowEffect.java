package org.r3d.postprocessing.effects;

import org.r3d.cameras.Camera;
import org.r3d.lights.DirectionalLight;
import org.r3d.materials.textures.ATexture.FilterType;
import org.r3d.materials.textures.ATexture.WrapType;
import org.r3d.postprocessing.APostProcessingEffect;
import org.r3d.postprocessing.materials.ShadowMapMaterial;
import org.r3d.postprocessing.passes.ShadowPass;
import org.r3d.postprocessing.passes.ShadowPass.ShadowPassType;
import org.r3d.renderer.RRenderer;
import org.r3d.renderer.RenderTarget;
import org.r3d.scene.RScene;

import android.graphics.Bitmap.Config;
import android.opengl.GLES20;


public class ShadowEffect extends APostProcessingEffect {
	private RScene mScene;
	private Camera mCamera;
	private DirectionalLight mLight;
	private int mShadowMapSize;
	private RenderTarget mShadowRenderTarget;
	private float mShadowInfluence;
	private ShadowMapMaterial mShadowMapMaterial;
	
	public ShadowEffect(RScene scene, Camera camera, DirectionalLight light, int shadowMapSize) {
		super();
		mScene = scene;
		mCamera = camera;
		mLight = light;
		mShadowMapSize = shadowMapSize;
	}
	
	public void setShadowInfluence(float influence) {
		mShadowInfluence = influence;
		if(mShadowMapMaterial != null)
			mShadowMapMaterial.setShadowInfluence(influence);
	}

	@Override
	public void initialize(RRenderer renderer) {
		mShadowRenderTarget = new RenderTarget("shadowRT" + hashCode(), mShadowMapSize, mShadowMapSize, 0, 0,
				false, false, GLES20.GL_TEXTURE_2D, Config.ARGB_8888,
				FilterType.LINEAR, WrapType.CLAMP);
		renderer.addRenderTarget(mShadowRenderTarget);
		
		ShadowPass pass1 = new ShadowPass(ShadowPassType.CREATE_SHADOW_MAP, mScene, mCamera, mLight, mShadowRenderTarget);
		addPass(pass1);
		ShadowPass pass2 = new ShadowPass(ShadowPassType.APPLY_SHADOW_MAP, mScene, mCamera, mLight, mShadowRenderTarget);
		mShadowMapMaterial = pass1.getShadowMapMaterial();
		mShadowMapMaterial.setShadowInfluence(mShadowInfluence);
		pass2.setShadowMapMaterial(pass1.getShadowMapMaterial());
		addPass(pass2);
	}
}

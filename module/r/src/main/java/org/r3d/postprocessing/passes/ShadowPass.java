package org.r3d.postprocessing.passes;

import org.r3d.cameras.Camera;
import org.r3d.lights.DirectionalLight;
import org.r3d.postprocessing.materials.ShadowMapMaterial;
import org.r3d.primitives.ScreenQuad;
import org.r3d.renderer.RRenderer;
import org.r3d.renderer.RenderTarget;
import org.r3d.scene.RScene;


public class ShadowPass extends RenderPass {
	private RenderTarget mShadowRenderTarget;
	private int mShadowMapSize;
	
	public static enum ShadowPassType {
		CREATE_SHADOW_MAP, APPLY_SHADOW_MAP
	}
	
	private ShadowMapMaterial mShadowMapMaterial;
	private ShadowPassType mShadowPassType;

	public ShadowPass(ShadowPassType shadowPassType, RScene scene, Camera camera, DirectionalLight light, RenderTarget renderTarget) {
		super(scene, camera, 0);
		mShadowPassType = shadowPassType;
		mShadowRenderTarget = renderTarget;
		mShadowMapSize = renderTarget.getWidth();
		if(shadowPassType == ShadowPassType.CREATE_SHADOW_MAP) {
			mShadowMapMaterial = new ShadowMapMaterial();
			mShadowMapMaterial.setLight(light);
			mShadowMapMaterial.setCamera(camera);
			mShadowMapMaterial.setScene(scene);
			setMaterial(mShadowMapMaterial);
		}
	}
	
	@Override
	public void render(RScene scene, RRenderer renderer, ScreenQuad screenQuad, RenderTarget writeBuffer, RenderTarget readBuffer, long ellapsedTime, double deltaTime) {
		if(mShadowPassType == ShadowPassType.APPLY_SHADOW_MAP) {
			mShadowMapMaterial.setShadowMapTexture(mShadowRenderTarget.getTexture());
			super.render(scene, renderer, screenQuad, writeBuffer, readBuffer, ellapsedTime, deltaTime);
		} else {
            renderer.setOverrideViewportDimensions(mShadowMapSize, mShadowMapSize);
			super.render(scene, renderer, screenQuad, mShadowRenderTarget, readBuffer, ellapsedTime, deltaTime);
            renderer.clearOverrideViewportDimensions();
		}
	}
	
	public ShadowMapMaterial getShadowMapMaterial() {
		return mShadowMapMaterial;
	}
	
	public void setShadowMapMaterial(ShadowMapMaterial shadowMapMaterial) {
		mShadowMapMaterial = shadowMapMaterial;
	}
}

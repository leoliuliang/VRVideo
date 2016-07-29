/**
 * Copyright 2013 Dennis Ippel
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.r3d.postprocessing;

import android.graphics.Bitmap.Config;
import android.opengl.GLES20;

import org.r3d.materials.textures.ATexture;
import org.r3d.materials.textures.ATexture.FilterType;
import org.r3d.materials.textures.ATexture.WrapType;
import org.r3d.postprocessing.IPass.PassType;
import org.r3d.postprocessing.IPostProcessingComponent.PostProcessingComponentType;
import org.r3d.postprocessing.passes.CopyPass;
import org.r3d.postprocessing.passes.EffectPass;
import org.r3d.primitives.ScreenQuad;
import org.r3d.renderer.RRenderer;
import org.r3d.renderer.RenderTarget;
import org.r3d.scene.RScene;
import org.r3d.scenegraph.IGraphNode.GRAPH_TYPE;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostProcessingManager {

	protected RRenderer mRenderer;
	protected RenderTarget mRenderTarget1;
	protected RenderTarget mRenderTarget2;
	public RenderTarget mReadBuffer;
	public RenderTarget mWriteBuffer;

	protected List<IPostProcessingComponent> mComponents;
	protected List<IPass> mPasses;
	protected boolean mComponentsDirty = false;
	protected int mNumPasses;
	protected int mWidth;
	protected int mHeight;
	
	protected EffectPass mCopyPass;

	protected ScreenQuad mScreenQuad;
	protected RScene mScene;

	public PostProcessingManager(RRenderer renderer) {
		this(renderer, -1, -1);
	}
	
	public PostProcessingManager(RRenderer renderer, int width, int height) {
		mRenderer = renderer;

		if(width == -1 && height == -1) {
			width = mRenderer.getViewportWidth();
    		height = mRenderer.getViewportHeight();
		}

		mWidth = width;
		mHeight = height;
		
		mScreenQuad = new ScreenQuad();
		mScene = new RScene(mRenderer, GRAPH_TYPE.NONE);

		mRenderTarget1 = new RenderTarget("rt1" + hashCode(), width, height, 0, 0,
				false, false, GLES20.GL_TEXTURE_2D, Config.ARGB_8888,
				FilterType.LINEAR, WrapType.CLAMP);
		mRenderTarget2 = new RenderTarget("rt2" + hashCode(), width, height, 0, 0,
				false, false, GLES20.GL_TEXTURE_2D, Config.ARGB_8888,
				FilterType.LINEAR, WrapType.CLAMP);
		
		mWriteBuffer = mRenderTarget1;
		mReadBuffer = mRenderTarget2;

		mCopyPass = new EffectPass(new CopyPass());
		mComponents = Collections.synchronizedList(new CopyOnWriteArrayList<IPostProcessingComponent>());
		mPasses = Collections.synchronizedList(new CopyOnWriteArrayList<IPass>());

		mRenderer.addRenderTarget(mWriteBuffer);
		mRenderer.addRenderTarget(mReadBuffer);
		
		mScene.addChild(mScreenQuad);
		mRenderer.addScene(mScene);
	}

	/**
	 * Swaps read and write buffers.
	 */
	public void swapBuffers() {
		RenderTarget tmp = mReadBuffer;
		mReadBuffer = mWriteBuffer;
		mWriteBuffer = tmp;
	}

	public void addPass(IPass pass) {
		mComponents.add(pass);
		mComponentsDirty = true;
	}
	
	public void addEffect(IPostProcessingEffect multiPass) {
		multiPass.initialize(mRenderer);
		mComponents.addAll(multiPass.getPasses());
		mComponentsDirty = true;
	}

	public void insertPass(int index, IPass pass) {
		mComponents.add(index, pass);
		mComponentsDirty = true;
	}
	
	public void insertEffect(int index, IPostProcessingEffect multiPass) {
		multiPass.initialize(mRenderer);
		mComponents.addAll(index, multiPass.getPasses());
		mComponentsDirty = true;
	}

	public void removePass(IPass pass) {
		mComponents.remove(pass);
		mComponentsDirty = true;
	}
	
	public void removeEffect(IPostProcessingEffect multiPass) {
		mComponents.removeAll(multiPass.getPasses());
		mComponentsDirty = true;
	}
	
	public void setSize(int width, int height) {
		mRenderTarget1.setWidth(width);
		mRenderTarget1.setHeight(height);
		mRenderTarget2.setWidth(width);
		mRenderTarget2.setHeight(height);
		
		for(IPass pass : mPasses) {
			pass.setSize(width, height);
		}
		
		//reset(renderTarget);
		mWidth = width;
		mHeight = height;
	}

	public void reset(RenderTarget renderTarget) {
	}

	public void render(long ellapsedTime, double deltaTime) {
		if(mComponentsDirty)
		{
			updatePassesList();
			mComponentsDirty = false;
		}
		
		mWriteBuffer = mRenderTarget1;
		mReadBuffer = mRenderTarget2;
		
		boolean maskActive = false;

		IPass pass;

		for (int i = 0; i < mNumPasses; i++) {
			pass = mPasses.get(i);
			if (!pass.isEnabled())
				continue;

			PassType type = pass.getPassType();
			
			pass.render(type == PassType.RENDER || type == PassType.DEPTH ? mRenderer.getCurrentScene() : mScene, mRenderer,
					mScreenQuad, mWriteBuffer, mReadBuffer, ellapsedTime, deltaTime);

			if (pass.needsSwap() && i < mNumPasses - 1) {
				if (maskActive) {
					GLES20.glStencilFunc(GLES20.GL_NOTEQUAL, 1, 0xffffffff);

					mCopyPass.render(mScene, mRenderer, mScreenQuad, mWriteBuffer, mReadBuffer, ellapsedTime, deltaTime);

					GLES20.glStencilFunc(GLES20.GL_EQUAL, 1, 0xffffffff);
				}

				swapBuffers();
			}

			// If the current pass is a mask pass, notify the next pass that mask is active.
			if (type == PassType.MASK)
				maskActive = true;
			else if (type == PassType.CLEAR)
				maskActive = false;
		}
	}
	
	public ATexture getTexture() {
		return mWriteBuffer.getTexture();
	}
	
	private void updatePassesList()
	{
		mPasses.clear();
		
		for(int i=0; i<mComponents.size(); i++)
		{
			IPostProcessingComponent component = mComponents.get(i);
			if(component.getType() == PostProcessingComponentType.PASS)
			{
				mPasses.add((IPass)component);
			}
			else if(component.getType() == PostProcessingComponentType.EFFECT)
			{
				IPostProcessingEffect effect = (IPostProcessingEffect)component;
				mPasses.addAll(effect.getPasses());
			}
		}
		
		mNumPasses = mPasses.size();
	}

	public boolean isEmpty() {
		return mComponents.isEmpty();
	}

	public RScene getScene() {
		return mScene;
	}
	
	protected void setComponentsDirty()
	{
		mComponentsDirty = true;
	}
}

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

import org.r3d.materials.Material;
import org.r3d.primitives.ScreenQuad;
import org.r3d.renderer.RRenderer;
import org.r3d.renderer.RenderTarget;
import org.r3d.scene.RScene;


public interface IPass extends IPostProcessingComponent {
	public static enum PassType {
		RENDER, DEPTH, EFFECT, MASK, CLEAR, MULTIPASS
	};
	
	boolean isClear();
	boolean needsSwap();
	void render(RScene scene, RRenderer renderer, ScreenQuad screenQuad, RenderTarget writeTarget, RenderTarget readTarget, long ellapsedTime, double deltaTime);
	PassType getPassType();
	void setMaterial(Material material);
	void setRenderToScreen(boolean renderToScreen);
	public void setWidth(int width);
	public int getWidth();
	public void setHeight(int height);
	public int getHeight();
	public void setSize(int width, int height);
}

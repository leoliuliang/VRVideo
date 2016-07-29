package org.r3d.loader.awd;

import org.r3d.loader.LoaderAWD.AWDLittleEndianDataInputStream;
import org.r3d.loader.LoaderAWD.BlockHeader;
import org.r3d.loader.awd.exceptions.NotImplementedParsingException;

/**
 * 
 * @author Ian Thomas (toxicbakery@gmail.com)
 * 
 */
public class BlockTextureProjector extends ABlockParser {

	public void parseBlock(AWDLittleEndianDataInputStream dis, BlockHeader blockHeader) throws Exception {
		throw new NotImplementedParsingException();
	}

}

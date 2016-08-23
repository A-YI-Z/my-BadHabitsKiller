package com.curry.bhk.bhk.bean;

import java.io.Serializable;

/**
 *  picture object
 */
public class ImageItem implements Serializable
{
//	private static final long serialVersionUID = -7188270558443739436L;
	public String imageId;
	public String thumbnailPath;
	public String sourcePath;
	public boolean isSelected = false;
}

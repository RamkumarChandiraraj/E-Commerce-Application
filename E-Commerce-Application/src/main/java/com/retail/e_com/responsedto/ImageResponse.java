package com.retail.e_com.responsedto;

import com.retail.e_com.enums.ImageType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ImageResponse {
	private int imageId;
	private String image;
	private ImageType imageType;
}

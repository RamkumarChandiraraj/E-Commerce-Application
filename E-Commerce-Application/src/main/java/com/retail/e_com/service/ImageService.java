package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.retail.e_com.utility.SimpleResponseStructure;

public interface ImageService {

	ResponseEntity<SimpleResponseStructure> addImage(int productId, String imageType, MultipartFile image);

	ResponseEntity<byte[]> findById(String imageId);

	ResponseEntity<byte[]> findByproductId(int productId);

}

package com.retail.e_com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.retail.e_com.enums.ImageType;
import com.retail.e_com.model.Image;

public interface ImageRepository extends MongoRepository<Image, String> {
	boolean existsByProductIdAndImageType(int productId, ImageType imageType);

	Optional<Image> findByProductId(int productId);

	Optional<Image> findByProductIdAndImageType(int productId, ImageType cover);

	Optional<List<Image>> findImageIdsByProductIdAndImageType(int productId, ImageType imageType);

	Optional<Image> findImageIdByProductIdAndImageType(int productId, ImageType imageType);
}

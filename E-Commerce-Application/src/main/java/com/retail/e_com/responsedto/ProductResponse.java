package com.retail.e_com.responsedto;

import java.util.List;
import java.util.Optional;

import com.retail.e_com.enums.Category;
import com.retail.e_com.enums.ProductAvailabilityStatus;
import com.retail.e_com.model.Product.ProductBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponse {
	private int productId;
	private String productName;
	private String productDescription;
	private int productPrice;
	private int productQuantity;
	private Category category;
	private ProductAvailabilityStatus availabilityStatus;
	private Optional<String> coverImage;
	private Optional<List<String>> normalImage;
}

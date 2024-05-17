package com.retail.e_com.requestdto;

import com.retail.e_com.enums.Category;
import com.retail.e_com.enums.ProductAvailabilityStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
	@NotNull(message = "product name is requred")
	@NotBlank(message = "product name is required")
	private String productName;
	private String productDescription;
	@NotNull(message = "product price is requred")
	@NotBlank(message = "product price is required")
	private int productPrice;
	private int productQuantity;
	private Category category;
	private ProductAvailabilityStatus availabilityStatus;
}

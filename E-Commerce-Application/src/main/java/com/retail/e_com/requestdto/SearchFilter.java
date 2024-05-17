package com.retail.e_com.requestdto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchFilter {
	private int minPrice;
	private int maxPrice;
	private String category;

	private int rating;
	private int discount;
	private String availabilityStatus;
}

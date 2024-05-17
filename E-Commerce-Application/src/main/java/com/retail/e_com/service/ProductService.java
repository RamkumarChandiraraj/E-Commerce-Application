package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.enums.OrderBy;
import com.retail.e_com.requestdto.ProductRequest;
import com.retail.e_com.requestdto.SearchFilter;
import com.retail.e_com.responsedto.ProductResponse;
import com.retail.e_com.utility.ResponseStructure;

public interface ProductService {

	ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest);

	ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(ProductRequest productRequest, int productId);

	ResponseEntity<?> findProducts();

	ResponseEntity<ResponseStructure<ProductResponse>> findByProductId(int productId);

	ResponseEntity<?> searchString(String serachText);

	ResponseEntity<?> findProductByFilter(SearchFilter searchFilter, int i, OrderBy orderBy, String sortBy);

}

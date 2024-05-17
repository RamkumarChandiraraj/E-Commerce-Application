package com.retail.e_com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.enums.OrderBy;
import com.retail.e_com.repository.ProductRepository;
import com.retail.e_com.requestdto.ProductRequest;
import com.retail.e_com.requestdto.SearchFilter;
import com.retail.e_com.responsedto.ProductResponse;
import com.retail.e_com.service.ProductService;
import com.retail.e_com.utility.ResponseStructure;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/fk/pr")
@CrossOrigin(allowCredentials = "true",origins = "http://localhost:5173")
public class ProductController {

	private ProductService productService;
	private ProductRepository productRepository;

	@PreAuthorize("hasAuthority('SELLER')")
	@PostMapping("/products")
	public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(@RequestBody ProductRequest productRequest){
		System.out.println(productRequest.getProductName());
		System.out.println("Hii");
		return productService.addProduct(productRequest);
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@PutMapping("/products/{productId}")
	public ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(@RequestBody ProductRequest productRequest,@PathVariable int productId){
		return productService.updateProduct(productRequest,productId);
	}

	@GetMapping("/all/products")
	public ResponseEntity<?> findProducts(){
		return productService.findProducts();
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<ResponseStructure<ProductResponse>> findByProductId(@PathVariable int productId){
		return productService.findByProductId(productId);
	}

	@GetMapping("/findProduct")
	public ResponseEntity<?> findProductByFilter(SearchFilter searchFilter
			, Integer  page
			, OrderBy orderBy
			, String  sortBy) {
		System.out.println(searchFilter.getCategory());
		System.out.println(orderBy+"  rrr");
		return productService.findProductByFilter(searchFilter,0,orderBy,sortBy);
	}

	@GetMapping("/products")
	public ResponseEntity<?>searchString(@RequestParam  String serachText){
		return productService.searchString(serachText);
	}
}

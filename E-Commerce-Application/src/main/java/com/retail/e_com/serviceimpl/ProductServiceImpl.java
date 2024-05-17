package com.retail.e_com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.retail.e_com.controller.ImageController;
import com.retail.e_com.enums.OrderBy;
import com.retail.e_com.exception.ProductNotFoundByIdException;
import com.retail.e_com.model.Product;
import com.retail.e_com.repository.ProductRepository;
import com.retail.e_com.requestdto.ProductRequest;
import com.retail.e_com.requestdto.SearchFilter;
import com.retail.e_com.responsedto.ProductResponse;
import com.retail.e_com.service.ProductService;
import com.retail.e_com.utility.ProductSpecification;
import com.retail.e_com.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

	private ProductRepository productRepository;
	private ResponseStructure<ProductResponse> response;
	ResponseStructure<List<ProductResponse>> responseStructure;
	private ImageController controller;
	private ProductSpecification productSpecification;

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest) {

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.err.println(name);
		Product saveProduct=productRepository.save(mapToProduct(productRequest));
		return ResponseEntity.ok(response.setStatusCode(HttpStatus.CREATED.value())
				.setMessage("product added successfully")
				.setData(mapToProductResponse(saveProduct)));
	}

	private ProductResponse mapToProductResponse(Product saveProduct) {
		return ProductResponse.builder()
				.productId(saveProduct.getProductId())
				.productName(saveProduct.getProductName())
				.productDescription(saveProduct.getProductDescription())
				.productPrice(saveProduct.getProductPrice())
				.productQuantity(saveProduct.getProductQuantity())
				.category(saveProduct.getCategory())
				.availabilityStatus(saveProduct.getAvailabilityStatus())
				//				.images(saveProduct)
				.build();
	}

	private Product mapToProduct(ProductRequest productRequest) {
		return Product.builder()
				.productName(productRequest.getProductName())
				.productDescription(productRequest.getProductDescription())
				.productPrice(productRequest.getProductPrice())
				.productQuantity(productRequest.getProductQuantity())
				.category(productRequest.getCategory())
				.availabilityStatus(productRequest.getAvailabilityStatus())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(ProductRequest productRequest,
			int productId) {

		return productRepository.findById(productId).map(existingProduct -> {
			Product updatedProduct=mapToProduct(productRequest);
			updatedProduct.setProductId(productId);
			productRepository.save(updatedProduct);

			return ResponseEntity.ok(response
					.setMessage("Product updated succsfully")
					.setStatusCode(HttpStatus.OK.value())
					.setData(mapToProductResponse(updatedProduct)));
		}).orElseThrow(()->new ProductNotFoundByIdException("Product is not present by this id"));
	}

	@Override
	public ResponseEntity<?> findProducts() {
		List<Product> products = productRepository.findAll(); 
		List<ProductResponse> productResponses = convertToProductResponses(products);

		return ResponseEntity.ok().body(responseStructure.setData(productResponses));

		//       return ResponseEntity.ok(response.setStatuscode(HttpStatus.CREATED.value())
		//				.setMessage("product added successfully")
		//				.setData(mapToProductResponse(productResponses)));;
	}

	private List<ProductResponse> convertToProductResponses(List<Product> products) {
		List<ProductResponse> responses = new ArrayList<>();
		for (Product product : products) {
			responses.add(mapToProductResponse(product));
		}
		return responses;
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> findByProductId(int productId) {
		return productRepository.findById(productId).map(Product -> {
			return ResponseEntity.ok(response
					.setStatusCode(HttpStatus.OK.value())
					.setMessage("Product found successfully")
					.setData(mapToProductResponse(Product)));
		}).orElseThrow(()-> new ProductNotFoundByIdException("Product is not present by this id"));
	}

	@Override
	public ResponseEntity<?> findProductByFilter(SearchFilter searchFilter,int page,OrderBy orderBy,String sortBy) {
		Specification<Product> specification = productSpecification.buildSpecification(searchFilter);
		Pageable pageable= (Pageable)PageRequest.of(page, 30,Sort.by(OrderBy.DESC ==orderBy ? Sort.Direction.DESC : Sort.Direction.ASC,sortBy));
		Page<Product> page2 = productRepository.findAll(specification,pageable); 
		List<Product> products = page2.getContent();
		List<ProductResponse> productResponses = convertToProductResponses(products);
		return ResponseEntity.ok().body(responseStructure.setData(productResponses));
	}

	@Override
	public ResponseEntity<?> searchString(String serachText) {
		List<ProductResponse> products=productRepository.findByproductNameContainingOrProductDescriptionContaining(serachText,serachText)
				.stream().map(this::mapToProductResponse)
				.toList();
		return ResponseEntity.status(HttpStatus.FOUND.value())
				.body(new ResponseStructure<>().setStatusCode(HttpStatus.FOUND.value())
						.setMessage("the product is found")
						.setData(products));
	}
}

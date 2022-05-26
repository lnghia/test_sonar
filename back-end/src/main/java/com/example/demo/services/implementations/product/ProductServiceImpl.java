package com.example.demo.services.implementations.product;

import com.example.demo.dto.requests.product.PageableProductListResponseDto;
import com.example.demo.dto.responses.product.ProductResponseDto;
import com.example.demo.dto.responses.user.PageableUserRateProductResponseDto;
import com.example.demo.dto.responses.user.UserRateProductResponseDto;
import com.example.demo.entities.ProductEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.entities.UserRateProductEntity;
import com.example.demo.exceptions.ProductNotFoundException;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRateProductRepository;
import com.example.demo.services.interfaces.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ModelMapper modelMapper;

  private final ProductRepository productRepository;

  private final UserRateProductRepository userRateProductRepository;

  @Override
  public ProductResponseDto findById(long id) {
    Optional<ProductEntity> productEntity = productRepository.findById(id);

    if (productEntity.isEmpty()) {
      throw new ProductNotFoundException();
    }

    return modelMapper.map(productEntity.orElse(null), ProductResponseDto.class);
  }

  @Override
  public PageableProductListResponseDto findAllWithFilterAndSort(
      List<Long> categoryIds,
      List<Long> genderIds,
      List<Long> sportIds,
      List<Long> technologyIds,
      String name,
      int page,
      int size,
      String sortType,
      String sortBy) {
    Pageable pageable;
    if (sortType != null && sortBy != null && !sortType.isEmpty() && !sortBy.isEmpty()) {
      pageable =
          PageRequest.of(
              page,
              size,
              Sort.by(sortType.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
    } else {
      pageable = PageRequest.of(page, size);
    }

    List<String> categoryIdsInString =
        categoryIds != null
            ? categoryIds.stream().map(Object::toString).collect(Collectors.toList())
            : new ArrayList<>();
    List<String> technologyIdsInString =
        technologyIds != null
            ? technologyIds.stream().map(Object::toString).collect(Collectors.toList())
            : new ArrayList<>();
    List<String> genderIdsInString =
        genderIds != null
            ? genderIds.stream().map(Object::toString).collect(Collectors.toList())
            : new ArrayList<>();
    List<String> sportIdsInString =
        sportIds != null
            ? sportIds.stream().map(Object::toString).collect(Collectors.toList())
            : new ArrayList<>();

    Page<ProductEntity> productEntities =
        productRepository.findAllFilter(
            categoryIdsInString,
            genderIdsInString,
            sportIdsInString,
            technologyIdsInString,
            name != null ? name : "",
            pageable);

    return modelMapper.map(productEntities, PageableProductListResponseDto.class);
  }

  @Override
  public List<ProductResponseDto> getAll() {
    List<ProductEntity> productEntities = productRepository.findAll();

    return productEntities.stream()
        .map(product -> modelMapper.map(product, ProductResponseDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public PageableUserRateProductResponseDto getLatestCommentUserProduct(
      Long productId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<UserRateProductEntity> userRateProductEntities =
        userRateProductRepository.getLatestRatings(productId, pageable);

    return modelMapper.map(userRateProductEntities, PageableUserRateProductResponseDto.class);
  }

  @Override
  public UserRateProductResponseDto findReviewOfUserOnProduct(
      UserEntity userEntity, Long productId) {
    Optional<UserRateProductEntity> optionalResult =
        userRateProductRepository.findByUserIdProductId(userEntity.getId(), productId);

    UserRateProductEntity result = optionalResult.orElse(null);

    return modelMapper.map(result, UserRateProductResponseDto.class);
  }
}

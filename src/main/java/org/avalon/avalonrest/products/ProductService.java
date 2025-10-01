package org.avalon.avalonrest.products;

import org.avalon.avalonrest.images.FailedToUploadObjectToS3Exception;
import org.avalon.avalonrest.images.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final S3Service s3Service;
    private final ProductRepository productRepository;

    public ProductService(S3Service s3Service, ProductRepository productRepository) {
        this.s3Service = s3Service;
        this.productRepository = productRepository;
    }

    public Optional<ProductEntity> save(
            String nameBG,
            String descriptionBG,
            String nameEN,
            String descriptionEN,
            MultipartFile image,
            Double price,
            String currency,
            String productType,
            Integer quantity
    ) {
        try {
            UUID id = UUID.randomUUID();
            UUID imageId = UUID.randomUUID();
            String imageUrl = s3Service.uploadFile(imageId.toString(), image);
            return productRepository.save(
                    id,
                    nameBG,
                    descriptionBG,
                    nameEN,
                    descriptionEN,
                    imageUrl,
                    BigDecimal.valueOf(price),
                    Currency.fromCode(currency),
                    ProductType.fromCode(productType),
                    quantity
            );
        } catch (IOException | NoSuchAlgorithmException | FailedToUploadObjectToS3Exception ex) {
            throw new RuntimeException("Failed to upload image", ex);
        }
    }

    public Optional<ProductEntity> findById(UUID id) {
        return productRepository.findById(id);
    }

    public List<ProductEntity> findAll(
            int page,
            int size,
            String sortBy,
            String direction,
            String nameFilter
    ) {
        return productRepository.findAll(
                page,
                size,
                sortBy,
                direction,
                nameFilter
        );
    }

    public Optional<ProductEntity> update(
            UUID id,
            String nameBG,
            String descriptionBG,
            String nameEN,
            String descriptionEN,
            MultipartFile image,
            Double price,
            String currency,
            String productType,
            Integer quantity
    ) {
        try {
            Optional<ProductEntity> product = productRepository.update(
                    id,
                    nameBG,
                    descriptionBG,
                    nameEN,
                    descriptionEN,
                    BigDecimal.valueOf(price),
                    Currency.fromCode(currency),
                    ProductType.fromCode(productType),
                    quantity
            );
            if (product.isEmpty()) {
                return Optional.empty();
            }
            String[] parts = product.get().getImageUrl().split("/");
            String imageId = parts[parts.length - 1];
            s3Service.uploadFile(imageId, image);
            return product;
        } catch (IOException | NoSuchAlgorithmException | FailedToUploadObjectToS3Exception ex) {
            throw new FailedToUploadObjectToS3Exception("Failed to upload image " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to update product " + ex.getMessage(), ex);
        }
    }

    public Optional<ProductEntity> deleteById(UUID id) {
        return productRepository.deleteById(id);
    }
}
package org.avalon.avalonrest.products;

import jakarta.validation.ConstraintViolationException;
import org.avalon.avalonrest.exceptions.ApiException;
import org.avalon.avalonrest.utils.validators.EntityValidator;
import org.avalon.avalonrest.utils.LocaleHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/products")
public class ProductController {
    private final EntityValidator validator;
    private final ProductService productService;

    public ProductController(EntityValidator validator, ProductService productService) {
        this.validator = validator;
        this.productService = productService;
    }

    @PostMapping(
            value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestHeader(name = "Accept-Language", required = false) String localeString,
            @ModelAttribute ProductCreateRequestDTO dto
    ) {
        try {
            Locale locale = LocaleHandler.resolve(localeString);
            validator.validate(dto);
            var product = productService.save(
                    dto.getNameBG(),
                    dto.getDescriptionBG(),
                    dto.getNameEN(),
                    dto.getDescriptionEN(),
                    dto.getImage(),
                    dto.getPrice(),
                    dto.getCurrency(),
                    dto.getProductType(),
                    dto.getQuantity()
            );
            if (product.isEmpty()) {
                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to create a product"
                );
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ProductResponseDTO(locale, product.get()));
        } catch (ConstraintViolationException ex) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage()
            );
        } catch (Exception ex) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @RequestHeader(name = "Accept-Language", required = false) String localeString,
            @PathVariable UUID id
    ) {
        try {
            Locale locale = LocaleHandler.resolve(localeString);
            Optional<ProductEntity> product = productService.findById(id);
            if (product.isEmpty()) {
                throw new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                );
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ProductResponseDTO(locale, product.get()));
        } catch (Exception ex) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestHeader(name = "Accept-Language", required = false) String localeString,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "price") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "nameFilter", required = false) String nameFilter
    ) {
        try {
            Locale locale = LocaleHandler.resolve(localeString);
            List<ProductResponseDTO> products = productService.findAll(
                            page,
                            size,
                            sortBy,
                            direction,
                            nameFilter
                    ).stream()
                    .map(product -> new ProductResponseDTO(locale, product))
                    .toList();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(products);
        } catch (Exception ex) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }

    @PutMapping(
            value = "/{id}",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE
            }
    )
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @RequestHeader(name = "Accept-Language", required = false) String localeString,
            @PathVariable UUID id,
            @ModelAttribute ProductUpdateRequestDTO dto
    ) {
        try {
            Locale locale = LocaleHandler.resolve(localeString);
            validator.validate(dto);
            Optional<ProductEntity> product = productService.update(
                    id,
                    dto.getNameBG(),
                    dto.getDescriptionBG(),
                    dto.getNameEN(),
                    dto.getDescriptionEN(),
                    dto.getImage(),
                    dto.getPrice(),
                    dto.getCurrency(),
                    dto.getProductType(),
                    dto.getQuantity()
            );
            if (product.isEmpty()) {
                throw new ApiException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to update a product"
                );
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ProductResponseDTO(locale, product.get()));
        } catch (ConstraintViolationException ex) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage()
            );
        } catch (Exception ex) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(
            @RequestHeader(name = "Accept-Language", required = false) String localeString,
            @PathVariable UUID id
    ) {
        try {
            Locale locale = LocaleHandler.resolve(localeString);
            Optional<ProductEntity> product = productService.deleteById(id);
            if (product.isEmpty()) {
                throw new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                );
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ProductResponseDTO(locale, product.get()));
        } catch (Exception ex) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }
}
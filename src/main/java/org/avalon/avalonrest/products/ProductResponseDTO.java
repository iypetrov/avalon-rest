package org.avalon.avalonrest.products;

import java.util.Locale;
import java.util.UUID;

public class ProductResponseDTO {
    private final UUID id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final Double price;
    private final String currency;
    private final String productType;
    private final Integer quantity;
    private final String createdAt;
    private final String updatedAt;

    public ProductResponseDTO(Locale locale, ProductEntity product) {
        this.id = product.getId();
        this.imageUrl = product.getImageUrl();
        this.price = product.getPrice().doubleValue();
        this.currency = product.getCurrency();
        this.quantity = product.getQuantity();
        this.productType = product.getProductType();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();

        if (locale.equals(Locale.forLanguageTag("bg"))) {
            this.name = product.getNameBG();
            this.description = product.getDescriptionBG();
        } else {
            this.name = product.getNameEN();
            this.description = product.getDescriptionEN();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getProductType() {
        return productType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
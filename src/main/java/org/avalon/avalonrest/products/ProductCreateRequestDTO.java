package org.avalon.avalonrest.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.avalon.avalonrest.utils.validators.EnumValue;
import org.springframework.web.multipart.MultipartFile;

public class ProductCreateRequestDTO {
    @NotNull
    private String nameBG;

    private String descriptionBG;

    @NotNull
    private String nameEN;

    private String descriptionEN;

    private MultipartFile image;

    @NotNull
    @DecimalMin("0.0")
    private Double price;

    @NotNull
    @EnumValue(enumClass = Currency.class, message = "Invalid currency")
    private String currency;

    @NotNull
    @EnumValue(enumClass = ProductType.class, message = "Invalid product type")
    private String productType;

    @NotNull
    @Min(0)
    private Integer quantity;

    public ProductCreateRequestDTO() {
    }

    public String getNameBG() {
        return nameBG;
    }

    public void setNameBG(String nameBG) {
        this.nameBG = nameBG;
    }

    public String getDescriptionBG() {
        return descriptionBG;
    }

    public void setDescriptionBG(String descriptionBG) {
        this.descriptionBG = descriptionBG;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
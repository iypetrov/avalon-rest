package org.avalon.avalonrest.products;

public enum ProductType {
    SUPPLEMENT("SUPPLEMENT"),
    COSMETIC("COSMETIC"),
    BABY_CARE("BABY_CARE"),
    OTHER("OTHER");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProductType fromCode(String name) {
        for (ProductType productType: ProductType.values()) {
            if (productType.getName().equalsIgnoreCase(name)) {
                return productType;
            }
        }
        throw new UnknownProductTypeException("Unknown product type: " + name);
    }
}
package org.avalon.avalonrest.products;

public enum Currency {
    BGN("BGN"),
    EUR("EUR");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Currency fromCode(String code) {
        for (Currency currency : Currency.values()) {
            if (currency.getName().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new UnknownCurrencyException("Unknown currency: " + code);
    }
}
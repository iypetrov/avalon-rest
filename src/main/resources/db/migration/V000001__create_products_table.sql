CREATE TYPE currency AS ENUM ('BGN', 'EUR');
CREATE TYPE product_type AS ENUM ('SUPPLEMENT', 'COSMETIC', 'BABY_CARE', 'OTHER');

CREATE TABLE products (
    id UUID PRIMARY KEY,
    name_bg TEXT NOT NULL,
    description_bg TEXT,
    name_en TEXT NOT NULL,
    description_en TEXT,
    image_url TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    currency currency NOT NULL,
    product_type product_type NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
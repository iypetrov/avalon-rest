package org.avalon.avalonrest.products;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public ProductRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<ProductEntity> save(
            UUID id,
            String nameBG,
            String descriptionBG,
            String nameEN,
            String descriptionEN,
            String imageUrl,
            BigDecimal price,
            Currency currency,
            ProductType productType,
            Integer quantity
    ) {
        String sql = """
                    INSERT INTO products (
                        id,
                        name_bg,
                        description_bg,
                        name_en,
                        description_en,
                        image_url,
                        price,
                        currency,
                        product_type,
                        quantity,
                        created_at,
                        updated_at    
                    )
                    VALUES (
                        :id,
                        :name_bg,
                        :description_bg,
                        :name_en,
                        :description_en,
                        :image_url,
                        :price,
                        CAST(:currency AS currency),
                        CAST(:product_type AS product_type),                        
                        :quantity,
                        :created_at,
                        :updated_at
                    )
                    RETURNING
                        id,
                        name_bg,
                        description_bg,
                        name_en,
                        description_en,
                        image_url,
                        price,
                        currency,
                        product_type,
                        quantity,
                        created_at, 
                        updated_at
                """;
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name_bg", nameBG)
                .addValue("description_bg", descriptionBG)
                .addValue("name_en", nameEN)
                .addValue("description_en", descriptionEN)
                .addValue("image_url", imageUrl)
                .addValue("price", price)
                .addValue("currency", currency.name())
                .addValue("product_type", productType.name())
                .addValue("quantity", quantity)
                .addValue("created_at", Timestamp.from(utcNow.toInstant()))
                .addValue("updated_at", Timestamp.from(utcNow.toInstant()));
        ProductEntity entity = jdbc.queryForObject(
                sql,
                params,
                new ProductEntityRowMapper()
        );
        return Optional.ofNullable(entity);
    }

    public Optional<ProductEntity> findById(UUID id) {
        String sql = """
                    SELECT
                        id,
                        name_bg,
                        description_bg,
                        name_en,
                        description_en,
                        image_url,
                        price,
                        currency,
                        product_type,
                        quantity,
                        created_at,
                        updated_at    
                    FROM products
                    WHERE id = :id
                """;
        ProductEntity entity = jdbc.queryForObject(
                sql,
                new MapSqlParameterSource("id", id),
                new ProductEntityRowMapper()
        );
        return Optional.ofNullable(entity);
    }

    public List<ProductEntity> findAll(
            int page,
            int size,
            String sortBy,
            String direction,
            String nameFilter
    ) {
        String sql = """
                    SELECT
                        id,
                        name_bg,
                        description_bg,
                        name_en,
                        description_en,
                        image_url,
                        price,
                        currency,
                        product_type,
                        quantity,
                        created_at,
                        updated_at    
                    FROM products
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name_filter", nameFilter)
                .addValue("like_filter", nameFilter != null ? "%" + nameFilter + "%" : null)
                .addValue("sort_by", sortBy != null ? sortBy : "price")
                .addValue("direction", "desc".equalsIgnoreCase(direction) ? "DESC" : "ASC")
                .addValue("limit", size)
                .addValue("offset", page * size);

        return jdbc.query(
                sql,
                params,
                new ProductEntityRowMapper()
        );
    }

    public Optional<ProductEntity> update(
            UUID id,
            String nameBG,
            String descriptionBG,
            String nameEN,
            String descriptionEN,
            BigDecimal price,
            Currency currency,
            ProductType productType,
            Integer quantity
    ) {
        String sql = """
                    UPDATE products SET
                        name_bg = :name_bg,
                        description_bg = :description_bg,
                        name_en = :name_en,
                        description_en = :description_en,
                        price = :price,
                        currency = CAST(:currency AS currency),
                        product_type = CAST(:product_type AS product_type),
                        quantity = :quantity,
                        updated_at = :updated_at
                    WHERE id = :id
                    RETURNING
                        id,
                        name_bg,
                        description_bg,
                        name_en,
                        description_en,
                        image_url,
                        price,
                        currency,
                        product_type,                        
                        quantity,
                        created_at,
                        updated_at    
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name_bg", nameBG)
                .addValue("description_bg", descriptionBG)
                .addValue("name_en", nameEN)
                .addValue("description_en", descriptionEN)
                .addValue("price", price)
                .addValue("currency", currency.name())
                .addValue("product_type", productType.name())
                .addValue("quantity", quantity)
                .addValue("updated_at", Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()));
        ProductEntity entity = jdbc.queryForObject(
                sql,
                params,
                new ProductEntityRowMapper()
        );
        return Optional.ofNullable(entity);
    }

    public Optional<ProductEntity> deleteById(UUID id) {
        String sql = """
                    DELETE FROM products
                    WHERE id = :id
                    RETURNING
                        id,
                        name_bg,
                        description_bg,
                        name_en,
                        description_en,
                        image_url,
                        price,
                        currency,
                        product_type,
                        quantity,
                        created_at,
                        updated_at    
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        ProductEntity entity = jdbc.queryForObject(
                sql,
                params,
                new ProductEntityRowMapper()
        );
        return Optional.ofNullable(entity);
    }
}
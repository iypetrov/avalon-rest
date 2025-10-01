package org.avalon.avalonrest.products;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductEntityRowMapper implements RowMapper<ProductEntity> {
    @Override
    public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductEntity entity = new ProductEntity();
        entity.setId(UUID.fromString(rs.getString("id")));
        entity.setNameBG(rs.getString("name_bg"));
        entity.setDescriptionBG(rs.getString("description_bg"));
        entity.setNameEN(rs.getString("name_en"));
        entity.setDescriptionEN(rs.getString("description_en"));
        entity.setImageUrl(rs.getString("image_url"));
        entity.setPrice(rs.getBigDecimal("price"));
        entity.setCurrency(rs.getString("currency"));
        entity.setProductType(rs.getString("product_type"));
        entity.setQuantity(rs.getInt("quantity"));
        entity.setCreatedAt(rs.getString("created_at"));
        entity.setUpdatedAt(rs.getString("updated_at"));
        return entity;
    }
}
package com.example.springwebtask.dao;

import com.example.springwebtask.entity.Product;
import com.example.springwebtask.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Product> findAll(){
        System.out.println("ProductDaoCheck(findAll)");
        return jdbcTemplate.query("SELECT product_id productId\n" +
                                        "       ,p.name productName\n" +
                                        "       ,price productPrice\n" +
                                        "       ,c.name categoryName\n" +
                                        "       ,description description\n" +
                                        " FROM products p\n" +
                                        " JOIN categories c\n" +
                                        " ON p.category_id = c.id\n" +
                                        " ORDER BY product_id",
                new DataClassRowMapper<>(Product.class));
    }

    public Product findById(String id){
        System.out.println("ProductDaoCheck(findById)");
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id",id);
        var list = jdbcTemplate.query("SELECT product_id productId\n" +
                                            "       ,p.name productName\n" +
                                            "       ,price productPrice\n" +
                                            "       ,c.name categoryName\n" +
                                            "       ,description description\n" +
                                            " FROM products p\n" +
                                            " JOIN categories c\n" +
                                            " ON p.category_id = c.id\n" +
                                            " WHERE product_id = :id\n" +
                                            " ORDER BY product_id", param,
                                    new DataClassRowMapper<>(Product.class));
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Product> findOrderSort(String where,String order){
        System.out.println("ProductDaoCheck(findOrderSort)");
        MapSqlParameterSource param = new MapSqlParameterSource();
        var list = jdbcTemplate.query("SELECT product_id productId\n" +
                                        "       ,p.name productName\n" +
                                        "       ,price productPrice\n" +
                                        "       ,c.name categoryName\n" +
                                        "       ,description description\n" +
                                        " FROM products p\n" +
                                        " JOIN categories c\n" +
                                        " ON p.category_id = c.id\n" +
                                        " ORDER BY " + where + " " + order, param, new DataClassRowMapper<>(Product.class));
        return list.isEmpty() ? null : list;
    }
    public List<Product> findNameSort(String[] name){
        System.out.println("ProductDaoCheck(findNameSort)");
        MapSqlParameterSource param = new MapSqlParameterSource();


        var SQL = "SELECT product_id productId\n" +
                "       ,p.name productName\n" +
                "       ,price productPrice\n" +
                "       ,c.name categoryName\n" +
                "       ,description description\n" +
                " FROM products p\n" +
                " JOIN categories c\n" +
                " ON p.category_id = c.id\n";
        var WHERE = " WHERE p.name LIKE :name0\n";
        var OR =" OR c.name LIKE :name0";
        var ORDER = " ORDER BY product_id";

        String[] nameSort = new String[name.length];
        for(var i = 0;i < name.length;i++){
            nameSort[i] = "%" + name[i] + "%";
        }
        for(var i = 1;i < name.length;i++){
            WHERE += " AND p.name LIKE :name" + i;
            OR += "AND c.name LIKE :name" + i;
        }
        for(var i = 0;i < nameSort.length;i++) {
            param.addValue("name" + i, nameSort[i]);
        }

        var list = jdbcTemplate.query(SQL + WHERE + OR + ORDER
                                            , param, new DataClassRowMapper<>(Product.class));
        return list.isEmpty() ? null : list;
    }
    public List<Product> findOrderNameSort(String where,String order,String[] name){
        System.out.println("ProductDaoCheck(findOrderNameSort)");
        MapSqlParameterSource param = new MapSqlParameterSource();

        var SQL = "SELECT product_id productId\n" +
                "       ,p.name productName\n" +
                "       ,price productPrice\n" +
                "       ,c.name categoryName\n" +
                "       ,description description\n" +
                " FROM products p\n" +
                " JOIN categories c\n" +
                " ON p.category_id = c.id\n";
        var WHERE = " WHERE p.name LIKE :name0\n";
        var OR =" OR c.name LIKE :name0";
        var ORDER = " ORDER BY " + where + " " + order;

        String[] nameSort = new String[name.length];
        for(var i = 0;i < name.length;i++){
            nameSort[i] = "%" + name[i] + "%";
        }
        for(var i = 1;i < name.length;i++){
            WHERE += " AND p.name LIKE :name" + i;
            OR += "AND c.name LIKE :name" + i;
        }
        for(var i = 0;i < nameSort.length;i++) {
            param.addValue("name" + i, nameSort[i]);
        }

        var list = jdbcTemplate.query(SQL + WHERE + OR + ORDER, param, new DataClassRowMapper<>(Product.class));
        return list.isEmpty() ? null : list;
    }



    public int insert(Product product) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", product.getProductId());
        param.addValue("name", product.getProductName());
        param.addValue("price", product.getProductPrice());
        param.addValue("categoryName", product.getCategoryName());
        param.addValue("description", product.getDescription());

        try{
            return jdbcTemplate.update("INSERT INTO products(product_id,category_id,name,price,description)\n" +
                    "VALUES(:id,(SELECT id\n" +
                    "             FROM categories\n" +
                    "             WHERE name = :categoryName),:name,:price,:description)", param);
        }catch (Exception e){
            return 0;
        }

    }

    public int update(Product product,String changeId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", product.getProductId());
        param.addValue("name", product.getProductName());
        param.addValue("price", product.getProductPrice());
        param.addValue("categoryName", product.getCategoryName());
        param.addValue("description", product.getDescription());
        param.addValue("changeId",changeId);

        try{
            return jdbcTemplate.update("UPDATE products SET product_id = :id\n" +
                    "                    ,category_id = (SELECT id\n" +
                    "                                     FROM categories\n" +
                    "                                     WHERE name = :categoryName)\n" +
                    "                    ,name = :name\n" +
                    "                    ,price = :price\n" +
                    "                    ,description = :description\n" +
                    " WHERE product_id = :changeId", param);
        }catch (Exception e){
            return 0;
        }

    }

    public int delete(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        try{
            return jdbcTemplate.update("DELETE FROM products WHERE product_id = :id", param);
        }catch (Exception e){
            return 0;
        }

    }

}

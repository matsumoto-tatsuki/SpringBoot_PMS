package com.example.springwebtask.dao;

import com.example.springwebtask.entity.Category;
import com.example.springwebtask.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Category> findAll(){
        System.out.println("CategoryDaoCheck(findAll)");
        return jdbcTemplate.query("SELECT id categoryId,name categoryName\n" +
                                    "FROM categories\n" +
                                    "ORDER BY id;",
                new DataClassRowMapper<>(Category.class));
    }
}

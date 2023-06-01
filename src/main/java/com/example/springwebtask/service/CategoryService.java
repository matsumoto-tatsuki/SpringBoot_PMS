package com.example.springwebtask.service;

import com.example.springwebtask.dao.CategoryDao;
import com.example.springwebtask.dao.ProductDao;
import com.example.springwebtask.entity.Category;
import com.example.springwebtask.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    public List<Category> findAll() {
        return categoryDao.findAll();
    }
}

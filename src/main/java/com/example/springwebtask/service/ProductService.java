package com.example.springwebtask.service;

import com.example.springwebtask.dao.ProductDao;
import com.example.springwebtask.dao.UserDao;
import com.example.springwebtask.entity.Product;
import com.example.springwebtask.entity.User;
import com.example.springwebtask.from.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public List<Product> findOrderSort(String where, String order) {
        return productDao.findOrderSort(where,order);
    }
    public List<Product> findNameSort(String name) {
        return productDao.findNameSort(name);
    }
    public List<Product> findOrderNameSort(String where, String order, String name) {
        return productDao.findOrderNameSort(where, order, name);
    }

    public Product findById(String id) {
        return productDao.findById(id);
    }



    public int insert(Product product) {
        return productDao.insert(product);
    }


    public int update(Product product,String changeId) {
        return productDao.update(product,changeId);
    }

    public int delete(String id) {
        return productDao.delete(id);
    }
}

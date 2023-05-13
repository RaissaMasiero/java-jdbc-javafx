package com.jdbcfx.javafxcomjdbc.model.dao;

import com.jdbcfx.javafxcomjdbc.model.entities.Department;
import com.jdbcfx.javafxcomjdbc.model.entities.Seller;

import java.util.List;

public interface SellerDAO {

    void insert(Seller s);
    void update(Seller s);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}

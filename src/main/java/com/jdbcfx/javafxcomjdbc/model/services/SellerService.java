package com.jdbcfx.javafxcomjdbc.model.services;

import com.jdbcfx.javafxcomjdbc.model.dao.DaoFactory;
import com.jdbcfx.javafxcomjdbc.model.dao.SellerDAO;
import com.jdbcfx.javafxcomjdbc.model.entities.Seller;

import java.util.List;

public class SellerService {

    private SellerDAO dao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }else{
            dao.update(obj);
        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}

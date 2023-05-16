package com.jdbcfx.javafxcomjdbc.model.services;

import com.jdbcfx.javafxcomjdbc.model.dao.DaoFactory;
import com.jdbcfx.javafxcomjdbc.model.dao.DepartmentDAO;
import com.jdbcfx.javafxcomjdbc.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDAO dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }else{
            dao.update(obj);
        }
    }

    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}

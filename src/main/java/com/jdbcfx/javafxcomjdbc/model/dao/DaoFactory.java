package com.jdbcfx.javafxcomjdbc.model.dao;

import com.jdbcfx.javafxcomjdbc.db.DB;
import com.jdbcfx.javafxcomjdbc.model.dao.impl.DepartmentDaoJDBC;
import com.jdbcfx.javafxcomjdbc.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDAO createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDAO createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}

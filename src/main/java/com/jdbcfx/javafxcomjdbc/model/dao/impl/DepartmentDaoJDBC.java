package com.jdbcfx.javafxcomjdbc.model.dao.impl;

import com.jdbcfx.javafxcomjdbc.db.DB;
import com.jdbcfx.javafxcomjdbc.db.DbException;
import com.jdbcfx.javafxcomjdbc.model.dao.DepartmentDAO;
import com.jdbcfx.javafxcomjdbc.model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDAO {

    private Connection connection;

    public DepartmentDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Department d) {
        PreparedStatement st = null;

        try{
            st = connection.prepareStatement("INSERT INTO department (Name) VALUES (?)",
                                                 Statement.RETURN_GENERATED_KEYS);

            st.setString(1, d.getName());
            int linhasAfetadas = st.executeUpdate();

            if(linhasAfetadas > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    d.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Erro inesperado! Nenhuma linha afetada!");
            }

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department d) {
        PreparedStatement st = null;

        try{
            st = connection.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            st.setString(1, d.getName());
            st.setInt(2, d.getId());
            st.executeUpdate();

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try{
            st = connection.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1, id);
            st.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = connection.prepareStatement("SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                Department d = instanciaDepartment(rs);
                return d;
            }

            return null;

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = connection.prepareStatement("SELECT * FROM department ORDER BY Name");
            rs = st.executeQuery();
            List<Department> list = new ArrayList<>();

            while(rs.next()){
                Department d = instanciaDepartment(rs);
                list.add(d);
            }

            return list;

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department instanciaDepartment(ResultSet rs) throws SQLException{
        Department d = new Department();
        d.setId(rs.getInt("Id"));
        d.setName(rs.getString("Name"));
        return d;
    }
}

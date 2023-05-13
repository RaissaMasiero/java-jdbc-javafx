module com.jdbcfx.javafxcomjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.jdbcfx.javafxcomjdbc to javafx.fxml;
    exports com.jdbcfx.javafxcomjdbc;
    exports com.jdbcfx.javafxcomjdbc.gui;
    exports com.jdbcfx.javafxcomjdbc.model.entities;
    exports com.jdbcfx.javafxcomjdbc.model.dao;
    exports com.jdbcfx.javafxcomjdbc.db;
    opens com.jdbcfx.javafxcomjdbc.gui to javafx.fxml;
}
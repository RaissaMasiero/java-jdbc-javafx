module com.jdbcfx.javafxcomjdbc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jdbcfx.javafxcomjdbc to javafx.fxml;
    exports com.jdbcfx.javafxcomjdbc;
}
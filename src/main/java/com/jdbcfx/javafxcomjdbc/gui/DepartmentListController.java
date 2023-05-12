package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.Main;
import com.jdbcfx.javafxcomjdbc.model.entities.Department;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnNome;

    @FXML
    private Button buttonNovo;

    @FXML
    public void onButtonNovoAction(){
        System.out.println("onButtonNovoAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }
}

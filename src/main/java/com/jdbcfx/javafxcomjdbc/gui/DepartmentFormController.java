package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.db.DbException;
import com.jdbcfx.javafxcomjdbc.gui.listeners.DataChangeListener;
import com.jdbcfx.javafxcomjdbc.gui.util.Alerts;
import com.jdbcfx.javafxcomjdbc.gui.util.Constraints;
import com.jdbcfx.javafxcomjdbc.gui.util.Utils;
import com.jdbcfx.javafxcomjdbc.model.entities.Department;
import com.jdbcfx.javafxcomjdbc.model.services.DepartmentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entidade;

    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private Label labelNomeErro;

    @FXML
    private Button buttonSalvar;

    @FXML
    private Button buttonCancelar;

    public void setDepartamento(Department entidade) {
        this.entidade = entidade;
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onButtonSalvarAction(ActionEvent event){
        if(entidade == null){
            throw new IllegalStateException("Entidade está nula!");
        }
        if(service == null){
            throw new IllegalStateException("Service está nulo!");
        }
        try {
            entidade = getFormData();
            service.saveOrUpdate(entidade);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Erro ao salvar objeto!", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtNome.getText());
        return obj;
    }

    @FXML
    public void onButtonCancelarAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }

    public void updateFormData(){
        if(entidade == null){
            throw new IllegalStateException("Entidade está nula!");
        }
        txtId.setText(String.valueOf(entidade.getId()));
        txtNome.setText(entidade.getName());
    }
}

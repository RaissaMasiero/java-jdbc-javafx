package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.db.DbException;
import com.jdbcfx.javafxcomjdbc.gui.listeners.DataChangeListener;
import com.jdbcfx.javafxcomjdbc.gui.util.Alerts;
import com.jdbcfx.javafxcomjdbc.gui.util.Constraints;
import com.jdbcfx.javafxcomjdbc.gui.util.Utils;
import com.jdbcfx.javafxcomjdbc.model.entities.Seller;
import com.jdbcfx.javafxcomjdbc.model.exceptions.ValidationException;
import com.jdbcfx.javafxcomjdbc.model.services.SellerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entidade;

    private SellerService service;

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

    public void setVendedor(Seller entidade) {
        this.entidade = entidade;
    }

    public void setSellerService(SellerService service) {
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
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidationException exception = new ValidationException("Erro de validação!");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtNome.getText() == null || txtNome.getText().trim().equals("")){
            exception.addError("name", "Campo não pode estar vazio!");
        }

        obj.setNome(txtNome.getText());

        if(exception.getErrors().size() > 0){
            throw exception;
        }

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
        txtNome.setText(entidade.getNome());
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if(fields.contains("nome")){
            labelNomeErro.setText(errors.get("nome"));
        }
    }
}

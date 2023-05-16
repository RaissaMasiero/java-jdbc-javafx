package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.db.DbException;
import com.jdbcfx.javafxcomjdbc.gui.listeners.DataChangeListener;
import com.jdbcfx.javafxcomjdbc.gui.util.Alerts;
import com.jdbcfx.javafxcomjdbc.gui.util.Constraints;
import com.jdbcfx.javafxcomjdbc.gui.util.Utils;
import com.jdbcfx.javafxcomjdbc.model.entities.Department;
import com.jdbcfx.javafxcomjdbc.model.entities.Seller;
import com.jdbcfx.javafxcomjdbc.model.exceptions.ValidationException;
import com.jdbcfx.javafxcomjdbc.model.services.DepartmentService;
import com.jdbcfx.javafxcomjdbc.model.services.SellerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entidade;

    private SellerService service;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpDataNascimento;

    @FXML
    private TextField txtSalarioBase;

    @FXML
    private ComboBox<Department> comboBoxDepartment;

    @FXML
    private Label labelNomeErro;

    @FXML
    private Label labelEmailErro;

    @FXML
    private Label labelDataNascimentoErro;

    @FXML
    private Label labelSalarioBaseErro;

    @FXML
    private Button buttonSalvar;

    @FXML
    private Button buttonCancelar;

    private ObservableList<Department> obsList;

    public void setVendedor(Seller entidade) {
        this.entidade = entidade;
    }

    public void setServices(SellerService service, DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
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
            exception.addError("nome", "Campo não pode estar vazio!");
        }
        obj.setNome(txtNome.getText());

        if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "Campo não pode estar vazio!");
        }
        obj.setEmail(txtEmail.getText());

        if(dpDataNascimento.getValue() == null){
            exception.addError("dataNascimento", "Campo não pode estar vazio!");
        }else{
            Instant instant = Instant.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setDataNascimento(Date.from(instant));
        }

        if(txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")){
            exception.addError("salarioBase", "Campo não pode estar vazio!");
        }
        obj.setSalarioBase(Utils.tryParseToDouble(txtSalarioBase.getText()));

        obj.setDepartment(comboBoxDepartment.getValue());

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
        Constraints.setTextFieldMaxLength(txtNome, 70);
        Constraints.setTextFieldDouble(txtSalarioBase);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void updateFormData(){
        if(entidade == null){
            throw new IllegalStateException("Entidade está nula!");
        }

        txtId.setText(String.valueOf(entidade.getId()));
        txtNome.setText(entidade.getNome());
        txtEmail.setText(entidade.getEmail());
        Locale.setDefault(Locale.US);

        txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));

        if(entidade.getDataNascimento() != null){
            dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getDataNascimento().toInstant(),
                    ZoneId.systemDefault()));
        }

        if(entidade.getDepartment() == null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        }else {
            comboBoxDepartment.setValue(entidade.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        if(departmentService == null){
            throw new IllegalStateException("DepartmentService está nulo!");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        labelNomeErro.setText((fields.contains("nome") ? errors.get("nome") : ""));
        labelEmailErro.setText((fields.contains("email") ? errors.get("email") : ""));
        labelDataNascimentoErro.setText((fields.contains("dataNascimento") ? errors.get("dataNascimento") : ""));
        labelSalarioBaseErro.setText((fields.contains("salarioBase") ? errors.get("salarioBase") : ""));
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}

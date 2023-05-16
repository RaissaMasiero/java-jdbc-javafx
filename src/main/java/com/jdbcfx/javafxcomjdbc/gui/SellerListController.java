package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.Main;
import com.jdbcfx.javafxcomjdbc.db.DbException;
import com.jdbcfx.javafxcomjdbc.gui.listeners.DataChangeListener;
import com.jdbcfx.javafxcomjdbc.gui.util.Alerts;
import com.jdbcfx.javafxcomjdbc.gui.util.Utils;
import com.jdbcfx.javafxcomjdbc.model.entities.Seller;
import com.jdbcfx.javafxcomjdbc.model.services.SellerService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnNome;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;

    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;

    @FXML
    private Button buttonNovo;

    private ObservableList<Seller> observableList;

    @FXML
    public void onButtonNovoAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Seller();
        createDialogForm(obj, "SellerForm.fxml", parentStage);
    }

    public void  setSellerService(SellerService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if (service == null) {
            throw new IllegalStateException("Service está nulo!");
        }
        List<Seller> list = service.findAll();
        observableList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(observableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller obj, String nomeCompleto, Stage parentStage){
        /*try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(nomeCompleto));
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setDepartamento(obj);
            controller.setSellerService(new SellerService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dados do Vendedor");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }catch (IOException e) {
            Alerts.showAlert("IO Exception", null, e.getMessage(), Alert.AlertType.ERROR);
        } */
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(obj, "SellerForm.fxml",
                                    Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {

            private final Button button = new Button("Excluir");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação",
                                                            "Tem certeza que quer excluir?");

        if(result.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Service está nulo!");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e) {
                Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}

package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.Main;
import com.jdbcfx.javafxcomjdbc.gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemVendedor;

    @FXML
    private MenuItem menuItemDepartamento;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemVendedorAction(){
        System.out.println("onMenuItemVendedorAction");
    }

    @FXML
    public void onMenuItemDepartamentoAction(){
        loadView("DepartmentList.fxml");
    }

    @FXML
    public void onMenuItemAboutAction(){
        loadView("About.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private synchronized void loadView(String nomeCompleto){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(nomeCompleto));
            VBox newVBox = loader.load();

            Scene scene = Main.getScene();
            VBox vBoxPrincipal = (VBox) ((ScrollPane)scene.getRoot()).getContent();

            Node menuPrincipal = vBoxPrincipal.getChildren().get(0);
            vBoxPrincipal.getChildren().clear();
            vBoxPrincipal.getChildren().add(menuPrincipal);
            vBoxPrincipal.getChildren().addAll(newVBox.getChildren());

        } catch (IOException e) {
            Alerts.showAlert("IOException", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}

package com.jdbcfx.javafxcomjdbc.gui;

import com.jdbcfx.javafxcomjdbc.Main;
import com.jdbcfx.javafxcomjdbc.gui.util.Alerts;
import com.jdbcfx.javafxcomjdbc.model.services.DepartmentService;
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
import java.util.function.Consumer;

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
        loadView("DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAboutAction(){
        loadView("About.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    // loadView recebe o nome do caminho da view e uma inicialização por parâmetro. Essa ação carrega a janela.
    private synchronized <T> void loadView(String nomeCompleto, Consumer<T> acaoDeInicializacao){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(nomeCompleto));
            VBox newVBox = loader.load();

            Scene scene = Main.getScene();
            VBox vBoxPrincipal = (VBox) ((ScrollPane)scene.getRoot()).getContent();

            Node menuPrincipal = vBoxPrincipal.getChildren().get(0);
            vBoxPrincipal.getChildren().clear();
            vBoxPrincipal.getChildren().add(menuPrincipal);
            vBoxPrincipal.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            acaoDeInicializacao.accept(controller);

        } catch (IOException e) {
            Alerts.showAlert("IOException", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}

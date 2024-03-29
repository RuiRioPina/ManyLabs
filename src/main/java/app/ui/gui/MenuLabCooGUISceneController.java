package app.ui.gui;

import app.controller.App;
import app.controller.IntervalController;
import app.domain.model.Test;
import app.ui.console.GenerateSampleUI;
import app.ui.console.ValidateWorkUI;
import app.ui.gui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MenuLabCooGUISceneController {

    private MainMenuGUISceneController menuUI;
    private App app;
    private IntervalController controller;

    public void associarParentUI(MainMenuGUISceneController mainMenuGUISceneController) {
        menuUI = mainMenuGUISceneController;
    }

    @FXML
    private Button importTests;

    @FXML
    private Label lblNameClient;

    public IntervalController getController() {
        return this.controller;
    }

    /**
     * Menu Import Tests.
     * @param event - event.
     */
    @FXML
    public void menuImportTestsAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());
        String path = file.getAbsolutePath();
        new ImportTests(path);
        if (!App.getInstance().getCompany().getImportedTests().isEmpty()) {
            Stage stage1 = loadTestsUi();
            if (stage1 == null) {
                return;
            }
            stage1.showAndWait();
        } else {
            JOptionPane.showMessageDialog(null,"No Tests were imported.");
            List<Test> newImpTests = new ArrayList<>();
            App.getInstance().getCompany().setImportedTests(newImpTests);
        }
    }

    private Stage loadTestsUi() {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ShowImpTestsScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage novoViewTestsStage = new Stage();
        novoViewTestsStage.initModality(Modality.APPLICATION_MODAL);
        novoViewTestsStage.setTitle("Imported Tests");
        novoViewTestsStage.setResizable(false);
        novoViewTestsStage.setScene(scene);

        ShowImportedTestsController novoViewTestsUI = loader.getController();
        novoViewTestsUI.associarParentUI(this);

        return novoViewTestsStage;
    } catch (IOException ex) {
        Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
        return null;
    }
    }

    @FXML
    private void menuViewMaxSum(ActionEvent event) {

        Stage stage1 = loadMaximunSumUi();
        if(stage1 == null) {
            return;
        }

        stage1.showAndWait();
    }

    private Stage loadMaximunSumUi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DatesInterval.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage novoViewTestDetailsStage = new Stage();
            novoViewTestDetailsStage.initModality(Modality.APPLICATION_MODAL);
            novoViewTestDetailsStage.setTitle("Max Sum");
            novoViewTestDetailsStage.setMaximized(true);
            novoViewTestDetailsStage.setScene(scene);
            TwoDatesInterval twoDatesInterval = loader.getController();
            twoDatesInterval.associarParentUI(this);

            return novoViewTestDetailsStage;
        } catch (IOException ex) {
            Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            return null;
        }
    }

    public void menuValidateWork(ActionEvent actionEvent) {
        Stage stg = (Stage) lblNameClient.getScene().getWindow();
        stg.hide();
        ValidateWorkUI validateWorkUI = new ValidateWorkUI();
        validateWorkUI.run();
        stg.show();
    }
}

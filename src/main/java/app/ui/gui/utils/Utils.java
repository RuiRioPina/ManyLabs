package app.ui.gui.utils;

import javafx.scene.control.Alert;

public class Utils {

    static public void createAlert(Alert.AlertType tipoAlerta, String cabecalho, String mensagem) {
        Alert alerta = new Alert(tipoAlerta);

        alerta.setTitle("Aplica��o");
        alerta.setResizable(true);
        alerta.setHeaderText(cabecalho);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}

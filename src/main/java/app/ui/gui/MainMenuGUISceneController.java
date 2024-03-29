package app.ui.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.controller.App;
import app.domain.shared.Constants;
import app.ui.gui.utils.Utils;
import auth.UserSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class MainMenuGUISceneController implements Initializable {

	private App app;
	@FXML
	private Label lblInitial;

	public MainMenuGUISceneController() {
		this.app = App.getInstance();
	}

	@Override
    public void initialize(URL url, ResourceBundle rb) {
			
    }
	
	private Stage loadLoginUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AuthGUIScene.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        
	        Stage novoLoginStage = new Stage();
	        novoLoginStage.initModality(Modality.APPLICATION_MODAL);
	        novoLoginStage.setTitle("Login");
	        novoLoginStage.setResizable(false);
	        novoLoginStage.setScene(scene);
	        
	        AuthGUISceneController novoLoginUI = loader.getController();
	        novoLoginUI.associarParentUI(this);
	        
	        return novoLoginStage;
        } catch (IOException ex) {	
        	Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            return null;
        }
	}

	private Stage loadLabCooUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LabCGUIScene.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);

			Stage Stage = new Stage();
			Stage.initModality(Modality.APPLICATION_MODAL);
			Stage.setTitle("Laboratory Coordinator");
			Stage.setMaximized(true);
			Stage.setScene(scene);
			Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					app.doLogout();
					Stage window = (Stage)lblInitial.getScene().getWindow();
					window.show();
				}
			});

			MenuLabCooGUISceneController labCooUI = loader.getController();
			labCooUI.associarParentUI(this);

			return Stage;
		} catch (IOException ex) {
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
			return null;
		}
	}

	private Stage loadClientUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientGUIScene.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        
	        Stage novoClientStage = new Stage();
	        novoClientStage.initModality(Modality.APPLICATION_MODAL);
	        novoClientStage.setTitle("Client");
	        novoClientStage.setMaximized(true);
	        novoClientStage.setScene(scene);
	        novoClientStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	            	app.doLogout();
	            	Stage window = (Stage)lblInitial.getScene().getWindow();
	                window.show();
	            }
	        });
	        
	        MenuClientGUISceneController novoClientUI = loader.getController();
	        novoClientUI.associarParentUI(this);
	        
	        return novoClientStage;
		} catch (IOException ex) {	
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
			return null;
		}
	}

	private Stage loadCctUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CctGUIScene.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);

			Stage novoCttStage = new Stage();
			novoCttStage.initModality(Modality.APPLICATION_MODAL);
			novoCttStage.setTitle("Clinical Chemistry Technologist");
			novoCttStage.setMaximized(true);
			novoCttStage.setScene(scene);
			novoCttStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Stage window = (Stage) lblInitial.getScene().getWindow();
					window.show();
				}
			});

			MenuCctGUISceneController cctUi = loader.getController();
			cctUi.associarParentUI(this);

			return novoCttStage;
		} catch (IOException ex) {
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
			return null;
		}
	}
	
	private Stage loadAdminUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminGUIScene.fxml"));
	        Parent root = loader.load();

	        Scene scene = new Scene(root);

	        Stage novoAdminStage = new Stage();
	        novoAdminStage.initModality(Modality.APPLICATION_MODAL);
	        novoAdminStage.setTitle("Administrador");
	        novoAdminStage.setMaximized(false);
	        novoAdminStage.setScene(scene);
	        novoAdminStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	            	app.doLogout();
	            	Stage window = (Stage)lblInitial.getScene().getWindow();
	                window.show();
	            }
	        });

	        MenuAdminGUISceneController novoAdminUI = loader.getController();
	        novoAdminUI.associarParentUI(this);

	        return novoAdminStage;
		} catch (IOException ex) {
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            return null;
        }
	}

	private Stage loadSpeDocUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SpecialistDoctorGUIScene.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);

			Stage novoAdminStage = new Stage();
			novoAdminStage.initModality(Modality.APPLICATION_MODAL);
			novoAdminStage.setTitle("Specialist Doctor");
			novoAdminStage.setMaximized(true);
			novoAdminStage.setScene(scene);
			novoAdminStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					app.doLogout();
					Stage window = (Stage) lblInitial.getScene().getWindow();
					window.show();
				}
			});

			MenuSpeDocGUISceneController speDocUI = loader.getController();
			speDocUI.associarParentUI(this);

			return novoAdminStage;
		} catch (IOException ex) {
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
			return null;
		}
}

	private Stage loadChooseLabUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChooseLabGUIScene.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        
	        Stage novoLabStage = new Stage();
	        novoLabStage.initModality(Modality.APPLICATION_MODAL);
	        novoLabStage.setTitle("Login");
	        novoLabStage.setResizable(false);
	        novoLabStage.setScene(scene);
	        
	        ChooseLabGUISceneController novoLoginUI = loader.getController();
	        novoLoginUI.associarParentUI(this);
	        
	        return novoLabStage;
        } catch (IOException ex) {	
        	Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            return null;
        }
	}
	
	private Stage loadMedLabTechUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MedLabTechGUIScene.fxml"));
	        Parent root = loader.load();

	        Scene scene = new Scene(root);

	        Stage novoMedLabStage = new Stage();
	        novoMedLabStage.initModality(Modality.APPLICATION_MODAL);
	        novoMedLabStage.setTitle("Medical Lab Technician");
	        novoMedLabStage.setMaximized(true);
	        novoMedLabStage.setScene(scene);
	        novoMedLabStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	            	app.doLogout();
	            	Stage window = (Stage)lblInitial.getScene().getWindow();
	                window.show();
	            }
	        });

	        MenuMedicalLabTechnicianGUISceneController novoMedLabUI = loader.getController();
	        novoMedLabUI.associarParentUI(this);

	        return novoMedLabStage;
		} catch (IOException ex) {
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            return null;
        }
	}
	private Stage loadReceptionistUi() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReceptionistGUIScene.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);

			Stage novoMedLabStage = new Stage();
			novoMedLabStage.initModality(Modality.APPLICATION_MODAL);
			novoMedLabStage.setTitle("Receptionist");
			novoMedLabStage.setMaximized(true);
			novoMedLabStage.setScene(scene);
			novoMedLabStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					app.doLogout();
					Stage window = (Stage)lblInitial.getScene().getWindow();
					window.show();
				}
			});

			MenuReceptionistGUISceneController rec = loader.getController();
			rec.associarParentUI(this);

			return novoMedLabStage;
		} catch (IOException ex) {
			Utils.createAlert(Alert.AlertType.ERROR, "Erro", ex.getMessage());
			return null;
		}
	}
	@FXML
    private void menuExitAction(ActionEvent event) {
        Window window = lblInitial.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

	@FXML
	private void menuLoginAction(ActionEvent event) {
		app.doLogout();
		Stage stage1 = loadLoginUi();
		if(stage1 == null) {
			return;
		}
		stage1.showAndWait();

		UserSession sessao = this.app.getCurrentUserSession();
		if(sessao == null) {
			return;
		}

		Stage stage = null;
		if(sessao.isLoggedInWithRole(Constants.ROLE_CLIENT)) {
			stage = loadClientUi();
		} else if(sessao.isLoggedInWithRole(Constants.ROLE_ADMIN)) {
			stage = loadAdminUi();
		}else if(sessao.isLoggedInWithRole(Constants.ROLE_CLINICAL_CHEMISTRY_TECHNOLOGIST)) {
			stage = loadCctUi();
		} else if (sessao.isLoggedInWithRole(Constants.ROLE_LABORATORY_COORDINATOR)){
			stage = loadLabCooUi();
		} else if (sessao.isLoggedInWithRole(Constants.ROLE_SPECIALIST_DOCTOR)){
			stage = loadSpeDocUi();
		} else if (sessao.isLoggedInWithRole(Constants.ROLE_MEDICAL_LAB_TECHNICIAN)){
			Stage stage2 = loadChooseLabUi();
			if(stage2 == null) {
				return;
			}
			stage2.showAndWait();
			if(app.getCompany().getCLA() == null) {
				return;
			}
			stage = loadMedLabTechUi();
		}
	    else if (sessao.isLoggedInWithRole(Constants.ROLE_RECEPTIONIST)){
		Stage stage3 = loadChooseLabUi();
		if(stage3 == null) {
			return;
		}
		stage3.showAndWait();
		if(app.getCompany().getCLA() == null) {
			return;
		}
		stage = loadReceptionistUi();
	}
		if(stage == null) {
			return;
		}

		stage.show();

		Window window = lblInitial.getScene().getWindow();
		window.hide();
	}



	public Label getLabel() {
		return this.lblInitial;
	}
	@FXML
    private void menuDevTeamAction(ActionEvent event) {
		String x = "Development Team:\n\t Jo�o Moreira - 1190709@isep.ipp.pt \n\t Jorge Ferreira - 1201564@isep.ipp.pt \n\t Rafael Leite - 1201566@isep.ipp.pt \n"
				+ "\t Rui Pina - 1201568@isep.ipp.pt \n\t Santiago Azevedo - 1201623@isep.ipp.pt \n";
        Utils.createAlert(Alert.AlertType.INFORMATION, "Acerca",
                x);
    }
}

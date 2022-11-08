package application;

import application.news.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import serverConection.ConnectionManager;

public class LoginController {
//TODO Add all attribute and methods as needed 
	private LoginModel loginModel = new LoginModel();
	private NewsReaderModel newsReaderModel = new NewsReaderModel();

	private User loggedUsr = null;
	private String user;
	private String password;

	@FXML
	private Button btnLogin;
	@FXML
	private Button btnCancel;
	@FXML
	private Text loginError;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	public LoginController() {

		// Uncomment next sentence to use data from server instead dummy data
		loginModel.setDummyData(false);
		
	}

	User getLoggedUsr() {
		return loggedUsr;

	}

	void setConnectionManager(ConnectionManager connection) {
		this.loginModel.setConnectionManager(connection);
	}
	@FXML
	void initialize() {
		assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'Login.fxml'.";
	}

	@FXML
	void onLogin(ActionEvent event) {
		user = usernameField.getText().toString();
		password = passwordField.getText().toString();
		
		
		User usr = loginModel.validateUser(user, password);
		if (usr == null) {
			System.out.print("Login Error");
			loginError.setText("Login error! Incorrect user or password!");
		} else {
			loggedUsr = usr;
								
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
		}

	}

	@FXML
	void onCancel(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.close();
	}
}
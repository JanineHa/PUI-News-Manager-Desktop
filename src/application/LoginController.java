package application;

import com.jfoenix.controls.JFXButton;

import application.news.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import serverConection.ConnectionManager;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
//TODO Add all attribute and methods as needed 
	private LoginModel loginModel = new LoginModel();
	@FXML
	private TextField UserId;
	@FXML
	private PasswordField Passwd;

	private User loggedUsr = null;
	@FXML
	private JFXButton LoginBtn;
	@FXML
	private JFXButton CancelBtn;

	private String UserId_string;
	private String Passwd_string;
	@FXML
	private Text ErrorMsg;

	@FXML
	void cancel(ActionEvent event) {
		System.out.println("cancel");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}

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
		//System.out.println("successss");
		//assert LoginBtn != null : "";
	}

	// Login Functionality
	@FXML
	void LoginAction(ActionEvent event) {
		// System.out.println("success123");
		UserId_string = UserId.getText().toString();
		Passwd_string = Passwd.getText().toString();
		// System.out.println("success456");
		// System.out.println(UserId_string+" "+ Passwd_string);
		// loginModel.setConnectionManager(this.NewsReaderModel.getConnectionManger() );
		User User_Variable = loginModel.validateUser(UserId_string, Passwd_string);
		// System.out.println("success2");
		if (User_Variable != null) {
			// System.out.println("success");
			loggedUsr = User_Variable; // Set Variable after validation
			// Change to NewsReader
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
		} else {
			ErrorMsg.setText("Incorrect ID Password : Please try again");
		}
	}
}
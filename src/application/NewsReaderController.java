/**
 * 
 */
package application;

import java.io.IOException;

import application.news.Article;
import application.news.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import serverConection.ConnectionManager;


/**
 * @author ÃngelLucas
 *
 */
public class NewsReaderController {

	private NewsReaderModel newsReaderModel = new NewsReaderModel();
	private User usr;
	Article Article;


	//TODO add attributes and methods as needed

	public NewsReaderController() {
		//TODO
		//Uncomment next sentence to use data from server instead dummy data
		newsReaderModel.setDummyData(false);
		//Get text Label
		
	}	
	

	private void getData() {
		//TODO retrieve data and update UI
		//The method newsReaderModel.retrieveData() can be used to retrieve data 
	}

	/**
	 * @return the usr
	 */
	User getUsr() {
		return usr;
	}

	void setConnectionManager (ConnectionManager connection){
		this.newsReaderModel.setDummyData(false); //System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		this.getData();
	}
	
	/**
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		
		this.usr = usr;
		//Reload articles
		this.getData();
		//TODO Update UI
	}

    @FXML
	void openDetails(ActionEvent event) {
    	try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.NEWS_DETAILS.getFxmlFile()));
			Scene articleScene = new Scene(loader.load());
			ArticleDetailsController controller = loader.<ArticleDetailsController>getController();
			Article article = newsReaderModel.getFullArticle(1);
			controller.setArticle(article);
			controller.setUsr(usr);
			primaryStage.setScene(articleScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}

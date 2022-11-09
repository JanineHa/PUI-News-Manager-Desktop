/**
 * 
 */
package application;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import application.news.Article;
import application.news.User;
import application.utils.JsonArticle;
import application.utils.exceptions.ErrorMalFormedArticle;
import application.news.Categories;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

import javafx.stage.Modality;

import javafx.stage.FileChooser;

import javafx.stage.Modality;

import javafx.stage.Stage;
import javafx.stage.Window;
import serverConection.ConnectionManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * @author �ngelLucas
 * * Manon Eppl�e
 * Janine Haschke
 *
 */
public class NewsReaderController {

	private NewsReaderModel newsReaderModel = new NewsReaderModel();
	private User usr;
	Article Article;

	@FXML
	private ListView<Article> articleListView;
	@FXML
	private ListView<String> menuOptions;
	@FXML
	private FilteredList<Article> filteredArticleList;
	@FXML
	private ComboBox<Categories> categorieList;
	@FXML
	private Button btnDetails;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnNew;
	@FXML
	private Button btnLoad;
	@FXML
	private ImageView imageView;
	@FXML
	private WebView articleAbstract;
	@FXML
	private Label username;

	// TODO add attributes and methods as needed

	public NewsReaderController() {
		// TODO
		// Uncomment next sentence to use data from server instead dummy data
		newsReaderModel.setDummyData(true);
		// Get text Label

	}

	private void getData() {
		// TODO retrieve data and update UI
		// The method newsReaderModel.retrieveData() can be used to retrieve data

		newsReaderModel.retrieveData();

		filteredArticleList = new FilteredList<>(newsReaderModel.getArticles(), article -> true);
		this.articleListView.setItems(filteredArticleList);
		articleListView.getSelectionModel().selectFirst();

		if (this.usr == null) {
			this.btnDelete.setVisible(false);
			this.btnEdit.setVisible(false);
			
		} else {
			
			this.btnDelete.setVisible(true);
			this.btnEdit.setVisible(true);
			
		}

	}

	@FXML
	private void filterCategorie(ActionEvent event) {
		String filterText = this.categorieList.getValue().toString();

		if (filterText == "All") {
			filteredArticleList.setPredicate(article -> true);

		} else {
			filteredArticleList.setPredicate(article -> article.getCategory().equals(filterText));
		}

		this.articleListView.setItems(filteredArticleList);
		articleListView.getSelectionModel().selectFirst();
	}

	/**
	 * @return the usr
	 */
	User getUsr() {
		return usr;
	}

	public void setConnectionManager(ConnectionManager connection) {
		this.newsReaderModel.setDummyData(false); // System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		categorieList.getItems().addAll(newsReaderModel.getCategories());
		categorieList.getSelectionModel().selectFirst();
		this.getData();
	}

	/**
	 * @param usr the usr to set
	 */
	public void setUsr(User usr) {

		this.usr = usr;
		// Reload articles
		this.getData();
		// TODO Update UI
		if (usr == null) {
			return; // Not logged user
		}
		this.username.setText(usr.getLogin());
	}

	@FXML
	void initialize() {
		assert articleListView != null : "the List is empty";

		this.articleListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Article>() {
			@Override
			public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
				if (newValue != null) {
					Article = newValue;
					articleAbstract.getEngine().loadContent(Article.getAbstractText());
					imageView.setImage(Article.getImageData());
					Article.setIdArticle(newValue.getIdArticle());
					if (usr != null && !(Article.getIdUser() == usr.getIdUser())) {
						btnDelete.setVisible(false);
						btnEdit.setVisible(false);
					} else if (usr != null && (Article.getIdUser() == usr.getIdUser())) {
						btnDelete.setVisible(true);
						btnEdit.setVisible(true);
					}
				}
			}

		});
	}

	@FXML
	void onOpenDetails(ActionEvent event) {
		try {
			Scene parentScene = ((Node) event.getSource()).getScene();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.NEWS_DETAILS.getFxmlFile()));
			Pane root = loader.load();
			Scene scene = new Scene(root);
			Window parentStage = parentScene.getWindow();
			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("Details Page");
			ArticleDetailsController controller = loader.<ArticleDetailsController>getController();
			Article article = newsReaderModel.getFullArticle(Article.getIdArticle());
			controller.setArticle(article);
			controller.setUsr(usr);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@FXML
	void onEdit(ActionEvent event) {
		try{
			FXMLLoader loader= new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));			
			Stage primaryStage= (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene articleScene= new Scene(loader.load());
			
			Article article= newsReaderModel.getFullArticle(Article.getIdArticle());
			ArticleEditController controller= loader.<ArticleEditController>getController();
			controller.setConnectionMannager(this.newsReaderModel.getConnectionManager());
			controller.setArticle(article);
			controller.setUsr(usr);
			primaryStage.setScene(articleScene);
			primaryStage.setTitle("Edit Page");

		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void onNew(ActionEvent event) {
		try{
			FXMLLoader loader= new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));			
			Stage primaryStage= (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene articleScene= new Scene(loader.load());
			ArticleEditController controller= loader.<ArticleEditController>getController();
			controller.setArticle(null);
			controller.setUsr(this.usr);
			controller.setConnectionMannager(newsReaderModel.getConnectionManager());
			primaryStage.setScene(articleScene);
			primaryStage.setTitle("New Article Page");

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onDelete(ActionEvent event) {
		
		 newsReaderModel.deleteArticle(this.Article);
	
	}

	@FXML
	void onLogin(ActionEvent event) {
		try {
			Scene parentScene = ((Node) event.getSource()).getScene();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.LOGIN.getFxmlFile()));
			Pane root = loader.load();
			Scene scene = new Scene(root);
			Window parentStage = parentScene.getWindow();
			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("Login Page");

			LoginController controller = loader.<LoginController>getController();
			controller.setConnectionManager(this.newsReaderModel.getConnectionManager());
			stage.showAndWait();

			User loggedUser = controller.getLoggedUsr();
			if (loggedUser != null) {
				setUsr(loggedUser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onExit(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void onLoad(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Article file");

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Article files (*.news)", "*.news");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(new Stage());
		if (file == null) {
			return;
		}

		try {
			Article article = JsonArticle.jsonToArticle(JsonArticle.readFile(file.getAbsolutePath()));
				
			FXMLLoader loader= new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));			
			Stage primaryStage= (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene articleScene= new Scene(loader.load());
			
			ArticleEditController controller= loader.<ArticleEditController>getController();
			controller.setConnectionMannager(this.newsReaderModel.getConnectionManager());
			controller.setArticle(article);
			controller.setUsr(usr);
			primaryStage.setScene(articleScene);
			primaryStage.setTitle("Edit Page");
			
		

		} catch (ErrorMalFormedArticle ex) {
			ex.printStackTrace();
		}
	}
}

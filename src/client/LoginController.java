package client;

import java.io.IOException;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import protocol.Action;
import protocol.Data;
import protocol.MessageBox;

public class LoginController {
	private Client client;
	private Stage stage;
	private MainController controller;
	private Scene scene;
	private CreateAccountController createAccountController;
	
	private ObservableList<String> friendsList;
	private TreeItem<String> treeViewRoot;
	private TreeView<String> chatTreeView;
	private HashMap<String, TextArea> messageSpaces;
	private Label loggedIn;
	@FXML private Button exit;
	@FXML private Button login;
	@FXML private Text signup;
	@FXML private Text noAccount;
	@FXML private Text username;
	@FXML private Text password;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private Text signupSuccessful;
	@FXML private Text loginUnsuccessful;
	@FXML private Text loginText;
	
	public LoginController(Stage stage, Client client) {
		this.client = client;
		this.stage = stage;
		treeViewRoot = new TreeItem<String>();
		friendsList = FXCollections.observableArrayList();
		messageSpaces = new HashMap<String, TextArea>();
	}
	
	public void initialize() {		
		signupSuccessful.setVisible(false);
		loginUnsuccessful.setVisible(false);
	}

	@FXML
	public void exit(ActionEvent e) {
		MessageBox logout = new MessageBox(Action.QUIT);
		logout.add(Data.USER_NAME, client.getUser().getUserName());
		client.sendMessage(logout);
		System.exit(1);
	}
	
	@FXML 
	public void login(ActionEvent e) {
		signupSuccessful.setVisible(false);
		String username = usernameField.getText();
		if (username.length() > 10) {
			refuseLogin(true);
			return;
		}
		MessageBox login = new MessageBox(Action.LOGIN);
		login.add(Data.USER_NAME, username);
		client.getUser().setUserName(usernameField.getText());
		usernameField.clear();
		login.add(Data.PASSWORD, passwordField.getText() + "");
		passwordField.clear();
		client.sendMessage(login);
	}
	
	@FXML
	public void signup(MouseEvent e) throws IOException {
		CreateAccountController controller = new CreateAccountController(client, stage, this);
		createAccountController = controller;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
		loader.setController(controller);
		Parent root = loader.load();
		
		Scene scene = new Scene(root);
		stage.close();
		stage.setTitle("ChinWag");
		stage.setScene(scene);
		stage.show();
	}
	
	public void login() throws IOException {
		//loggedInAs = new Text("Logged in as " + loggedInName);
		loggedIn = new Label();
		loggedIn.setText("Logged in as " + client.getUser().getUserName());
		controller = new MainController(stage, client, 
				treeViewRoot, chatTreeView, messageSpaces, loggedIn, friendsList,
				scene, this);
		setMainController(controller);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
		loader.setController(controller);
		Parent root = loader.load();
		stage.close();
		scene = new Scene(root);
		stage.setTitle("ChinWag");
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		//drawMainScreen(messageSpaces.get(chatTreeView.getSelectionModel().getSelectedItem().getValue()));
		//messageSpaces.get("global").appendText("Login successful!" + "\n");
	}
	
	public void refuseLogin(boolean nameTooLong) {
		
		loginUnsuccessful.setVisible(true);
		loginText.setFill(Color.RED); //notifies user
		usernameField.clear();
		passwordField.clear();
		login.setDefaultButton(true);
		usernameField.requestFocus();
	}
	
	public MainController getMainController() {
		return controller;
	}
	
	public void setMainController(MainController controller) {
		this.controller = controller;
	}
	
	public TreeView<String> getChatTreeView() {
		return chatTreeView;
	}
	
	public TreeItem<String> getTreeViewRoot() {
		return treeViewRoot;
	}
	
	public ObservableList<String> getFriendsList(){
		return friendsList;
	}
	
	public HashMap<String, TextArea> getMessageSpaces() {
		return messageSpaces;
	}
	
	public void setSuccessfulSignupVisible() {
		signupSuccessful.setVisible(true);
	}
	
	public CreateAccountController getCreateAccountController() {
		return createAccountController;
	}
	
	public void addSessionSpace(String session) {
		TextArea newSpace = new TextArea();
//		newSpace.setOnMouseReleased(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent e) {
//				requestInputFocus();
//			}	
//		});
		newSpace.setEditable(false);
		newSpace.setWrapText(true);
		messageSpaces.put(session, newSpace);
	}
	
}

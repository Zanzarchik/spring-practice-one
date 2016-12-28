package ua.epam.spring.hometask.gui.forms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.gui.Main;
import ua.epam.spring.hometask.service.api.AuthService;
import ua.epam.spring.hometask.service.api.UserService;

public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signIn;
    private Stage primaryStage;

    private AuthService authService = Main.getSpringContext().getBean(AuthService.class);
    private UserService userService = Main.getSpringContext().getBean(UserService.class);

    @FXML
    private void initialize() {

        //primaryStage = (Stage) signIn.getScene().getWindow();
        signIn.setOnAction(event -> doLogin());

        AnchorPane pane = Main.getPaneFromLocation("forms/home.fxml", AnchorPane.class);
        Scene scene = new Scene(pane);

       // primaryStage.setScene(scene);
        //primaryStage.show();

    }

    private void doLogin() {

        User user = userService.getUserByEmail(username.getText());
        if (authService.login(user, password.getText())) {

        }
    }
}

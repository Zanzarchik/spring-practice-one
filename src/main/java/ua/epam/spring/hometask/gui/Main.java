package ua.epam.spring.hometask.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static ApplicationContext springContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
    private static final FXMLLoader LOADER = new FXMLLoader();
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        initPrimaryStage();
    }

    public static ApplicationContext getSpringContext() {
        return springContext;
    }

    private void initPrimaryStage() {

        AnchorPane pane = getPaneFromLocation("forms/login.fxml", AnchorPane.class);
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    static public <T> T getPaneFromLocation(String path, Class<T> type) {

        try {
            return LOADER.load(LOADER.getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException("Can not found .fxml file " + path, e);
        }
    }
}

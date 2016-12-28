package ua.epam.spring.hometask.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static ApplicationContext springContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public static ApplicationContext getSpringContext() {
        return springContext;
    }
}

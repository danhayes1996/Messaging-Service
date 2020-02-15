package com.drh.messaging.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Messaging Client");
    primaryStage.setScene(new Scene(new VBox(), 300, 275));
    primaryStage.show();
  }

  @Override
  public void init() {

  }

  public static void main(String[] args) {
    launch(args);
  }
}

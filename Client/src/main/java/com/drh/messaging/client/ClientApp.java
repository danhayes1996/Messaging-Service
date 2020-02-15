package com.drh.messaging.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
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
//    launch(args);

    try {
      Socket server = new Socket("localhost", 8081);
      InputStream is = server.getInputStream();
      OutputStream os = server.getOutputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      System.out.println(reader.readLine());
      os.write("testing\r".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

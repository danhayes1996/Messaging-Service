package com.drh.messaging.server.connector;

import com.drh.messaging.server.Server;
import com.drh.messaging.server.client.ClientSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketConnector {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private ServerSocket serverSocket = null;
  private Server server;

  public SocketConnector(int port, Server server) {
    this.server = server;
    try {
      this.serverSocket = new ServerSocket(port);
      LOGGER.info("Server started on port " + port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void listenForConnections() {
    if(serverSocket != null) {
      LOGGER.info("Started listening for connections.");
      while(true) {
        try {
          Socket socket = serverSocket.accept();
          LOGGER.info("New connection received: " + socket.getInetAddress().toString());
          ClientSocket clientSocket = new ClientSocket(socket, server);
          new Thread(clientSocket).start();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
//      try {
//        serverSocket.close();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
    }
  }
}

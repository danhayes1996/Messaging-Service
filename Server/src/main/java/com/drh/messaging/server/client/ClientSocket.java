package com.drh.messaging.server.client;

import static com.drh.messaging.server.utils.MessageFormatter.formatMessage;

import com.drh.messaging.server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSocket implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private Socket socket;
  private Server server;

  private String screenName;

  public ClientSocket(Socket socket, Server server) {
    this.socket = socket;
    this.server = server;
  }

  public void run() {
    try {
      handleClientSocket();
    } catch (IOException e) {
      LOGGER.info("Client Connection Interrupted: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if(!socket.isClosed()) {
        closeConnection();
      }
    }
  }

  private void handleClientSocket() throws IOException {
    initClient();

    InputStream clientIs = socket.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(clientIs));
    String line;
    while(!socket.isClosed() && (line = reader.readLine()) != null) {
      if(isCommand(line)) {
         executeCommand(line);
      } else {
        LOGGER.info("Broadcasting message \"" + line + "\" from \"" + screenName + "\"");
        server.broadcastMessage(this, line);
      }
    }
  }

  public void executeCommand(String command) {
    LOGGER.info("User \"" + screenName + "\" has sent the command \"" + command + "\"");
    Server.SERVER_COMMANDS.get(command).apply(command, this, server);
  }

  public boolean isCommand(String line) {
    String[] segments = line.trim().split(" ");
    if(segments != null && segments.length > 0) {
      if(Server.SERVER_COMMANDS.containsKey(segments[0])) return true;
    }
    return false;
  }

  private void initClient() throws IOException {
    LOGGER.info("Greeting new client.");
    OutputStream clientOs = socket.getOutputStream();
    InputStream clientIs = socket.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(clientIs));

    clientOs.write(formatMessage("Welcome, enter a screen name:", false));

    String screenName = reader.readLine();
    this.screenName = screenName;
    LOGGER.info("Client set their screen name: \"" + screenName + "\"");

    server.addClientSocket(this);
  }

  public void send(byte[] message) {
    try {
      socket.getOutputStream().write(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getScreenName() {
    return screenName;
  }

  public void closeConnection() {
    try {
      socket.close();
      LOGGER.info("Client \"" + screenName + "\" has disconnected");
      server.removeClient(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

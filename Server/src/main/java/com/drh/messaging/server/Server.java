package com.drh.messaging.server;

import static com.drh.messaging.server.utils.MessageFormatter.formatMessage;
import static java.util.logging.Logger.getLogger;

import com.drh.messaging.server.client.ClientSocket;
import com.drh.messaging.server.connector.SocketConnector;
import com.drh.messaging.server.utils.CommandFuncs;
import com.drh.messaging.server.utils.CommandFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Server {
  private static final Logger LOGGER = getLogger(Logger.GLOBAL_LOGGER_NAME);

  private SocketConnector socketConnector;
  private List<ClientSocket> clientSockets;

  public static final Map<String,
      CommandFunction<
          String,
          ClientSocket,
          Server,
          Void>> SERVER_COMMANDS = new HashMap<>();

  static {
    SERVER_COMMANDS.put("/quit", CommandFuncs.QUIT);
    SERVER_COMMANDS.put("/list", CommandFuncs.LIST_USERS);
  }

  public Server() {
    socketConnector = new SocketConnector(8081, this);
    clientSockets = new ArrayList<>();
  }

  public void start() {
    socketConnector.listenForConnections();
  }

  public void broadcastMessage(ClientSocket sender, String message) {
    byte[] formattedMessage = formatMessage(getMessageWithSender(sender, message), true);
    for (ClientSocket clientSocket : clientSockets) {
      if (sender.getScreenName().equals(clientSocket.getScreenName())) {
        continue;
      }
      clientSocket.send(formattedMessage);
    }
  }

  private void broadcastMessage(String message) {
    byte[] formattedMessage = formatMessage(message, true);
    for (ClientSocket clientSocket : clientSockets) {
      clientSocket.send(formattedMessage);
    }
  }

  private String getMessageWithSender(ClientSocket sender, String message) {
    return sender.getScreenName() + " : " + message + "\n";
  }

  public void addClientSocket(ClientSocket clientSocket) {
    LOGGER.info("Adding client \"" + clientSocket.getScreenName() + " to list of connections");
    if (clientSockets.add(clientSocket)) {
      broadcastMessage(clientSocket.getScreenName() + " has connected to the server.");
    }
  }

  public void removeClient(ClientSocket clientSocket) {
    LOGGER.info("Removing client \"" + clientSocket.getScreenName() + " from list of connections");
    if (clientSockets.remove(clientSocket)) {
      broadcastMessage(clientSocket.getScreenName() + " has disconnected from the server.");
    }
  }

  public void listUsers(ClientSocket clientSocket) {
    StringBuilder sb = new StringBuilder();
    sb.append("User List ").append(clientSockets.size()).append(":\n");
    for (ClientSocket client : clientSockets) {
      if (client.getScreenName().equals(clientSocket.getScreenName())) {
        sb.append("  *").append(client.getScreenName()).append("\n");
      } else {
        sb.append("  ").append(client.getScreenName()).append("\n");
      }
    }
    clientSocket.send(sb.toString().getBytes());
  }
}

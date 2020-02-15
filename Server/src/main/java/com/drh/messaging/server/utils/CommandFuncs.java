package com.drh.messaging.server.utils;

import com.drh.messaging.server.Server;
import com.drh.messaging.server.client.ClientSocket;

public class CommandFuncs {

  public static final CommandFunction<String, ClientSocket, Server, Void> QUIT =
      (command, clientSocket, server) -> {
        clientSocket.closeConnection();
        return null;
      };

  public static final CommandFunction<String, ClientSocket, Server, Void> LIST_USERS =
      (command, clientSocket, server) -> {
        server.listUsers(clientSocket);
        return null;
      };
}

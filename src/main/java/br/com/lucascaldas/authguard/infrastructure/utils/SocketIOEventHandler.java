package br.com.lucascaldas.authguard.infrastructure.utils;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketIOEventHandler {

    @Autowired
    private SocketIOServer server;

    @PostConstruct
    public void init() {
        // Log when a client connects
        server.addConnectListener(socket -> {
            log.info("Client connected: " + socket.getSessionId());
        });

        // Log when a client disconnects
        server.addDisconnectListener(socket -> {
            log.info("Client disconnected: " + socket.getSessionId());
        });

        // Listen for a "joinRoom" event from the client
        server.addEventListener("joinRoom", String.class, (socket, roomId, ackSender) -> {
            socket.joinRoom(roomId);
            log.info("Socket " + socket.getSessionId() + " joined room: " + roomId);
        });

    }
}

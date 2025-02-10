package br.com.lucascaldas.authguard.infrastructure.service;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketIOService {

    @Autowired
    private SocketIOServer server;

    /**
     * Send a message to all clients in a specific room.
     *
     * @param room  the room id (as a String)
     * @param event the event name
     * @param data  the data to send
     */
    public void sendMessageToRoom(String room, String event, Object data) {
        server.getRoomOperations(room).sendEvent(event, data);
    }
}

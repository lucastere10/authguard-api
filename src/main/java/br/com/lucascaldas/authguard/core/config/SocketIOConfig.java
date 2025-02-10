package br.com.lucascaldas.authguard.core.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SocketIOConfig {

    // You can externalize the port via application.properties (default to 9092 if not set)
    @Value("${socket.server.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(port);
        // Optionally, set additional configuration (e.g., authorization, ping timeout, etc.)
        return new SocketIOServer(config);
    }

    // Start the server when the application starts
    @Bean
    public CommandLineRunner runner(SocketIOServer server) {
        return args -> {
            server.start();
            // Ensure the server stops on shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        };
    }
}

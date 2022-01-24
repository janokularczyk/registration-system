package com.example.railwaysystem.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping
    public List<Connection> getConnections() {
        return connectionService.getConnections();
    }

    @PostMapping
    public void postNewConnection(@RequestBody Connection connection) {
        connectionService.addNewConnection(connection);
    }
}

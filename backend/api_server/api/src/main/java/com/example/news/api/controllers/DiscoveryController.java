package com.example.news.api.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.service.ServerDbBridge;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/discovery")
public class DiscoveryController {
    private final ServerDbBridge bridgeClient;
    public DiscoveryController() {
        this.bridgeClient = new ServerDbBridge();
    }


}

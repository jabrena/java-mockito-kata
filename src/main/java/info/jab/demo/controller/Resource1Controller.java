package info.jab.demo.controller;

import info.jab.demo.service.Resource1Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class Resource1Controller {

    private final Resource1Service resource1Service;

    public Resource1Controller(Resource1Service resource1Service) {
        this.resource1Service = resource1Service;
    }

    @GetMapping("resource1")
    public ResponseEntity<List<String>> getResources() {
        List<String> resources = resource1Service.getResources();
        return ResponseEntity.ok(resources);
    }
}

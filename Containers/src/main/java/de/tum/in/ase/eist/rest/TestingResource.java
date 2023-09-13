package de.tum.in.ase.eist.rest;

import de.tum.in.ase.eist.Application;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Do not change this class. It is used to test your Heroku deployment
 */
@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class TestingResource {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @GetMapping("testing/restart")
    public void restart() {
        Application.restart();
    }

    @GetMapping("testing/structure")
    public ResponseEntity<String> topLevelFileStructure() throws IOException {
        try (var inputStream = Runtime.getRuntime().exec("ls -al /app").getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                var structure = reader.lines().collect(Collectors.joining("\n"));
                return ResponseEntity.ok(structure);
            }
        }
    }

    @GetMapping("testing/url")
    public ResponseEntity<String> getUrl() {
        return ResponseEntity.ok(jdbcUrl);
    }
}

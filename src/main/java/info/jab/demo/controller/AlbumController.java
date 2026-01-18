package info.jab.demo.controller;

import info.jab.demo.model.AlbumEntity;
import info.jab.demo.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping("/albums")
    public ResponseEntity<AlbumEntity> createAlbum(@RequestBody CreateAlbumRequest request) {
        AlbumEntity album = albumService.createAlbum(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(album);
    }
}

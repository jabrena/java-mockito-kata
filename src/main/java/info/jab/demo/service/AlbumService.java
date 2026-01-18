package info.jab.demo.service;

import info.jab.demo.controller.CreateAlbumRequest;
import info.jab.demo.model.AlbumEntity;

import java.util.List;

public interface AlbumService {
    List<AlbumEntity> getAlbums();
    AlbumEntity createAlbum(CreateAlbumRequest request);
}

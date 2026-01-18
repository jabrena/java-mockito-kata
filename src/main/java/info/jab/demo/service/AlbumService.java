package info.jab.demo.service;

import info.jab.demo.controller.CreateAlbumRequest;
import info.jab.demo.model.AlbumEntity;

public interface AlbumService {
    AlbumEntity createAlbum(CreateAlbumRequest request);
}

package info.jab.demo.repository;

import info.jab.demo.model.AlbumEntity;

import java.util.List;

public interface AlbumRepository {
    List<AlbumEntity> findAll();
    AlbumEntity save(AlbumEntity album);
}

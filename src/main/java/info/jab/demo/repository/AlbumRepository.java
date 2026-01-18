package info.jab.demo.repository;

import info.jab.demo.model.AlbumEntity;

public interface AlbumRepository {
    AlbumEntity save(AlbumEntity album);
}

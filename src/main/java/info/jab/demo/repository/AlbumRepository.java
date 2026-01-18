package info.jab.demo.repository;

import info.jab.demo.model.AlbumEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AlbumRepository extends ListCrudRepository<AlbumEntity, UUID> {

    @Modifying
    @Query("INSERT INTO albums (id, name) VALUES (:#{#id}, :#{#name})")
    @Transactional
    void insertAlbum(@Param("id") UUID id, @Param("name") String name);
}

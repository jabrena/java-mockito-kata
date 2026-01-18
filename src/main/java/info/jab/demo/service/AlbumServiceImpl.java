package info.jab.demo.service;

import info.jab.demo.controller.CreateAlbumRequest;
import info.jab.demo.model.AlbumEntity;
import info.jab.demo.repository.AlbumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumEntity> getAlbums() {
        return albumRepository.findAll();
    }

    @Override
    @Transactional
    public AlbumEntity createAlbum(CreateAlbumRequest request) {
        UUID uuid = UUID.randomUUID();
        albumRepository.insertAlbum(uuid, request.name());
        return new AlbumEntity(uuid, request.name());
    }
}

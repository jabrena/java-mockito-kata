package info.jab.demo.service;

import info.jab.demo.controller.CreateAlbumRequest;
import info.jab.demo.model.AlbumEntity;
import info.jab.demo.repository.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public AlbumEntity createAlbum(CreateAlbumRequest request) {
        String uuid = UUID.randomUUID().toString();
        AlbumEntity album = new AlbumEntity(uuid, request.name());
        return albumRepository.save(album);
    }
}

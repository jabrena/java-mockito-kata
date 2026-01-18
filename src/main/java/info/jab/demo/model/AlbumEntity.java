package info.jab.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("albums")
public record AlbumEntity(
    @Id UUID id,
    String name
) { }

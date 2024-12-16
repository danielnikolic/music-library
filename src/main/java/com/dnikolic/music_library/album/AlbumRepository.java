package com.dnikolic.music_library.album;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
	Optional<List<Album>> findAlbumByName(String name);
}

package com.dnikolic.music_library.album;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.dnikolic.music_library.artist.Artist;
import com.dnikolic.music_library.artist.ArtistRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final ArtistRepository artistRepository;

	public List<Album> getAllAlbums() {
		List<Album> albumList = albumRepository.findAll();
		return albumList;
	}

	public List<Album> getAllAlbumsByArtistId(Integer id) {
		boolean exists = artistRepository.existsById(id);

		if (!exists) {
			throw new IllegalArgumentException("This artist does not exist!");
		}

		List<Album> ret = new ArrayList<Album>();
		List<Album> albums = albumRepository.findAll();

		for (Album album : albums) {
			if (album.getArtist().getId() == id) {
				ret.add(album);
			}
		}

		return ret;
	}

	public Album getAlbumById(Integer id) {
		boolean exists = albumRepository.existsById(id);

		if (!exists) {
			throw new IllegalArgumentException("This album does not exist!");
		}

		return albumRepository.findById(id).get();
	}

	public void addNewAlbum(Album album) {
		List<Artist> artistList = artistRepository.findAll();

		if (artistList.isEmpty()) {
			throw new IllegalArgumentException("Try adding a new artist first!");
		}

		int size = albumRepository.findAlbumByName(album.getName()).get().size();

		if (size > 0) {
			List<Album> albumList = albumRepository.findAlbumByName(album.getName()).get();

			for (Album a : albumList) {
				if (a.getArtist().getId() == album.getArtist().getId()) {
					throw new IllegalArgumentException("This album already exists!");
				}
			}
		}

		albumRepository.save(album);
	}

	public void deleteAlbum(Integer id) {
		if (!albumRepository.existsById(id)) {
			throw new IllegalArgumentException("This album does not exist!");
		}

		albumRepository.deleteById(id);
	}

	@Transactional
	public void updateAlbum(Integer id, String name, LocalDate releaseDate) {
		Album albumToUpdate = albumRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("This album does not exist!"));

		if (name != null && name.length() > 0 && !Objects.equals(albumToUpdate.getName(), name)) {
			int size = albumRepository.findAlbumByName(name).get().size();

			if (size > 0) {
				List<Album> albumList = albumRepository.findAlbumByName(name).get();

				for (Album album : albumList) {
					if (album.getArtist().getId() == albumToUpdate.getArtist().getId()) {
						throw new IllegalArgumentException("Album name taken!");
					}
				}
			}
			albumToUpdate.setName(name);
		}

		if (releaseDate != null && !Objects.equals(albumToUpdate.getReleaseDate(), releaseDate)) {
			albumToUpdate.setReleaseDate(releaseDate);
		}
	}
}

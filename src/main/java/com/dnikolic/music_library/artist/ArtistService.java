package com.dnikolic.music_library.artist;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistService {
	private final ArtistRepository artistRepository;

	public List<Artist> getAllArtists() {
		List<Artist> artistList = artistRepository.findAll();
		return artistList;
	}

	public Artist getArtistById(Integer id) {
		boolean exists = artistRepository.existsById(id);

		if (!exists) {
			throw new IllegalArgumentException("This artist does not exist!");
		}

		return artistRepository.findById(id).get();
	}

	public Artist getArtistByName(String name) {
		Optional<Artist> artist = artistRepository.findArtistByName(name);

		if (!artist.isPresent()) {
			throw new IllegalArgumentException("This artist does not exist!");
		}

		return artist.get();
	}

	public void addNewArtist(Artist artist) {
		Optional<Artist> artistOptional = artistRepository.findArtistByName(artist.getName());

		if (artistOptional.isPresent()) {
			throw new IllegalArgumentException("This artist already exists!");
		}

		artistRepository.save(artist);
	}

	public void deleteArtist(Integer id) {
		if (!artistRepository.existsById(id)) {
			throw new IllegalArgumentException("This artist does not exist!");
		}

		artistRepository.deleteById(id);
	}

	@Transactional
	public void updateArtist(Integer id, String name, Genre genre) {
		Artist artist = artistRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("This artist does not exist!"));

		if (name != null && name.length() > 0 && !Objects.equals(artist.getName(), name)) {
			Optional<Artist> artistOptional = artistRepository.findArtistByName(name);

			if (artistOptional.isPresent()) {
				throw new IllegalArgumentException("Artist name taken!");
			}

			artist.setName(name);
		}

		if (genre != null && !Objects.equals(artist.getGenre(), genre)) {
			artist.setGenre(genre);
		}
	}
}

package com.dnikolic.music_library.artist;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "artist")
public class ArtistController {
	private final ArtistService artistService;

	@GetMapping
	public ResponseEntity<List<Artist>> getAllArtists() {
		return new ResponseEntity<>(artistService.getAllArtists(), HttpStatus.OK);
	}

	@GetMapping(path = "id/{id}")
	public ResponseEntity<Artist> getArtistById(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(artistService.getArtistById(id), HttpStatus.OK);
	}

	@GetMapping(path = "name/{name}")
	public ResponseEntity<Artist> getArtistByName(@PathVariable("name") String name) {
		return new ResponseEntity<>(artistService.getArtistByName(name), HttpStatus.OK);
	}

	@PostMapping
	public void addNewArtist(@RequestBody Artist artist) {
		artistService.addNewArtist(artist);
	}

	@DeleteMapping(path = "{id}")
	public void deleteArtist(@PathVariable("id") Integer id) {
		artistService.deleteArtist(id);
	}

	@PutMapping(path = "{id}")
	public void updateArtist(@PathVariable("id") Integer id, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "genre", required = false) Genre genre) {
		artistService.updateArtist(id, name, genre);
	}
}

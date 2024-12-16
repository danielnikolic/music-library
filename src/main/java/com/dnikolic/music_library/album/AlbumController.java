package com.dnikolic.music_library.album;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "album")
public class AlbumController {
	private final AlbumService albumService;

	@GetMapping
	public ResponseEntity<List<Album>> getAllAlbums() {
		return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);
	}

	@GetMapping(path = "artist/{artistId}")
	public ResponseEntity<List<Album>> getAllAlbumsByArtistId(@PathVariable("artistId") Integer id) {
		return new ResponseEntity<>(albumService.getAllAlbumsByArtistId(id), HttpStatus.OK);
	}

	@GetMapping(path = "id/{id}")
	public ResponseEntity<Album> getAlbumById(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
	}

	@PostMapping
	public void addNewAlbum(@RequestBody Album album) {
		albumService.addNewAlbum(album);
	}

	@DeleteMapping(path = "{id}")
	public void deleteAlbum(@PathVariable("id") Integer id) {
		albumService.deleteAlbum(id);
	}

	@PutMapping(path = "{id}")
	public void updateAlbum(@PathVariable("id") Integer id, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "releaseDate", required = false) String releaseDate) {
		LocalDate date = null;
		if (releaseDate != null) {
			date = LocalDate.parse(releaseDate);
		}

		albumService.updateAlbum(id, name, date);
	}
}

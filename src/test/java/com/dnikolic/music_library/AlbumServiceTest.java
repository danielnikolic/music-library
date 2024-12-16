package com.dnikolic.music_library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.dnikolic.music_library.artist.*;
import com.dnikolic.music_library.album.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {
	@Mock
	private AlbumRepository albumRepository;

	@Mock
	private ArtistRepository artistRepository;

	@InjectMocks
	private AlbumService albumService;

	@Test
	void testGetAllAlbums() {
		Artist artist1 = new Artist(1, "Radiohead", Genre.ROCK, null);
		Artist artist2 = new Artist(2, "Japanese Breakfast", Genre.INDIE, null);
		Artist artist3 = new Artist(3, "Kendrick Lamar", Genre.HIP_HOP, null);

		Album album1 = new Album(1, "In Rainbows", LocalDate.of(2007, 10, 10), null, artist1);
		Album album2 = new Album(2, "Jubilee", LocalDate.of(2021, 6, 4), null, artist2);
		Album album3 = new Album(3, "To Pimp A Butterfly", LocalDate.of(2015, 3, 15), null, artist3);

		given(albumRepository.findAll()).willReturn(List.of(album1, album2, album3));
		List<Album> albums = albumService.getAllAlbums();

		assertThat(albums).hasSize(3);

		assertThat(albums.get(0).getName()).isEqualTo("In Rainbows");
		assertThat(albums.get(0).getAge()).isEqualTo(17);

		assertThat(albums.get(1).getName()).isEqualTo("Jubilee");
		assertThat(albums.get(1).getAge()).isEqualTo(3);

		assertThat(albums.get(2).getName()).isEqualTo("To Pimp A Butterfly");
		assertThat(albums.get(2).getAge()).isEqualTo(9);

		verify(albumRepository).findAll();
	}

	@Test
	void testAddNewAlbum() {
		Artist artist = new Artist(1, "Radiohead", Genre.ROCK, null);
		Album album = new Album(1, "In Rainbows", LocalDate.of(2007, 10, 10), null, artist);

		given(artistRepository.findAll()).willReturn(List.of(artist));
		given(albumRepository.findAlbumByName("In Rainbows")).willReturn(Optional.of(List.of()));

		albumService.addNewAlbum(album);

		verify(albumRepository).save(album);
	}

	@Test
	void testAddNewAlbum_ArtistNotFound() {
		Album album = new Album(1, "In Rainbows", LocalDate.of(2007, 10, 10), null, null);

		given(artistRepository.findAll()).willReturn(List.of());

		assertThatThrownBy(() -> albumService.addNewAlbum(album))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Try adding a new artist first!");

		verify(artistRepository).findAll();
	}

	@Test
	void testDeleteAlbum() {
		given(albumRepository.existsById(1)).willReturn(true);
		doNothing().when(albumRepository).deleteById(1);

		albumService.deleteAlbum(1);

		verify(albumRepository).existsById(1);
		verify(albumRepository).deleteById(1);
	}

	@Test
	void testDeleteAlbum_NotFound() {
		given(albumRepository.existsById(1)).willReturn(false);

		assertThatThrownBy(() -> albumService.deleteAlbum(1))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("This album does not exist!");

		verify(albumRepository).existsById(1);
	}

	@Test
	void testUpdateAlbum() {
		Artist artist = new Artist(1, "Radiohead", Genre.ROCK, null);
		Album album = new Album(1, "In Rainbows", LocalDate.of(2007, 10, 10), null, artist);

		assertThat(album.getName()).isEqualTo("In Rainbows");
		assertThat(album.getReleaseDate()).isEqualTo(LocalDate.of(2007, 10, 10));

		given(albumRepository.findById(1)).willReturn(Optional.of(album));
		given(albumRepository.findAlbumByName("Kid A")).willReturn(Optional.of(Collections.emptyList()));
		albumService.updateAlbum(1, "Kid A", LocalDate.of(2000, 10, 2));

		verify(albumRepository).findById(1);
		assertThat(album.getName()).isEqualTo("Kid A");
		assertThat(album.getReleaseDate()).isEqualTo(LocalDate.of(2000, 10, 2));
	}
}

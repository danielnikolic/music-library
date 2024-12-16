package com.dnikolic.music_library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.dnikolic.music_library.artist.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {
	@Mock
	private ArtistRepository artistRepository;

	@InjectMocks
	private ArtistService artistService;

	@Test
	void testGetAllArtists() {
		Artist artist1 = new Artist(1, "Radiohead", Genre.ROCK, null);
		Artist artist2 = new Artist(2, "Japanese Breakfast", Genre.INDIE, null);
		Artist artist3 = new Artist(3, "Kendrick Lamar", Genre.HIP_HOP, null);

		given(artistRepository.findAll()).willReturn(List.of(artist1, artist2, artist3));
		List<Artist> artists = artistService.getAllArtists();

		assertThat(artists).hasSize(3);

		assertThat(artists.get(0).getName()).isEqualTo("Radiohead");
		assertThat(artists.get(0).getGenre()).isEqualTo(Genre.ROCK);

		assertThat(artists.get(1).getName()).isEqualTo("Japanese Breakfast");
		assertThat(artists.get(1).getGenre()).isEqualTo(Genre.INDIE);

		assertThat(artists.get(2).getName()).isEqualTo("Kendrick Lamar");
		assertThat(artists.get(2).getGenre()).isEqualTo(Genre.HIP_HOP);

		verify(artistRepository).findAll();
	}

	@Test
	void testAddNewArtist() {
		Artist artist = new Artist(1, "Nine Inch Nails", Genre.ROCK, null);
		given(artistRepository.save(artist)).willReturn(new Artist(1, "Nine Inch Nails", Genre.ROCK, null));

		artistService.addNewArtist(artist);
		verify(artistRepository).save(artist);
	}

	@Test
	void testDeleteArtist() {
		given(artistRepository.existsById(1)).willReturn(true);
		doNothing().when(artistRepository).deleteById(1);

		artistService.deleteArtist(1);

		verify(artistRepository).existsById(1);
		verify(artistRepository).deleteById(1);
	}

	@Test
	void testUpdateArtist() {
		Artist artist = new Artist(1, "The Cure", Genre.POP, null);

		given(artistRepository.findById(1)).willReturn(Optional.of(artist));

		assertThat(artist.getName()).isEqualTo("The Cure");
		assertThat(artist.getGenre()).isEqualTo(Genre.POP);

		artistService.updateArtist(1, null, Genre.ROCK);

		verify(artistRepository).findById(1);
		assertThat(artist.getName()).isEqualTo("The Cure");
		assertThat(artist.getGenre()).isEqualTo(Genre.ROCK);

		artistService.updateArtist(1, "Nine Inch Nails", null);

		assertThat(artist.getName()).isEqualTo("Nine Inch Nails");
		assertThat(artist.getGenre()).isEqualTo(Genre.ROCK);
	}
}

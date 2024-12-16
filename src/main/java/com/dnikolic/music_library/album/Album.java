package com.dnikolic.music_library.album;

import com.dnikolic.music_library.artist.Artist;
import java.time.LocalDate;
import java.time.Period;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	private LocalDate releaseDate;

	@Transient
	@Getter(AccessLevel.NONE)
	private Integer age;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "artist_id", nullable = false)
	private Artist artist;

	public int getAge() {
		return Period.between(this.releaseDate, LocalDate.now()).getYears();
	}
}

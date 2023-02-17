package com.dnikolic.musiclibrary.artist;

import com.dnikolic.musiclibrary.album.Album;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 1, max = 30)
    @Column(unique = true)
    private String name;

    private Genre genre;

    @JsonIgnore
    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Album> albums;
}

package fr.popcorncine.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {

    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private String mediaType;
    private double popularity;
}

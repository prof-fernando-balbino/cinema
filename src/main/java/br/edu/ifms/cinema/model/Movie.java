package br.edu.ifms.cinema.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie {
    
    private Integer id;
    private String title;
    private String original_title;
    private String original_language;
    private String overview;
    private String release_date;
    private String poster_path;
    private Boolean adult;
    private Boolean video;

}

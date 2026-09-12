package br.edu.ifms.cinema.model;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class MovieDetails extends Movie {
    
    private Genre[] genres;
    private String tagline;

}

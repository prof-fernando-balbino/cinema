package br.edu.ifms.cinema.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class MovieResults {
    
    private Integer page;
    private Movie[] results;
    private Integer total_pages;
    private Integer total_results;

}

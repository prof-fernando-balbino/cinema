package br.edu.ifms.cinema.model;

import java.util.List;

import org.springframework.hateoas.EntityModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class MovieResultsModel {
    
    private Integer page;
    private List<EntityModel<Movie>> results;
    private Integer total_pages;
    private Integer total_results;

}

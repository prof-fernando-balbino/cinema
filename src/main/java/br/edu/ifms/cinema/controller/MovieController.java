package br.edu.ifms.cinema.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifms.cinema.model.MovieResults;
import br.edu.ifms.cinema.service.MovieService;

@RestController
public class MovieController {
    
    private final MovieService movieService;

    //método construtor
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/filmes")
    public MovieResults buscarTodos(@RequestParam(defaultValue = "1") Integer page) {
        return movieService.buscarFilmes(page);
    }

}

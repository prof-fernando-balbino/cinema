package br.edu.ifms.cinema.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifms.cinema.model.MovieDetails;
import br.edu.ifms.cinema.model.MovieResults;
import br.edu.ifms.cinema.service.MovieService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MovieController {
    
    private final MovieService movieService;

    //método construtor
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // @GetMapping("/filmes")
    // public MovieResults buscarTodos(@RequestParam(defaultValue = "1") Integer page) {
    //     return movieService.buscarFilmes(page);
    // }
    @GetMapping("/filmes")
    public EntityModel<MovieResults> buscarTodos(@RequestParam(defaultValue = "1") Integer page) {
        MovieResults movieResults = movieService.buscarFilmes(page);

        return EntityModel.of(
            movieResults,
            linkTo(
                methodOn(MovieController.class).buscarTodos(movieResults.getPage() + 1)
            ).withRel("nextPage")
        );
    }

    @GetMapping("/filmes/{id}")
    public MovieDetails buscarPorId(@PathVariable Integer id) {
        return movieService.buscarFilmesPorId(id);
    }

}

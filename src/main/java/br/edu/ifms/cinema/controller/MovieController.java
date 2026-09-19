package br.edu.ifms.cinema.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifms.cinema.model.Movie;
import br.edu.ifms.cinema.model.MovieDetails;
import br.edu.ifms.cinema.model.MovieResults;
import br.edu.ifms.cinema.model.MovieResultsModel;
import br.edu.ifms.cinema.service.MovieService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

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

        int next = movieResults.getPage() == movieResults.getTotal_pages() ?
                       movieResults.getTotal_pages() : movieResults.getPage() + 1;
        int previous = movieResults.getPage() == 1 ? 1 : movieResults.getPage() - 1;

        return EntityModel.of(
            movieResults,
            linkTo( methodOn(MovieController.class).buscarTodos(1) ).withRel("firstPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(previous) ).withRel("previousPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(next) ).withRel("nextPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(movieResults.getTotal_pages()) ).withRel("lastPage")
        );
    }

    @GetMapping("/filmes/model")
    public EntityModel<MovieResultsModel> buscarTodosModel(@RequestParam(defaultValue = "1") Integer page) {
        MovieResults movieResults = movieService.buscarFilmes(page);

        int next = movieResults.getPage() == movieResults.getTotal_pages() ?
                       movieResults.getTotal_pages() : movieResults.getPage() + 1;
        int previous = movieResults.getPage() == 1 ? 1 : movieResults.getPage() - 1;

        MovieResultsModel movieResultsModel = new MovieResultsModel();
        movieResultsModel.setPage(page);
        movieResultsModel.setTotal_pages(movieResults.getTotal_pages());
        movieResultsModel.setTotal_results(movieResults.getTotal_results());

        List<EntityModel<Movie>> lista = new ArrayList<>();

        for ( Movie movie : movieResults.getResults() ) {
            EntityModel<Movie> movieModel = 
              EntityModel.of(
                movie,
                linkTo( methodOn(MovieController.class).buscarPorId(movie.getId()) ).withRel("details")
              );
            lista.add(movieModel);
        }
        
        movieResultsModel.setResults(lista);

        return EntityModel.of(
            movieResultsModel,
            linkTo( methodOn(MovieController.class).buscarTodos(1) ).withRel("firstPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(previous) ).withRel("previousPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(next) ).withRel("nextPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(movieResults.getTotal_pages()) ).withRel("lastPage")
        );
    }

    @GetMapping("/filmes/{id}")
    public MovieDetails buscarPorId(@PathVariable Integer id) {
        return movieService.buscarFilmesPorId(id);
    }

}

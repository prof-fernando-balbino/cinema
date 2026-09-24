package br.edu.ifms.cinema.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    //método construtor, usado para injetar uma instância de MovieService na classe controladora
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Método em que (1) os links para paginação foram adicionados a MovieResults
    //               (2) o link para acesso ao regitro detalhado foi adicionado a cada Movie 
    @GetMapping("/filmes")
    public EntityModel<MovieResultsModel> buscarTodos(
                    @RequestParam(required = false) String titulo, @RequestParam(defaultValue = "1") Integer page) {
        MovieResults movieResults;

        if (titulo == null)
            movieResults = movieService.buscarFilmes(page);
        else
            movieResults = movieService.buscarFilmesPorTitulo(titulo, page);

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
            linkTo( methodOn(MovieController.class).buscarTodos(titulo, 1) ).withRel("firstPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(titulo, previous) ).withRel("previousPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(titulo, next) ).withRel("nextPage"),
            linkTo( methodOn(MovieController.class).buscarTodos(titulo, movieResults.getTotal_pages()) ).withRel("lastPage")
        );
    }

    // Método que fornece um endpoint para acesso ao registro detalhado de um filme
    @GetMapping("/filmes/{id}")
    public MovieDetails buscarPorId(@PathVariable Integer id) {
        return movieService.buscarFilmesPorId(id);
    }

    // Método que fornece um endpoint para retornar a imagem do poster do filme
    @GetMapping("/filmes/{id}/poster")
    public ResponseEntity<byte[]> buscarPoster(@PathVariable Integer id) {
        MovieDetails movieDetails = buscarPorId(id);

        byte[] imagem = movieService.buscarPoster(movieDetails.getPoster_path());

        MediaType mediaType;

        if (movieDetails.getPoster_path().endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (movieDetails.getPoster_path().endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok().contentType(mediaType).body(imagem);
    }
    
    // Método em que somente os links para paginação foram adicionados a MovieResults 
    // @GetMapping("/filmes")
    // public EntityModel<MovieResults> buscarTodos(@RequestParam(defaultValue = "1") Integer page) {
    //     MovieResults movieResults = movieService.buscarFilmes(page);

    //     int next = movieResults.getPage() == movieResults.getTotal_pages() ?
    //                    movieResults.getTotal_pages() : movieResults.getPage() + 1;
    //     int previous = movieResults.getPage() == 1 ? 1 : movieResults.getPage() - 1;

    //     return EntityModel.of(
    //         movieResults,
    //         linkTo( methodOn(MovieController.class).buscarTodos(1) ).withRel("firstPage"),
    //         linkTo( methodOn(MovieController.class).buscarTodos(previous) ).withRel("previousPage"),
    //         linkTo( methodOn(MovieController.class).buscarTodos(next) ).withRel("nextPage"),
    //         linkTo( methodOn(MovieController.class).buscarTodos(movieResults.getTotal_pages()) ).withRel("lastPage")
    //     );
    // }

}

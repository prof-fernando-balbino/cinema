package br.edu.ifms.cinema.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifms.cinema.model.MovieResults;

@Service
public class MovieService {
    
    private final RestClient restClient;

    //instanciar um objeto do tipo RestClient
    public MovieService(@Value("${tmdb.api.token}") String token) {
        restClient = RestClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
    }

    public MovieResults buscarFilmes(Integer page) {
        String uri = UriComponentsBuilder
            .fromPath("/discover/movie")
            .queryParam("language", "pt-BR")
            .queryParam("page", page)
            .toUriString();

        return restClient.get().uri(uri).retrieve().body(MovieResults.class);
    }

}

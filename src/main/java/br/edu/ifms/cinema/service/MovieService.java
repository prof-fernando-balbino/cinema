package br.edu.ifms.cinema.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifms.cinema.model.MovieDetails;
import br.edu.ifms.cinema.model.MovieResults;

@Service
public class MovieService {
    
    private final RestClient restClient;
    private final RestClient restClientImage;

    //Instancia objetos do tipo RestClient
    public MovieService(@Value("${tmdb.api.token}") String token) {
        restClient = RestClient.builder()
            .baseUrl("https://api.themoviedb.org/3")
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
        
        restClientImage = RestClient.builder()
            .baseUrl("https://image.tmdb.org/t/p/original")
            .build();
    }

    //Retorna todos os filmes disponíveis na API
    public MovieResults buscarFilmes(Integer page) {
        String uri = UriComponentsBuilder
            .fromPath("/discover/movie")
            .queryParam("language", "pt-BR")
            .queryParam("page", page)
            .toUriString();

        return restClient.get().uri(uri).retrieve().body(MovieResults.class);
    }

    //Retorna apenas os filmes que contêm palavras-chaves no título
    public MovieResults buscarFilmesPorTitulo(String query, Integer page) {
        String uri = UriComponentsBuilder
            .fromPath("/search/movie")
            .queryParam("query", query)
            .queryParam("language", "pt-BR")
            .queryParam("page", page)
            .toUriString();

        return restClient.get().uri(uri).retrieve().body(MovieResults.class);
    }

    //Retorna os detalhes do filme com o id informado como parâmetro
    public MovieDetails buscarFilmesPorId(Integer id) {
        String uri = UriComponentsBuilder
            .fromPath("/movie/{id}")
            .queryParam("language", "pt-BR")
            .buildAndExpand(id)
            .toUriString();

        return restClient.get().uri(uri).retrieve().body(MovieDetails.class);
    }

    //Retorna o arquivo de imagem do poster do filme
    public byte[] buscarPoster(String filename) {
        return restClientImage.get().uri(filename).retrieve().body(byte[].class);
    }

}

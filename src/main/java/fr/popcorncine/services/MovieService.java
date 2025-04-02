package fr.popcorncine.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.popcorncine.DTO.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieService {

    private final String API_KEY = "5c5b9e4f8d54f81af5f710d49fb31977";
    private final String BASE_URL = "https://api.themoviedb.org/3";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<MovieDTO> searchMovieOrSerie(String query) {
        String url = BASE_URL + "/search/multi?api_key=" + API_KEY + "&query=" + query;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<MovieDTO> movies = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            for (JsonNode result : root.path("results")){
                MovieDTO movie = new MovieDTO();
                movie.setId(result.path("id").asInt());
                movie.setTitle(result.path("title").asText(result.path("name").asText()));
                movie.setOverview(result.path("overview").asText());
                movie.setPosterPath("https://image.tmdb.org/t/p/w500" + result.path("poster_path").asText());
                movie.setReleaseDate(result.path("release_date").asText(result.path("first_air_date").asText()));
                movie.setMediaType(result.path("media_type").asText());
                movie.setPopularity(result.path("popularity").asDouble());

                movies.add(movie);
            }

            movies.sort(Comparator.comparingDouble(MovieDTO::getPopularity).reversed());
        }catch (Exception e){
            e.printStackTrace();
        }
        return movies;
    }
}

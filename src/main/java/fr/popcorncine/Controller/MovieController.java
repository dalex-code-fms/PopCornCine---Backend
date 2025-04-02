package fr.popcorncine.Controller;

import fr.popcorncine.DTO.MovieDTO;
import fr.popcorncine.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovieOrSerie(@RequestParam String query){
        return ResponseEntity.ok(movieService.searchMovieOrSerie(query));
    }
}

package com.catchingup.movies;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }

    @GetMapping("/listAll")
    public Collection<Movie> listAll(){
        return movieService.getAll();
    }

    @GetMapping("/movie/{id}")
    public Movie get(@PathVariable String id){
        Movie movie = movieService.get(id);
        if (movie == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return movie;
    }

    @PostMapping("/movie")
    public Movie createMovie(@RequestBody Movie newMovie) throws IOException {
        Movie movie = new Movie();
        movie.setTitle(newMovie.getTitle());
        movie.setOriginalTitle(newMovie.getOriginalTitle());
        movie.setReleaseYear(newMovie.getReleaseYear());
        movie.setRating(newMovie.getRating());
        movie.setCountry(newMovie.getCountry());
        movie.setId(Long.toString(Math.abs(UUID.randomUUID().getLeastSignificantBits())));
        movie.setOriginalFileName(newMovie.getOriginalFileName());
        movie.setData(newMovie.getData());
        movieService.save(movie);
        return movie;
    }

    @PostMapping("/movieImage/{id}")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile data, @PathVariable String id) throws IOException {
        System.out.println("Looking for " + id);
        Movie movie = movieService.get(id);
        if (movie == null) {
            return new ResponseEntity<>("Could not find data", HttpStatus.SERVICE_UNAVAILABLE);
        }
        movie.setData(data.getBytes());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping("/movie/{id}")
    public Movie delete(String id){
        Movie movie = movieService.get(id);
        if (movie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return movieService.remove(id);
    }
}

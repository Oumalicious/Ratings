package com.catchingup.movies.controllers;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import com.catchingup.movies.entities.MovieEntity;
import com.catchingup.movies.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static com.catchingup.movies.constants.MovieConstants.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@Slf4j
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping()
    public ResponseEntity<Collection<MovieEntity>> getAll(){
        return ResponseEntity.ok().body(movieService.getAll());
    }

    @GetMapping("/")
    public ResponseEntity<Page<MovieEntity>> getAllWithPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(movieService.getAllWithPagination(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieEntity> get(@PathVariable("id") Long id){
        MovieEntity movie = movieService.getMovie(id);
        if (movie == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return ResponseEntity.ok().body(movie);
    }

    @PostMapping()
    public ResponseEntity<MovieEntity> createMovie(@RequestBody MovieEntity newMovie) {
        return ResponseEntity.created(URI.create("/movies/id")).body(movieService.save(newMovie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieEntity> updateMovie(@PathVariable Long id, @RequestBody MovieEntity movie) {
        return ResponseEntity.accepted().body(movieService.updateMovie(id, movie));
    }


    @PutMapping("/photo")
    public ResponseEntity<String> uploadImage(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(movieService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

    @DeleteMapping("/movie/{id}")
    public void delete(Long id){
        MovieEntity movie = movieService.getMovie(id);
        if (movie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        movieService.remove(id);
    }
}

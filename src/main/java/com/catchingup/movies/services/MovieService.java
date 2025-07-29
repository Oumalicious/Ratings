package com.catchingup.movies.services;


import java.io.IOException;
import java.util.Collection;

import com.catchingup.movies.entities.MovieEntity;
import com.catchingup.movies.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepo;

    public Collection<MovieEntity> getAll(){
        return movieRepo.findAll();
    }

    public Page<MovieEntity> getAllWithPagination(int page, int size){
        return movieRepo.findAll(PageRequest.of(page, size, Sort.by("originalTitle")));
    }

    public MovieEntity getMovie(Long id){
        return movieRepo.findById(id).orElseThrow(() -> new RuntimeException("Movie not found."));
    }

    public void remove(Long id){
        movieRepo.deleteById(id);
    }

//    public MovieEntity save(MovieEntity movie, byte[] byteString){
    public MovieEntity save(MovieEntity movie){
        log.info("Saving new movie {}", movie.getTitle());
        return movieRepo.save(movie);
    }

    public MovieEntity updateMovie(Long id, MovieEntity updatedMovie) {
        MovieEntity movie = getMovie(id);
        log.info("Updating {}", movie.getTitle());
        movie.setCountry(updatedMovie.getCountry());
        movie.setRating(updatedMovie.getRating());
        movie.setOriginalTitle(updatedMovie.getOriginalTitle());
        movie.setReleaseYear(updatedMovie.getReleaseYear());
        return movieRepo.save(movie);
    }
    public String uploadPhoto(@RequestParam("id") Long id, @RequestParam("file")MultipartFile file) throws IOException {
        log.info("Saving picture for user ID: {}", id);
        MovieEntity movie = getMovie(id);
        movie.setData(file.getBytes());
        movie.setOriginalFileName(file.getOriginalFilename());
        movie.setFileType(file.getContentType());
        movieRepo.save(movie);
        log.info("Uploaded image {}", movie.getOriginalFileName());
        return movie.getOriginalFileName();
    }
}

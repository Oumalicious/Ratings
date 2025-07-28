package com.catchingup.movies.services;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.catchingup.movies.constants.MovieConstants.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
        movieRepo.save(movie);
//        String photoUrl = photoFunction.apply(id, file);
//        movie.setPhotoUrl(photoUrl);
        log.info("Uploaded image {}", movie.getOriginalFileName());
        return movie.getOriginalFileName();
    }

    private final Function<String, String> fileExtension = fileName -> Optional.of(fileName).filter(name -> name.contains("."))
            .map(name -> name.substring(fileName.lastIndexOf("."))).orElse(".png");

    private final BiFunction<Long, MultipartFile, String> photoFunction = (id, image) -> {
        String fileName = fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileLocation)) {
                Files.createDirectories(fileLocation);
            }

            Files.copy(image.getInputStream(), fileLocation.resolve(id + fileName ), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/movies/image/" + id + fileName).toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save Image");
        }
    };
}

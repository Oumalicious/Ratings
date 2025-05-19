package com.catchingup.movies;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class MovieService {
    
    private Map<String, Movie> db = new HashMap<>(){{
        String uuid = Long.toString(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        put(uuid, new Movie(uuid, "SampleMovie", "SampleOriginalMovie", 9999, 10, "USA", null, null));
    }};

    public Collection<Movie> getAll(){
        return db.values();
    }

    public Movie get(String id){
        return db.get(id);
    }

    public Movie remove(String id){
        return db.remove(id);
    }

    public void save(Movie movie){
        db.put(movie.getId(), movie);
    }
}

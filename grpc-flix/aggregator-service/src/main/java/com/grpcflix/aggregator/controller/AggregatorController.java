package com.grpcflix.aggregator.controller;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId){
        return this.userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user")
    public void setUserGenre(@RequestBody UserGenre userGenre){
        this.userMovieService.setUserGenre(userGenre);
    }

}

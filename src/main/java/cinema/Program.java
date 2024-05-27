package cinema;

import cinema.models.Movie;
import cinema.services.MovieService;
import cinema.services.ScreeningService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Program implements CommandLineRunner {
    private final MovieService movieService;
    private final ScreeningService screeningService;

    public Program(MovieService movieService, ScreeningService screeningService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Program.class, args);
    }

    @Override
    public void run(String... args) {
//        String highestImdbID = movieService.getHighestImdbID();
//        List<Movie> movies = movieService.getMovies(highestImdbID, 100);
        screeningService.createScreenings(10);

        System.out.println("Complete");
    }
}

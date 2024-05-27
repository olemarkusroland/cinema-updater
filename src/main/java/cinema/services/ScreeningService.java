package cinema.services;

import cinema.models.Movie;
import cinema.models.Screening;
import cinema.repositories.MovieRepository;
import cinema.repositories.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
public class ScreeningService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    private final Random random = new Random();

    public void createScreenings(int count) {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            System.out.println("No movies available in the database.");
            return;
        }

        for (int i = 0; i < count; i++) {
            Movie randomMovie = movies.get(random.nextInt(movies.size()));
            Screening screening = new Screening();
            screening.movieId = randomMovie.imdbID;
            screening.screenNumber = random.nextInt(10) + 1;  // Random screen number between 1 and 10
            screening.startsAt = randomDateTimeWithinNext10Days();

            screeningRepository.save(screening);
        }
    }

    private OffsetDateTime randomDateTimeWithinNext10Days() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        int randomDays = random.nextInt(10);
        int randomHours = random.nextInt(24);
        int randomMinutes = random.nextInt(60);
        return now.plus(randomDays, ChronoUnit.DAYS)
                .plus(randomHours, ChronoUnit.HOURS)
                .plus(randomMinutes, ChronoUnit.MINUTES);
    }
}

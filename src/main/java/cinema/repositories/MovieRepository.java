package cinema.repositories;

import cinema.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    @Query("SELECT m FROM Movie m ORDER BY m.imdbID DESC LIMIT 1")
    Movie findTopByOrderByImdbIDDesc();
}

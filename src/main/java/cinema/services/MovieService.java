package cinema.services;

import cinema.models.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import cinema.repositories.MovieRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Value("${omdb.api-key}")
    private String apiKey;

    @Autowired
    private MovieRepository movieRepository;

    public Movie getMovie(String imdbID) {
        String url = "https://www.omdbapi.com/?&apikey=" + apiKey + "&i=" + imdbID;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            Movie movie = objectMapper.treeToValue(jsonNode, Movie.class);
            if (isValidMovie(movie)) {
                OffsetDateTime now = OffsetDateTime.now();

                movie.createdAt = now;
                movie.updatedAt = now;
                movieRepository.save(movie);
            }
            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Movie> getMovies(String startImdbID, int count) {
        List<Movie> movies = new ArrayList<>();
        int startId = Integer.parseInt(startImdbID.substring(2));
        for (int i = 0; i < count; i++) {
            String imdbID = String.format("tt%07d", startId + i);
            Movie movie = getMovie(imdbID);
            if (movie != null) {
                movies.add(movie);
            }
        }
        return movies;
    }

    public String getHighestImdbID() {
        Movie movie = movieRepository.findTopByOrderByImdbIDDesc();
        return (movie != null) ? movie.imdbID : "tt0000000";
    }

    private boolean isValidMovie(Movie movie) {
        if (movie == null) return false;
        if ("N/A".equals(movie.title) || "N/A".equals(movie.year) || "N/A".equals(movie.released) ||
                "N/A".equals(movie.plot) || "N/A".equals(movie.poster)) {
            return false;
        }
        return "movie".equals(movie.type);
    }
}
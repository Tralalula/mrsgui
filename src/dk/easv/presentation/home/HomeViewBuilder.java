package dk.easv.presentation.home;

import dk.easv.entities.Movie;
import dk.easv.entities.TopMovie;
import dk.easv.entities.UserSimilarity;
import dk.easv.presentation.app.AppModel;
import dk.easv.presentation.widgets.LabelWidgets;
import dk.easv.presentation.widgets.MoviePoster;
import dk.easv.presentation.widgets.ScrollPaneWidgets;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;
import java.util.stream.Collectors;

public class HomeViewBuilder implements Builder<Region> {
    private final AppModel model;
    private HorizontalPaginator<Movie> topMovieSeen;
    private HorizontalPaginator<Movie> topAvgNotSeen;
    private HorizontalPaginator<TopMovie> topFromSimilar;

    public HomeViewBuilder(AppModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        VBox vbox = new VBox();

        topMovieSeen = createTopMovieSeen();
        topAvgNotSeen = createTopAvgNotSeen();
        topFromSimilar = createTopFromSimilar();

        model.getObsTopMovieSeen().addListener((ListChangeListener<Movie>) change -> updateTopMovieSeen());
        model.getObsTopMovieNotSeen().addListener((ListChangeListener<Movie>) change -> updateTopAvgNotSeen());
        model.getObsTopMoviesSimilarUsers().addListener((ListChangeListener<TopMovie>) change -> updateTopFromSimilar());

        vbox.getChildren().addAll(topAvgNotSeen, topFromSimilar, topMovieSeen);
        vbox.getStyleClass().add("home-content");
        vbox.setPadding(new Insets(-16, 0, 0, 0));

        return ScrollPaneWidgets.defaultPageScrollPane(vbox);
    }

    private void updateTopMovieSeen() {
        topMovieSeen = createTopMovieSeen();
    }

    private void updateTopAvgNotSeen() {
        topAvgNotSeen = createTopAvgNotSeen();
    }

    private void updateTopFromSimilar() {
        topFromSimilar = createTopFromSimilar();
    }

    private HorizontalPaginator<Movie> createTopMovieSeen() {
        return new HorizontalPaginator<>(
                model.getObsTopMovieSeen(),
                movie -> createMoviePoster(movie.getTitle()),
                "Top Movie Seen"
        );
    }

    private HorizontalPaginator<Movie> createTopAvgNotSeen() {
        return new HorizontalPaginator<>(
                model.getObsTopMovieNotSeen(),
                movie -> createMoviePoster(movie.getTitle()),
                "Top Movie Not Seen"
        );
    }

    private HorizontalPaginator<TopMovie> createTopFromSimilar() {
        return new HorizontalPaginator<>(
                model.getObsTopMoviesSimilarUsers(),
                movie -> createMoviePoster(movie.getTitle()),
                "Top Movies From Similar Users"
        );
    }


    private VBox createMoviePoster(String text) {
        VBox results = new VBox(8);

        MoviePoster poster = new MoviePoster(getRandomPosterPath(), 150, 224, 10);
        Label title = LabelWidgets.styledLabel(text, "movie-poster-title");

        results.getChildren().addAll(poster, title);

        return results;
    }

    private String getRandomPosterPath() {
        File folder = new File("data");
        FilenameFilter filter = (dir, name) -> name.endsWith(".jpg");

        File[] files = folder.listFiles(filter);

        if (files == null || files.length == 0) return null;

        File selectedFile = files[new Random().nextInt(files.length)];
        return "file:" + selectedFile.getPath();
    }



}

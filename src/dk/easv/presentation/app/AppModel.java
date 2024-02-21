package dk.easv.presentation.app;

import dk.easv.entities.*;
import dk.easv.logic.LogicManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class AppModel {
    private final ObjectProperty<ViewType> activeView = new SimpleObjectProperty<>(ViewType.LOGIN);
    private final ObjectProperty<ViewType> previousView = new SimpleObjectProperty<>(ViewType.LOGIN);

    LogicManager logic = new LogicManager();
    // Models of the data in the view
    private final ObservableList<User>  obsUsers = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieSeen = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieNotSeen = FXCollections.observableArrayList();
    private final ObservableList<UserSimilarity>  obsSimilarUsers = FXCollections.observableArrayList();
    private final ObservableList<TopMovie> obsTopMoviesSimilarUsers = FXCollections.observableArrayList();

    private final SimpleObjectProperty<User> obsLoggedInUser = new SimpleObjectProperty<>();
    private final BooleanProperty isCollapsed = new SimpleBooleanProperty(false);

    public void loadUsers(){
/*        obsUsers.clear();
        obsUsers.addAll(logic.getAllUsers());*/

        performBackgroundTask(() -> {
            return logic.getAllUsers();
        }, result -> {
            obsUsers.clear();
            obsUsers.addAll(result);
        }, error -> {
            error.printStackTrace();
        });
    }

    public void loadData(User user) {
/*        obsTopMovieSeen.clear();
        obsTopMovieSeen.addAll(logic.getTopAverageRatedMovies(user).stream().limit(100).toList());

        obsTopMovieNotSeen.clear();
        obsTopMovieNotSeen.addAll(logic.getTopAverageRatedMoviesUserDidNotSee(user).stream().limit(100).toList());

        obsSimilarUsers.clear();
        obsSimilarUsers.addAll(logic.getTopSimilarUsers(user).stream().limit(100).toList());

        obsTopMoviesSimilarUsers.clear();
        obsTopMoviesSimilarUsers.addAll(logic.getTopMoviesFromSimilarPeople(user).stream().limit(100).toList());*/

        performBackgroundTask(() -> {
            return logic.getTopAverageRatedMovies(user).stream().limit(100).toList();
        }, result -> {
            obsTopMovieSeen.clear();
            obsTopMovieSeen.addAll(result);
        }, error -> {
            error.printStackTrace();
        });

        performBackgroundTask(() -> {
            return logic.getTopAverageRatedMoviesUserDidNotSee(user).stream().limit(100).toList();
        }, result -> {
            obsTopMovieNotSeen.clear();
            obsTopMovieNotSeen.addAll(result);
        }, error -> {
            error.printStackTrace();
        });

        performBackgroundTask(() -> {
            return logic.getTopSimilarUsers(user).stream().limit(100).toList();
        }, result -> {
            obsSimilarUsers.clear();
            obsSimilarUsers.addAll(result);
        }, error -> {
            error.printStackTrace();
        });

        performBackgroundTask(() -> {
            return logic.getTopMoviesFromSimilarPeople(user).stream().limit(100).toList();
        }, result -> {
            obsTopMoviesSimilarUsers.clear();
            obsTopMoviesSimilarUsers.addAll(result);
        }, error -> {
            error.printStackTrace();
        });
    }

    public ObservableList<User> getObsUsers() {
        return obsUsers;
    }

    public ObservableList<Movie> getObsTopMovieSeen() {
        return obsTopMovieSeen;
    }

    public ObservableList<Movie> getObsTopMovieNotSeen() {
        return obsTopMovieNotSeen;
    }

    public ObservableList<UserSimilarity> getObsSimilarUsers() {
        return obsSimilarUsers;
    }

    public ObservableList<TopMovie> getObsTopMoviesSimilarUsers() {
        return obsTopMoviesSimilarUsers;
    }

    public User getObsLoggedInUser() {
        return obsLoggedInUser.get();
    }

    public SimpleObjectProperty<User> obsLoggedInUserProperty() {
        return obsLoggedInUser;
    }

    public void setObsLoggedInUser(User obsLoggedInUser) {
        this.obsLoggedInUser.set(obsLoggedInUser);
    }

    public boolean loginUserFromUsername(String userName) {
        User u = logic.getUser(userName);
        obsLoggedInUser.set(u);
        return u != null;
    }

    public ObjectProperty<ViewType> activeViewProperty() {
        return activeView;
    }

    public ObjectProperty<ViewType> previousViewProperty() {
        return previousView;
    }

    public BooleanProperty isCollapsedProperty() {
        return isCollapsed;
    }

    private <T> void performBackgroundTask(Callable<T> task, Consumer<T> onSuccess, Consumer<Exception> onError) {
        // Hent data i baggrunden så vi ikke bloker Java FX Application Thread (FXAT), hvis forbindelsen f.eks. er langsom
        Task<T> backgroundTask = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return task.call();
            }
        };

        // Håndter successful hentning af data
        backgroundTask.setOnSucceeded(event -> {
            T result = backgroundTask.getValue();
            onSuccess.accept(result);
        });

        // Håndter exceptions
        backgroundTask.setOnFailed(event -> {
            Throwable error = backgroundTask.getException();
            onError.accept((Exception) error);
        });

        // Start ny tråd
        new Thread(backgroundTask).start();
    }

}

package dk.easv.presentation.app;

import dk.easv.presentation.utils.Animations;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class ViewHandler {
    private final AppModel model;
    private Region loginView;
    private Region homeView;

    public ViewHandler(AppModel model) {
        this.model = model;
    }

    public void setLoginView(Region loginView) {
        this.loginView = loginView;
    }

    public void setHomeView(Region homeView) {
        this.homeView = homeView;
    }

    public void changeView(ViewType newView) {
        if (model.activeViewProperty().get() == ViewType.LOGIN && newView == ViewType.HOME) {
            animateTransitionFromLoginToHome();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//
//            }
        }

        model.previousViewProperty().set(model.activeViewProperty().get());
//        model.activeViewProperty().set(newView);
    }

    public void previousView() {
        model.activeViewProperty().set(model.previousViewProperty().get());
    }

    private void animateTransitionFromLoginToHome() {
        ScaleTransition shrink = new ScaleTransition(Duration.seconds(1), loginView);
        shrink.setToX(0.1);
        shrink.setToY(0.1);

        RotateTransition spin = new RotateTransition(Duration.seconds(1), loginView);
        spin.setByAngle(360 * 3);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), loginView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(1));

        SequentialTransition loginSequence = new SequentialTransition(shrink, spin, fadeOut);

        homeView.setScaleX(0.1);
        homeView.setScaleY(0.1);
        homeView.setOpacity(0.0);

        ScaleTransition zoomIn = new ScaleTransition(Duration.seconds(1.5), homeView);
        zoomIn.setToX(1);
        zoomIn.setToY(1);
        zoomIn.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition rotateIn = new RotateTransition(Duration.seconds(1.5), homeView);
        rotateIn.setByAngle(-360);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), homeView);
        fadeIn.setToValue(1.0);

        zoomIn.setDelay(Duration.seconds(0.5));
        rotateIn.setDelay(Duration.seconds(0.5));
        fadeIn.setDelay(Duration.seconds(0.5));

        ScaleTransition bounceBack = new ScaleTransition(Duration.seconds(0.3), homeView);
        bounceBack.setToX(1.0);
        bounceBack.setToY(1.0);
        bounceBack.setDelay(Duration.seconds(2));

        SequentialTransition homeSequence = new SequentialTransition(new ParallelTransition(zoomIn, rotateIn, fadeIn), bounceBack);

        loginSequence.setOnFinished(event -> {
            changeViewPostAnimation(ViewType.HOME);
            homeSequence.play();
        });

        loginSequence.play();
    }


    private void changeViewPostAnimation(ViewType newView) {
        Platform.runLater(() -> {
            model.previousViewProperty().set(model.activeViewProperty().get());
            model.activeViewProperty().set(newView);
        });
    }
}
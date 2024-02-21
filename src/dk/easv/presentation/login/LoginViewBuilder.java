package dk.easv.presentation.login;

import dk.easv.presentation.app.AppModel;
import dk.easv.presentation.app.ViewHandler;
import dk.easv.presentation.app.ViewType;
import dk.easv.presentation.widgets.ButtonWidgets;
import dk.easv.presentation.widgets.LabelWidgets;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;

public class LoginViewBuilder implements Builder<Region> {
    private final AppModel model;
    private final ViewHandler viewHandler;

    public LoginViewBuilder(AppModel model, ViewHandler viewHandler) {
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public Region build() {
        Label titleLabel = LabelWidgets.styledLabel("Sign In", "login-title");
        titleLabel.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setMaxWidth(400);
        gridPane.setMinWidth(400);
        gridPane.setPrefWidth(400);

        Label usernameLabel = LabelWidgets.styledLabel("Username", "login-label");
        TextField usernameTextField = new TextField();
        usernameTextField.getStyleClass().add("login-field");
        GridPane.setMargin(usernameTextField, new Insets(-10, 0, 0, 0));
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameTextField, 0, 1);

        Label passwordLabel = LabelWidgets.styledLabel("Password", "login-label");
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("login-field");
        GridPane.setMargin(passwordField, new Insets(-10, 0, 0, 0));
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 0, 3);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.getStyleClass().add("login-link");
        gridPane.add(forgotPasswordLink, 0, 4);
        GridPane.setMargin(forgotPasswordLink, new Insets(-10, 0, 0, 0));
        GridPane.setHalignment(forgotPasswordLink, HPos.RIGHT);

        Runnable loginLogic = () -> {
            model.loginUserFromUsername(usernameTextField.getText());
            if (model.getObsLoggedInUser() != null) {
                model.loadData(model.getObsLoggedInUser());
                viewHandler.changeView(ViewType.HOME);
            }
        };


        Button continueButton = ButtonWidgets.actionButtonStyle("Continue", "login-button", event -> loginLogic.run());
        gridPane.add(continueButton, 0, 5);
        GridPane.setMargin(continueButton, new Insets(10, 0, 0, 0));
        continueButton.minWidthProperty().bind(gridPane.widthProperty());
        continueButton.maxWidthProperty().bind(gridPane.widthProperty());
        continueButton.setPrefHeight(30);

        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) loginLogic.run();
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) loginLogic.run();
        });

        Label signUpPrompt = LabelWidgets.styledLabel("Not a member yet?", "login-text");
        Hyperlink signUpLink = new Hyperlink("Sign up");
        signUpLink.getStyleClass().add("login-link");

        HBox signUpBox = new HBox(signUpPrompt, signUpLink);
        signUpBox.setAlignment(Pos.CENTER);
        gridPane.add(signUpBox, 0, 6);
        GridPane.setMargin(signUpBox, new Insets(10, 0, 0, 0));

        gridPane.setAlignment(Pos.CENTER);


        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, gridPane);
        layout.setAlignment(Pos.CENTER);
        return layout;
    }


}

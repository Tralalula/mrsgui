package dk.easv.presentation.app;

import dk.easv.presentation.widgets.ButtonWidgets;
import dk.easv.presentation.widgets.IconWidgets;
import dk.easv.presentation.widgets.LabelWidgets;
import dk.easv.presentation.widgets.NavigationGroup;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import java.util.Objects;

public class AppViewBuilder implements Builder<Region> {
    private final AppModel model;
    private final Region loginView;
    private final Region homeView;
    private BorderPane main;
    private Label mrs;

    private final String lightThemePath = Objects.requireNonNull(this.getClass().getResource("../css/lightTheme.css")).toExternalForm();
    private final String darkThemePath = Objects.requireNonNull(this.getClass().getResource("../css/darkTheme.css")).toExternalForm();

    public AppViewBuilder(AppModel model, Region loginView, Region homeView) {
        this.model = model;
        this.loginView = loginView;
        this.homeView = homeView;
    }

    @Override
    public Region build() {
        main = new BorderPane();

        main.getStylesheets().add(darkThemePath);
        main.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("../css/app.css")).toExternalForm());

        main.getStyleClass().add("main");

        Region top = createTop();
        Region left = createLeft();
        Region center = createContent();

        main.setTop(top);
        main.setLeft(left);
        main.setCenter(center);

        BorderPane.setMargin(top, new Insets(0, 0, 8, 0));

        Platform.runLater(mrs::requestFocus);

        model.activeViewProperty().addListener((obs, ov, nv) -> {
            mrs.requestFocus();
            Platform.runLater(mrs::requestFocus);
        });

        return main;
    }

    private Region createContent() {
        loginView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.LOGIN));
        homeView.visibleProperty().bind(model.activeViewProperty().isEqualTo(ViewType.HOME));

        return new StackPane(loginView, homeView);
    }

    private Region createTop() {
        Region topbar = createTopbar();

        topbar.visibleProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.LOGIN));
        topbar.managedProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.LOGIN));

        return topbar;
    }

    private Region createLeft() {
        Region sidebar = createSidebar();

        sidebar.visibleProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.LOGIN));
        sidebar.managedProperty().bind(model.activeViewProperty().isNotEqualTo(ViewType.LOGIN));

        return sidebar;
    }

    private Region createTopbar() {
        HBox results = new HBox();
        results.getStyleClass().add("topbar");

        Button menuIcon = ButtonWidgets.actionIconButton(Material2MZ.MENU, "menu-icon", event -> {
            model.isCollapsedProperty().set(!model.isCollapsedProperty().get());
        });

        mrs = LabelWidgets.styledLabel("MRS", "logo");
        mrs.setWrapText(false);

        Region iconSearchFieldSpacer = new Region();
        iconSearchFieldSpacer.setPrefWidth(10);
        iconSearchFieldSpacer.setMinWidth(10);
        iconSearchFieldSpacer.setMaxWidth(10);

        StackPane searchContainer = new StackPane();
        TextField searchField = new TextField();
        searchField.setPrefWidth(300);
        FontIcon searchIcon = IconWidgets.styledIcon(Material2MZ.SEARCH, "icon-textfield-icon", "search-icon");

        searchContainer.getChildren().addAll(searchField, searchIcon);
        searchField.getStyleClass().add("icon-textfield");
        StackPane.setAlignment(searchIcon, Pos.CENTER_LEFT);

        StackPane.setMargin(searchIcon, new Insets(5));

        searchField.focusedProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                searchIcon.setIconColor(Color.BLACK);
            } else {
                searchIcon.setIconColor(Color.WHITE);
            }
        });

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        ToggleButton themeToggle = new ToggleButton("Dark Mode");
        themeToggle.selectedProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                themeToggle.setText("Light Mode");
                switchToDarkTheme();
            } else {
                themeToggle.setText("Dark Mode");
                switchToLightTheme();
            }
        });

//        iconSearchFieldSpacer.managedProperty().bind(model.isCollapsedProperty().not());

        FontIcon settingsIcon = IconWidgets.styledIcon(Material2MZ.SETTINGS, "menu-icon");

        results.getChildren().addAll(menuIcon, mrs, iconSearchFieldSpacer, searchContainer, space, settingsIcon);
        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }

    private Region createSidebar() {
        NavigationGroup results = new NavigationGroup("sidebar", model.activeViewProperty());

        results.add(Material2OutlinedAL.HOME, "Home", ViewType.HOME, model.isCollapsedProperty());
        results.add(Material2OutlinedAL.COLLECTIONS, "Genres", ViewType.GENRES, model.isCollapsedProperty());

        return results.getView();
    }

    private void switchToLightTheme() {
        main.getStylesheets().remove(darkThemePath);
        main.getStylesheets().add(lightThemePath);
    }

    private void switchToDarkTheme() {
        main.getStylesheets().remove(lightThemePath);
        main.getStylesheets().add(darkThemePath);
    }
}

package dk.easv.presentation.widgets;

import dk.easv.presentation.app.ViewType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.Ikon;

public class NavigationGroup {
    private final VBox container = new VBox();
    private final ObjectProperty<ViewType> activeView;

    public NavigationGroup(String style, ObjectProperty<ViewType> activeView) {
        container.getStyleClass().add(style);
        this.activeView = activeView;
    }

    public void add(Ikon iconCode, String text, ViewType viewType, BooleanProperty showText) {
        NavigationButton button = new NavigationButton(iconCode, text, activeView, viewType, showText);
        button.setOnAction(e -> activeView.set(viewType));
        container.getChildren().add(button);
    }

    public Region getView() {
        return container;
    }
}

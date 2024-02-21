package dk.easv.presentation.widgets;

import dk.easv.presentation.app.ViewType;
import dk.easv.presentation.utils.Animations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class NavigationButton extends Button {
    private static final PseudoClass HOVER = PseudoClass.getPseudoClass("hover");
    private static final PseudoClass ACTIVE = PseudoClass.getPseudoClass("active");
    private static final String CSS_CLASS_NAV_BUTTON = "nav-button";
    private static final String CSS_CLASS_ICON = "font-icon";
    private static final String CSS_CLASS_LABEL = "label";
    private static final String CSS_CLASS_INDICATOR = "indicator";

    private final ObjectProperty<ViewType> activeView;
    private final ViewType viewType;
    private final Region indicator;

    public NavigationButton(Ikon iconCode, String text, ObjectProperty<ViewType> activeView, ViewType viewType, BooleanProperty showText) {
        super();

        this.activeView = activeView;
        this.viewType = viewType;

        this.getStyleClass().add(CSS_CLASS_NAV_BUTTON);
        this.setMaxWidth(Double.MAX_VALUE);

        FontIcon icon = IconWidgets.styledIcon(iconCode, CSS_CLASS_NAV_BUTTON, CSS_CLASS_ICON);
        Label label = LabelWidgets.styledLabel(text, CSS_CLASS_NAV_BUTTON, CSS_CLASS_LABEL);

        label.visibleProperty().bind(showText.not());
        label.managedProperty().bind(showText.not());

        this.indicator = createIndicator(icon);
        Region content = createContent(indicator, icon, label);

        this.setGraphic(content);

        this.activeView.addListener((obs, oldVal, newVal) -> updateActiveState());
        updateActiveState(); // opdater på konstruktion af knap

        setupAnimations();
    }

    private Region createIndicator(FontIcon icon) {
        Region results = new Region();

        results.getStyleClass().add(CSS_CLASS_INDICATOR);
        results.setMinWidth(1);
        results.setVisible(false);

        // Juster højden på indicator baseret på højden af ikonet da det ikke umiddelbart lige kan gøres via CSS.
        icon.layoutBoundsProperty().addListener((obs, ov, nv) -> results.setMinHeight(nv.getHeight() + 8));

        return results;
    }

    private Region createContent(Region indicator, FontIcon icon, Label label) {
        HBox results = new HBox(indicator, icon, label);

        results.setAlignment(Pos.CENTER_LEFT);
        results.setSpacing(10);

        return results;
    }

    private void updateActiveState() {
        boolean isActive = activeView.get() == viewType;
        this.pseudoClassStateChanged(ACTIVE, isActive);
        this.indicator.setVisible(isActive);
        if (!isActive) Animations.fadeOut(this, Duration.millis(200), 0.8, 1.0);
    }

    private void setupAnimations() {
        // Custom hover animation
        this.hoverProperty().addListener((obs, ov, nv) -> {
            this.pseudoClassStateChanged(HOVER, nv);
            if (nv) Animations.fadeIn(this, Duration.millis(200), 0.8, 1.0);
        });
    }
}
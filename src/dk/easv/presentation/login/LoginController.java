package dk.easv.presentation.login;

import dk.easv.presentation.app.AppModel;
import dk.easv.presentation.app.ViewHandler;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class LoginController {
    private final AppModel model;
    private final Builder<Region> viewBuilder;

    public LoginController(AppModel model, ViewHandler viewHandler) {
        this.model = model;
        this.viewBuilder = new LoginViewBuilder(model, viewHandler);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}

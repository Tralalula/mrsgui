package dk.easv.presentation.app;

import dk.easv.presentation.home.HomeViewBuilder;
import dk.easv.presentation.login.LoginController;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class AppController {
    private final AppModel model;
    private final Builder<Region> viewBuilder;
    private final ViewHandler viewHandler;

    public AppController() {
        this.model = new AppModel();
        this.viewHandler = new ViewHandler(model);

        Region login = new LoginController(model, viewHandler).getView();
        Region home = new HomeViewBuilder(model).build();

        viewHandler.setLoginView(login);
        viewHandler.setHomeView(home);

        this.viewBuilder = new AppViewBuilder(
                model,
                login,
                home);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}

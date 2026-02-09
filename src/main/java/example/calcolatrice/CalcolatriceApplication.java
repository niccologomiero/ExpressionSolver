package example.calcolatrice;

import example.calcolatrice.expression.Espressione;
import example.calcolatrice.expression.ExpressionException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class CalcolatriceApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalcolatriceApplication.class.getResource("view/Calcolatrice-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 500);
        stage.setTitle("Calcolatrice");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
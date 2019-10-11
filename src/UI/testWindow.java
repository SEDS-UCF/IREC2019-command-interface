package UI;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import yamFactory.yamManager;

import java.io.IOException;

//Straight up AIDS
public class testWindow
{
    public void getWindow()
    {
        Stage window = new Stage();
        window.setTitle("Testing n Stuff");
        window.setHeight(600);
        window.setWidth(500);
        window.initModality(Modality.APPLICATION_MODAL);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10,10,10,10));

        VBox centerLayout = new VBox(10);
        centerLayout.setAlignment(Pos.CENTER);

        JFXButton makeYam = new JFXButton("Make Yam");
        makeYam.setTooltip(new Tooltip("Check console for output"));

        makeYam.setOnAction(e -> makeAYam());

        centerLayout.getChildren().add(makeYam);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        JFXButton closeButton = new JFXButton("Close");

        buttonBox.getChildren().add(closeButton);

        closeButton.setOnAction(e -> window.close());

        mainLayout.setCenter(centerLayout);
        mainLayout.setBottom(buttonBox);

        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add("UI/CSS/mainStyle.css");
        window.setScene(scene);
        mainLayout.requestFocus();
        window.show();
    }

    void makeAYam()
    {
        Platform.runLater(() -> {
            yamManager yamer = new yamManager();
            try {
                yamer.test();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}

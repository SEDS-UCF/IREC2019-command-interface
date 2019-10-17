package UI;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logs.Logger;
import netwerking.Netwerk;


//Straight up AIDS
public class testWindow
{

    public void getWindow()
    {
        Stage window = new Stage();
        window.setTitle("Testing n Stuff");
        window.setHeight(600);
        window.setWidth(500);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10,10,10,10));

        VBox centerLayout = new VBox(10);
        centerLayout.setAlignment(Pos.CENTER);


        JFXButton sendYam = new JFXButton("Send Yam");
        sendYam.setTooltip(new Tooltip("Send a yam"));

        JFXButton connectButton = new JFXButton("Connect");
        connectButton.setTooltip(new Tooltip("Connect socket"));

        JFXButton checkIn = new JFXButton("Check In");
        connectButton.setTooltip(new Tooltip("Check In"));

        sendYam.setOnAction(e -> sendYam());

        connectButton.setOnAction(e -> connect());

        checkIn.setOnAction(e -> checkInput());


        centerLayout.getChildren().addAll(sendYam,connectButton,checkIn);

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

    void checkInput()
    {
        if(!Netwerk.getInstance().inQueue.isEmpty())
        {
            Logger.getInstance().logMessage(Netwerk.getInstance().inQueue.poll().getMsg().toJson());
        }
        else
        {
            Logger.getInstance().logMessage("There is nothing in the queue");
        }
    }

    void connect()
    {
        Netwerk.getInstance().connect("lax.benrstraw.xyz",42069);
    }

    private void sendYam()
    {
        Netwerk.getInstance().test();
    }
}

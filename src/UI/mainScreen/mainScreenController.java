package UI.mainScreen;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import netwerking.networkUtils;
import UI.screensFramework.ControlledScreen;
import UI.screensFramework.ScreensController;

public class mainScreenController implements ControlledScreen
{

    private ScreensController myController;
    networkUtils theThing = new networkUtils();


    @FXML
    Button netButt, close;


    @Override
    public void setScreenParent(ScreensController screenPage)
    {
        myController = screenPage;
    }

    @Override
    public void update(){}


    public void initialize()
    {
        start();
        netButt.setOnAction(e -> getFactory());
        close.setOnAction(e -> closeCon());
    }



    private void closeCon()
    {
        theThing.closeControlComputerConnection();
    }

    private void start()
    {
        theThing.establishControlComputerConnection();
    }


    private void getFactory()
    {

        String message = "This is a message";

        new Thread(() -> {

            String response = theThing.sendRequest(message);


            if(response.compareTo("err") == 0)
            {
                Platform.runLater(() -> {

                    notifications.errorNotifications.getTimeoutAlert(response, "", Alert.AlertType.ERROR, message);
                });
            }

        }).start();



    }

}

package UI;


import UI.LogDisplay.LogWindow;
import UI.screensFramework.ControlledScreen;
import UI.screensFramework.ScreensController;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import netwerking.Netwerk;


enum wifiEnum
{
    CONNECTED("Connected"),
    DISCONNECTED("Disconnected");

    private final String status;

    wifiEnum(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}

public class mainScreenController implements ControlledScreen
{

    private ScreensController myController;

    LogWindow logWindow;

    ObjectProperty<wifiEnum> wifiStatus = new SimpleObjectProperty<>(wifiEnum.DISCONNECTED);

    @FXML
    Label wifiStatusLabel;



    @Override
    public void setScreenParent(ScreensController screenPage)
    {
        myController = screenPage;
    }

    @Override
    public void update(){}


    public void initialize()
    {
        logWindow = new LogWindow();
        wifiStatusLabel.textProperty().bind(
                new When(wifiStatus.isEqualTo(wifiEnum.CONNECTED)).then(setLabelConnected()).otherwise(setLabelDisconnected())
        );
        startStatusChecker();
        start();
    }

    private String setLabelConnected()
    {
        wifiStatusLabel.setStyle("-fx-text-fill: #00b600;");

        return wifiEnum.CONNECTED.toString();
    }

    private String setLabelDisconnected()
    {
        wifiStatusLabel.setStyle("-fx-text-fill: RED;");

        return wifiEnum.DISCONNECTED.toString();
    }

    private void startStatusChecker()
    {
        Thread checker = new Thread(() -> {
            boolean alive = Netwerk.getInstance().isConnected();
            if (alive) {
                Platform.runLater(() -> wifiStatus.set(wifiEnum.CONNECTED));
            } else {
                Platform.runLater(() -> wifiStatus.set(wifiEnum.DISCONNECTED));
            }

        });

        checker.setDaemon(true);

    }



    public void getLogWindow()
    {
        logWindow.createLogWindow();
    }

    private void start()
    {

    }


    public void testAction()
    {
        testWindow tester = new testWindow();
        tester.getWindow();
    }



}

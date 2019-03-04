package notifications;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import netwerking.networkUtils;

import java.util.Optional;

public class errorNotifications
{
    public static void getTimeoutAlert(String header, String content, Alert.AlertType type, String message)
    {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ButtonType retry = new ButtonType("Retry");
        ButtonType cancel = new ButtonType("Cancel");


        alert.getButtonTypes().setAll(retry,cancel);
        Optional<ButtonType> getres = alert.showAndWait();

        if(getres.isPresent() && getres.get() == retry)
        {
            networkUtils utils = new networkUtils();
            utils.sendRequest(message);
        }
    }
}

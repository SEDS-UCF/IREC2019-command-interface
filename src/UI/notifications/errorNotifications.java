package UI.notifications;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;


/*
This class implements the error message notifications that
will be a part of the notification system. This allows us
to create global notifications despite what state the
application is in.
*/

public class errorNotifications
{


    public static void getErrorAlert(String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();

    }
}

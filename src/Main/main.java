package Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import UI.screensFramework.ScreensController;
import logs.Logger;
import netwerking.Netwerk;


public class main extends Application
{
    public static String mainScreenID = "main";
    public static String mainScreenFile = "/UI/mainScreen.fxml";


    //Everything that needs to be done before
    //the application closes should be in here
    private void closeDownOperations()
    {
        Netwerk.getInstance().closeConnection();
        Logger.getInstance().closeAllLogFiles();
    }


    @Override
    public void start(Stage primaryStage)
    {

        //Load all screens into the screen controller
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(mainScreenID, mainScreenFile);


        mainContainer.setScreen(main.mainScreenID);

        Scene scene = new Scene(mainContainer);
        primaryStage.setTitle("Launch Computer");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();


        //call closeDownOperations() before shutdown
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            closeDownOperations();
            primaryStage.close();
        });


    }


    public static void main(String[] args)
    {

        launch(args);
    }
}

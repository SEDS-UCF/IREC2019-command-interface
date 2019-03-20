package UI.LogDisplay;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logs.Logger;

public class LogWindow
{
    private static boolean isOpen = false;
    private final String masterTabName = "Master Log";
    private final String errorTabName = "Error Log";
    private final String eventTabName = "Event Log";

    private TextArea getTabContent(Tab tab)
    {
        tab.setClosable(false);

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));

        TextArea textArea = new TextArea();

        //This is to make sure the text area always
        //tries to fill all available height. The right
        //way to do this would be to bind the height
        //property of the text area with the height
        //of the VBox container but this is easier.
        textArea.setPrefHeight(6000);

        layout.getChildren().add(textArea);

        tab.setContent(layout);

        return textArea;

    }

    public void createLogWindow()
    {
        if(isOpen)
        {
            return;
        }

        isOpen = true;

        Stage window = new Stage();
        window.setHeight(600);
        window.setWidth(500);
        window.setTitle("Logs");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(0,0,10,0));
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button clearButton = new Button("Clear");
        Button closeButton = new Button("Close");


        buttonBox.getChildren().addAll(clearButton,closeButton);

        TabPane tabPane = new TabPane();

        Tab masterTab = new Tab();
        masterTab.setText(masterTabName);

        Tab errorTab = new Tab();
        errorTab.setText(errorTabName);

        Tab eventTab = new Tab();
        eventTab.setText(eventTabName);

        getTabContent(masterTab).textProperty().bindBidirectional(Logger.getInstance().getObservableMasterLog());
        getTabContent(errorTab).textProperty().bindBidirectional(Logger.getInstance().getObservableErrorLog());
        getTabContent(eventTab).textProperty().bindBidirectional(Logger.getInstance().getObservableEventLog());


        tabPane.getTabs().addAll(masterTab,errorTab,eventTab);

        mainLayout.getChildren().addAll(tabPane,buttonBox);


        clearButton.setOnAction(e -> {

            String selected = tabPane.getSelectionModel().getSelectedItem().getText();

            if(selected.compareTo(masterTabName) == 0)
            {
                Logger.getInstance().clearMasterLog();
            }
            else if(selected.compareTo(errorTabName) == 0)
            {
                Logger.getInstance().clearErrorLog();
            }
            else if(selected.compareTo(eventTabName) == 0)
            {
                Logger.getInstance().clearEventLog();
            }
        });

        closeButton.setOnAction(e -> {
            isOpen = false;
            window.close();
        });

        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add("CSS/mainStyle.css");
        window.setScene(scene);
        window.show();

        window.setOnCloseRequest(event -> {
            event.consume();
            System.out.println("We got here");
            isOpen = false;
            window.close();
        });
    }
}

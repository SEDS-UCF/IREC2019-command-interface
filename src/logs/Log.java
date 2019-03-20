package logs;

import javafx.beans.property.SimpleStringProperty;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


//This class will wrap up all the necessary methods for creating,
//adding messages to, and writing files for each log. Every log will
//maintain it's own ArrayList object to hold individual messages. This
//is mainly to ease formatting and provide a method of accessing
//individual messages should we need to parse the log in the future

//This is only a support class for the Logger class that will wrap up
//the primary logging functions for the application
class Log
{
    private ArrayList<String> log;
    private String filePath;
    private FileWriter writer;

    SimpleStringProperty observableLog;

    Log(String path)
    {
        this.log = new ArrayList<>();
        this.filePath = path;
        this.observableLog = new SimpleStringProperty("");
        startLogFile();

    }

    private void startLogFile()
    {
        try {
            this.writer = new FileWriter(filePath);

            this.writer.write("Log Opened At: " + LocalDateTime.now().toString() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getFilePath()
    {
        return filePath;
    }

    void writeMessageToFile(String message)
    {
        try {

            writer.write(message + "\n");

            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e)
        {
            System.out.println("Something went wrong trying to create the " + filePath + " log");
        }
    }

    void closeLogFile()
    {
        try {
            writer.write("Log Closed At: " + LocalDateTime.now().toString() + "\n");
            writer.write("EOF");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Prefix every log message with the current time.
    //we can omit the data since that is written once
    //when we actually create the text file.
    private String formatMessage(String message)
    {
        if(message == null)
        {
            return "";
        }

        return LocalTime.now().toString() + ": " + message;
    }


    void logMessage(String message)
    {
        String formattedMessage = formatMessage(message);
        log.add(formattedMessage);
        observableLog.setValue(observableLog.get() + "\n" + formatMessage(message));
        writeMessageToFile(formattedMessage);
    }

    void clearObservableLog()
    {
        observableLog.setValue("");
    }

    String getLogMessage(int index)
    {
        return log.get(index);
    }


    ArrayList<String> getAllLogMessages()
    {
        return log;
    }




}

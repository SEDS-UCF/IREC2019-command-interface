package logs;

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

    Log(String path)
    {
        log = new ArrayList<>();
        filePath = path;
    }


    public String getFilePath()
    {
        return filePath;
    }

    //each log should be able to write itself to a text file using some standard formatting
    //In this case, we time stamp the file and just write each log message line by line to
    //whatever directory/filename is specified on the creation of the specific log instance
    void createLogFile()
    {
        try {
            FileWriter writer = new FileWriter(filePath);

            writer.write("Log Started At: " + LocalDateTime.now().toString() + "\n");

            for (String aLog : log)
            {
                writer.write(aLog);
                writer.write("\n");
            }

            writer.write("EOF");

            writer.close();

        } catch (IOException e)
        {
            System.out.println("Something went wrong trying to create the " + filePath + " log");
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
        log.add(formatMessage(message));
    }

    String getLogMessage(int index)
    {
        return log.get(index);
    }


    //Return a copy of Log to preserve Log integrity.
    //This will be useful if we want to display the log
    //somewhere else in the program without having to worry
    //about whether it is altered in any way.
    ArrayList<String> getAllLogMessages()
    {
        return new ArrayList<>(log);
    }




}

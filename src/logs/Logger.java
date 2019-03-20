package logs;

//This is a singleton class to handle creating and managing logs
//that will be written to standard text files throughout the program.
//We also provide get methods so we can view the logs live if we need to.
//We maintain three different logs throughout the program.
//The event log will only hold discrete non-error actions we perform.
//The error log will only hold errors which can be exceptions or
//defined errors/assertion violations.
//The master log will hold all of the messages in each log


import javafx.beans.property.SimpleStringProperty;


public class Logger
{
    private final String errorLogPath = "errorLog.txt";
    private final String eventLogPath = "eventLog.txt";
    private final String masterLogPath = "masterLog.txt";


    private static Logger logger = null;

    private Log errorLog;
    private Log eventLog;
    private Log masterLog;


    private Logger()
    {
        errorLog = new Log(errorLogPath);
        eventLog = new Log(eventLogPath);
        masterLog = new Log(masterLogPath);
    }

    public static Logger getInstance()
    {
        if(logger == null)
        {
            logger = new Logger();
        }

        return logger;
    }

    public void closeErrorLogFile()
    {
        errorLog.closeLogFile();
    }

    public void closeEventLogFile()
    {
        eventLog.closeLogFile();
    }

    public void closeMasterLogFile()
    {
        masterLog.closeLogFile();
    }

    public void closeAllLogFiles()
    {
        closeErrorLogFile();
        closeEventLogFile();
        closeMasterLogFile();
    }



    public void logMessage(String message)
    {
        eventLog.logMessage(message);
        masterLog.logMessage(message);
    }

    public void logErrorMessage(String message)
    {
        errorLog.logMessage("[Error] " + message);
        masterLog.logMessage("[Error] " + message);
    }


    public SimpleStringProperty getObservableMasterLog()
    {
        return masterLog.observableLog;
    }

    public SimpleStringProperty getObservableErrorLog()
    {
        return errorLog.observableLog;
    }

    public SimpleStringProperty getObservableEventLog()
    {
        return eventLog.observableLog;
    }


    public void clearMasterLog()
    {
        masterLog.clearObservableLog();
    }

    public void clearErrorLog()
    {
        errorLog.clearObservableLog();
    }

    public void clearEventLog()
    {
        eventLog.clearObservableLog();
    }






}

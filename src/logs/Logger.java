package logs;

//This is a singleton class to handle creating and managing logs
//that will be written to standard text files throughout the program.
//We also provide get methods so we can view the logs live if we need to.
//We maintain three different logs throughout the program.
//The event log will only hold discrete non-error actions we perform.
//The error log will only hold errors which can be exceptions or
//defined errors/assertion violations.
//The master log will hold all of the messages in each log


public class Logger
{
    private final String errorLogPath = "errorLot.txt";
    private final String eventLogPath = "eventLot.txt";
    private final String masterLogPath = "masterLot.txt";


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
            return new Logger();
        }

        return logger;
    }

    public void createErrorLogFile()
    {
        errorLog.createLogFile();
    }

    public void createEventLogFile()
    {
        eventLog.createLogFile();
    }

    public void createMasterLogFile()
    {
        masterLog.createLogFile();
    }

    public void createAllLogFiles()
    {
        createErrorLogFile();
        createEventLogFile();
        createMasterLogFile();
    }



    public void logMessage(String message)
    {
        eventLog.logMessage(message);
        masterLog.logMessage(message);
    }

    public void logErrorMessage(String message)
    {
        errorLog.logMessage(message);
        masterLog.logMessage(message);
    }





}

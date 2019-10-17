package UI;


import UI.LogDisplay.LogWindow;
import UI.screensFramework.ControlledScreen;
import UI.screensFramework.ScreensController;

public class mainScreenController implements ControlledScreen
{

    private ScreensController myController;

    LogWindow logWindow;



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
        start();
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

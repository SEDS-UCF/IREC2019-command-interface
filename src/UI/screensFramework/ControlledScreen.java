package UI.screensFramework;


public interface ControlledScreen
{
    
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(ScreensController screenPage);


    //We want to provide an optional update method to be called
    //every time we set a new screen in the screens controller.
    //This allows every controller to update itself right before
    //the user sets the screen.
    public void update();

}

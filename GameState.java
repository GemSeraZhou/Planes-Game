import java.util.ArrayList;

/*
represents global game state

essentially uses the state pattern
*/

public interface GameState
{
    public void onKeyHold(ArrayList<String> input);
    public void onKeyPress(String keyCode);
    public void onKeyRelease(String keyCode);
    public void onMousePress(double mouseX, double mouseY);
    public void onMouseRelease(double mouseX, double mouseY);
    public void onMouseClick(double mouseX, double mouseY);
    
    //transitions the game to a new state by giving the next state to GameEngine
    public GameState newState();
    
    public void updateAndDisplay();
}

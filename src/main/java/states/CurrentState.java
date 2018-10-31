package states;

public class CurrentState
{
    private static State currentState = State.STARTING;

    public static State getCurrentState()
    {
        return currentState;
    }

    public static void setCurrentState(State currentState)
    {
        CurrentState.currentState = currentState;
    }
}

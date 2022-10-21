package Utils;

public class Constants {
    // UI params
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    public static final int OFF = 5;

    // slider params
    public static final int MAX_SLIDER = 10000;
    public static final int MAX_RATE = MAX_SLIDER / 10;
    public static final int SLIDER_STEP_MAX = MAX_RATE / 10;
    public static final int SLIDER_STEP_MIN = SLIDER_STEP_MAX / 10;

    // integrator params
    public static final double dt = 1.0;
    public static final int STEPS = 10;
}

package MainStuff;

import processing.core.PApplet;

public interface ISystem {
    void setValue(double value);
    double getRealValue();
    double getError();
    void runSystem();
    void drawSystem(PApplet graphics);
}

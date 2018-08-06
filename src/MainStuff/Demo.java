package MainStuff;

import processing.core.PApplet;

public class Demo extends PApplet {
    public static void main(String[] args) {
        PApplet.main("MainStuff.Demo");
    }

    PIDRunner runner;

    @Override
    public void settings() {
        size(640, 480);
    }

    @Override
    public void setup() {
        runner = new PIDRunner(PIDRunner.SystemType.WHEEL_RPM);
    }

    @Override
    public void draw() {
        background(255);
        runner.setTargetValue(1);
        runner.run();
        runner.draw(this);
    }
}

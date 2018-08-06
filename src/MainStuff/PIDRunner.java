package MainStuff;

import processing.core.PApplet;

public class PIDRunner {
    enum SystemType {
        WHEEL_RPM
    }

    private ISystem system;
    private PIDF pid;
    private double targetValue;

    public PIDRunner(SystemType systemType) {
        switch (systemType) {

            case WHEEL_RPM:
                system = new WheelSystem(0.1, 1);
                break;

            default:
                System.out.println("bad error: SystemType enum case not handled");
                break;
        }

        pid = new PIDF();
        pid.P = 1.000;
    }

    public void run() {
        double pidOutput = targetValue;
//        pidOutput -= system.getError() * pid.P;
        system.setValue(pidOutput);
        system.runSystem();
        System.out.println(system.getRealValue());
    }

    public void draw(PApplet graphics) {
        system.drawSystem(graphics);
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }
}

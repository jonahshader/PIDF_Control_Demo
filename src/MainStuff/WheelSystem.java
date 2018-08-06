package MainStuff;

import processing.core.PApplet;
import processing.core.PConstants;

public class WheelSystem implements ISystem {
    /*
    math notes:
    moment of intertia for a disk: I = (1/2)*M*R^2

    T = Ia

     */

    //CIM motor specifications
    public static final double CIM_NO_LOAD_RPM = 5310.0;
    public static final double CIM_NO_LOAD_AMP = 2.7;
    public static final double CIM_NO_LOAD_TORQUE = 0.04912781954887218045;
    public static final double CIM_STALL_TORQUE = 2.42; //N-m
    public static final double CIM_STALL_AMP = 133.0;
    public static final double CIM_FRICTION_TORQUE = CIM_NO_LOAD_TORQUE;

    private double wheelMass; //in newtons (converted from kilograms in constructor)
    private double wheelRadius;

    private double wheelAngle;

    private double targetPower;
    private double wheelVelocity;

    private double momentOfIntertia;

    public WheelSystem(double wheelMass, double wheelRadius) {
        this.wheelMass = wheelMass * 9.81; //from kilograms to newtons
        this.wheelRadius = wheelRadius;

        wheelAngle = 0;
        targetPower = 0;
        wheelVelocity = 0;

        momentOfIntertia = 0.5 * wheelMass * wheelRadius * wheelRadius;
    }

    /**
     *
     * @param value - from -1 to 1
     */
    @Override
    public void setValue(double value) {
        if (value > 1) value = 1;
        else if (value < -1) value = -1;
        targetPower = value;
    }

    /**
     *
     * @return RPM
     */
    @Override
    public double getRealValue() {
        return wheelVelocity / (CIM_NO_LOAD_RPM * Math.PI * 2.0 / 60.0);
    }

    @Override
    public double getError() {
//        return wheelVelocity - targetPower;
        return 0;
    }

    @Override
    public void runSystem() {
//        double maxDifference = motorTorque / (wheelMass * wheelRadius);
//        wheelVelocity += maxDifference * (targetPower - wheelVelocity);

        //find torque
        double speedNormalized = Math.abs(wheelVelocity / CIM_NO_LOAD_RPM);
        if ((targetPower > 0 && speedNormalized < 0) || (targetPower < 0 && speedNormalized > 0)) {
            //sign is different, make speedNormalized negative
            speedNormalized = -speedNormalized;
        }
        //spNorm 1 torque = free spin torque. spNrom 0 torque = stall torque.
        //spNorm -1 torque = -free spin torque + 2x stall torque
        double maxTorqueAtSpeed = (speedNormalized * CIM_NO_LOAD_TORQUE) + ((1.0 - speedNormalized) * CIM_STALL_TORQUE);
        double torque = targetPower * maxTorqueAtSpeed;

        // T/I = a
        double angularAccel = torque / momentOfIntertia; //rads per second squared
//        angularAccel /= (Math.PI * 2.0); // rots per second squared
        wheelVelocity += (angularAccel / 60.0); //div by 60 for fps correction

        if (wheelVelocity > 0) {
            wheelVelocity -= (CIM_FRICTION_TORQUE / momentOfIntertia) / 60.0;
        } else {
            wheelVelocity += (CIM_FRICTION_TORQUE / momentOfIntertia) / 60.0;
        }
    }

    @Override
    public void drawSystem(PApplet graphics) {
        wheelAngle += wheelVelocity;

        graphics.ellipseMode(PConstants.CENTER);
        graphics.stroke(0);
        graphics.fill(180);
        graphics.ellipse(graphics.width / 2, graphics.height / 2, (float) wheelRadius * 32, (float) wheelRadius * 32);
        graphics.line(graphics.width / 2, graphics.height / 2, (float) (Math.cos(wheelAngle) * wheelRadius * 16 + graphics.width / 2), (float) (Math.sin(wheelAngle) * wheelRadius * 16 + graphics.height / 2));
    }
}

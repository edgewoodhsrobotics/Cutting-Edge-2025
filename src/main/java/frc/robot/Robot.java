// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


package frc.robot;


import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class Robot extends TimedRobot {
  SparkMax leftLeader;
  SparkMax leftFollower;
  SparkMax rightLeader;
  SparkMax rightFollower;
  SparkMax elevatorLeader;
  SparkMax elevatorFollower;
  XboxController joystick;
  Solenoid latchSolenoid;
  Timer autoTimer;

  public Robot() {
    // Initialize the SPARKs
    leftLeader = new SparkMax(4, MotorType.kBrushed);
    leftFollower = new SparkMax(2, MotorType.kBrushed);
    rightLeader = new SparkMax(3, MotorType.kBrushed);
    rightFollower = new SparkMax(1, MotorType.kBrushed);
    elevatorLeader = new SparkMax(5, MotorType.kBrushless);
    elevatorFollower = new SparkMax(6, MotorType.kBrushless);
    latchSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);
    autoTimer = new Timer();


    /*
     * Create new SPARK MAX configuration objects. These will store the
     * configuration parameters for the SPARK MAXes that we will set below.
     */
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    SparkMaxConfig rightLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig leftFollowerConfig = new SparkMaxConfig();
    SparkMaxConfig rightFollowerConfig = new SparkMaxConfig();
    SparkMaxConfig elevatorLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig elevatorFollowerConfig = new SparkMaxConfig();
    
    /*
     * Set parameters that will apply to all SPARKs. We will also use this as
     * the left leader config.
     */
    globalConfig
        .smartCurrentLimit(50)
        .idleMode(IdleMode.kBrake);

    // Apply the global config and invert since it is on the opposite side
    rightLeaderConfig
        .apply(globalConfig)
        .inverted(true);

    // Apply the global config and set the leader SPARK for follower mode
    leftFollowerConfig
        .apply(globalConfig)
        .follow(leftLeader);

    // Apply the global config
    rightFollowerConfig
        .apply(globalConfig)
        .follow(rightLeader);

    // Apply the global config and set the leader SPARK for follower mode

    elevatorLeaderConfig
    .apply(globalConfig);

    elevatorFollowerConfig 
    .apply(globalConfig)
    .follow(elevatorLeader);

    /*
     * Apply the configuration to the SPARKs.
     *
     * kResetSafeParameters is used to get the SPARK MAX to a known state. This
     * is useful in case the SPARK MAX is replaced.
     *
     * kPersistParameters is used to ensure the configuration is not lost when
     * the SPARK MAX loses power. This is useful for power cycles that may occur
     * mid-operation.
     */
    leftLeader.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftFollower.configure(leftFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightLeader.configure(rightLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightFollower.configure(rightFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    elevatorLeader.configure(elevatorLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    elevatorFollower.configure(elevatorFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Initialize joystick
    joystick = new XboxController(0);
  }

  @Override
  public void robotPeriodic() {
    // Display the applied output of the left and right side onto the dashboard
    SmartDashboard.putNumber("Left Out", leftLeader.getAppliedOutput());
    SmartDashboard.putNumber("Right Out", rightLeader.getAppliedOutput());
  }

  @Override
  public void autonomousInit() {
    autoTimer.reset();
    autoTimer.start();
  }


  @Override
  public void autonomousPeriodic() {
    double time = autoTimer.get();
  
if (time < 5.5) {
      leftLeader.set(0.3);
      rightLeader.set(0.3);
    } else if (time < 6.2) {
    leftLeader.set(0.0);
    rightLeader.set(-0.5);
    }
    else if (time < 8.5) {
      leftLeader.set(0.3);
      rightLeader.set(0.3);
      } else {
      leftLeader.set(0);
      rightLeader.set(0);
    }
  
  }


  @Override
  public void teleopInit() {
  }


  @Override
  public void teleopPeriodic() {
   
  //Tank Drive
  /*
  double leftPower = joystick.getLeftY();
  double rightPower = joystick.getRightY();

  leftLeader.set(((Math.pow(leftPower, 3))*0.9)*0.5);
  rightLeader.set(((Math.pow(rightPower, 3))*0.5)*0.5);
   */
  

    //Arcade Drive
    
    double forward = (Math.pow(joystick.getLeftY(), 3))*0.30;
    double rotation = (Math.pow(joystick.getRightX(), 3))*0.30;

    leftLeader.set(forward - rotation);
   
    rightLeader.set((forward + rotation));
    

    //Elevator

    double rightTrigger = joystick.getRightTriggerAxis();
    double leftTrigger = joystick.getLeftTriggerAxis();
    double elevatorPower = ((Math.pow(((rightTrigger - leftTrigger)), 3)) * 0.5);
    elevatorLeader.set(elevatorPower);

    //Switch
    if (joystick.getAButton()) {
      latchSolenoid.set(true);  // Unlock
  } else {
      latchSolenoid.set(false); // Lock
  }
  
  }


  @Override
  public void disabledInit() {
  }


  @Override
  public void disabledPeriodic() {
  }


  @Override
  public void testInit() {
  }


  @Override
  public void testPeriodic() {
  }


  @Override
  public void simulationInit() {
  }


  @Override
  public void simulationPeriodic() {
  }
}

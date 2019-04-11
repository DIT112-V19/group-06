#include <Smartcar.h>
int LEFT_SENSORPIN = 5;
int CENTER_SENSORPIN = 6;
int RIGHT_SENSORPIN=  7;

 //Initialize different Pin value
int leftMotorForwardPin = 8;
int leftMotorBackwardPin = 10;
int leftMotorSpeedPin = 9;
int rightMotorForwardPin = 12;
int rightMotorBackwardPin = 13;
int rightMotorSpeedPin = 11;

BrushedMotor leftMotor(leftMotorForwardPin,leftMotorBackwardPin, leftMotorSpeedPin);

BrushedMotor rightMotor(rightMotorForwardPin, rightMotorBackwardPin, rightMotorSpeedPin);

DifferentialControl control(leftMotor, rightMotor);
SimpleCar car(control);



void setup()
{
  Serial.begin(9600);
  pinMode(LEFT_SENSORPIN,INPUT);
  pinMode(CENTER_SENSORPIN,INPUT);
  pinMode(RIGHT_SENSORPIN,INPUT);
car.setSpeed(30);
 
}
 
void loop()
{// read input from sensors
  byte leftSensor=digitalRead(LEFT_SENSORPIN);
  byte centerSensor=digitalRead(CENTER_SENSORPIN);
  byte rightSensor=digitalRead(RIGHT_SENSORPIN);
car.setSpeed(30);
car.setAngle(0);
    while(leftSensor == 1){
      car.setAngle(-90);
     break;
    }
   
    while(rightSensor == 1){
      car.setAngle(90);
     break;
    }
    delay(30);
  

    

}

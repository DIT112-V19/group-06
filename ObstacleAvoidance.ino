#include <Smartcar.h>

//Initialize different Pin value
int leftMotorForwardPin = 8;
int leftMotorBackwardPin = 10;
int leftMotorSpeedPin = 9;
int rightMotorForwardPin = 12;
int rightMotorBackwardPin = 13;
int rightMotorSpeedPin = 11;
const int TRIGGER_PIN = 6; //D6
const int ECHO_PIN = 7; //D7

//Initialize magic value
const unsigned int MAX_DISTANCE = 100;
const int STOP_DISTANCE = 20;

BrushedMotor leftMotor(leftMotorForwardPin,leftMotorBackwardPin, leftMotorSpeedPin);

BrushedMotor rightMotor(rightMotorForwardPin, rightMotorBackwardPin, rightMotorSpeedPin);

DifferentialControl control(leftMotor, rightMotor);

SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

SimpleCar car(control);

void setup() {
 
 Serial.begin(9600);
 
 car.setSpeed(100);
 
}

void loop() {

 if(front.getDistance() < STOP_DISTANCE && front.getDistance() > 0){
  
  car.setSpeed(0);
  
  }
  
}

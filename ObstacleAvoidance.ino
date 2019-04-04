#include <Smartcar.h>

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

const int TRIGGER_PIN = 6; //D6
const int ECHO_PIN = 7; //D7
const unsigned int MAX_DISTANCE = 100000;
SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);


void setup() {
Serial.begin(9600);
 car.setSpeed(100);
 



}

void loop() {
  
Serial.println(front.getDistance());

if(front.getDistance() < 20){
car.setSpeed(0);

}else{
  car.setSpeed(100);
  }

}

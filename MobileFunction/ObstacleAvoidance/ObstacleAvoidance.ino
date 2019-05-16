#include <Smartcar.h>

//Initialize different Pin value
int leftMotorForwardPin = 8;
int leftMotorBackwardPin = 10;
int leftMotorSpeedPin = 9;
int rightMotorForwardPin = 12;
int rightMotorBackwardPin = 13;
int rightMotorSpeedPin = 11;
const int TRIGGER_PIN = 52; //D6
const int ECHO_PIN = 53; //D7

//Initialize magic value
const unsigned int MAX_DISTANCE = 100;
const int STOP_DISTANCE = 40;

BrushedMotor leftMotor(leftMotorForwardPin,leftMotorBackwardPin, leftMotorSpeedPin);

BrushedMotor rightMotor(rightMotorForwardPin, rightMotorBackwardPin, rightMotorSpeedPin);

DifferentialControl control(leftMotor, rightMotor);

SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

SimpleCar car(control);

void setup() {
 
 Serial.begin(9600);
 
// car.setSpeed(40);
 
}

void loop() {

 int curDistance = front.getDistance();

 if(curDistance < STOP_DISTANCE && curDistance > 0){
  if(curDistance < 20 ){
        Backward();
        delay(1000);
        Stop();
        }
   while(curDistance < 50 && curDistance > 0){
        Right();
        delay(1000);
        Stop();
        curDistance = front.getDistance();
        if(curDistance < 50 && curDistance > 0){
        Left();
        delay(1000);
        Stop();
        curDistance = front.getDistance();
        }
        } 
  }else{
    Forward();
  }
  
}

void Forward(){
  car.setSpeed(30);
  car.setAngle(0);
}

void Backward(){
  car.setSpeed(-30);
  car.setAngle(0);
}

void Left(){
  car.setSpeed(30);
  car.setAngle(-110);
}

void Right(){
  car.setSpeed(30);
  car.setAngle(110);
}

void Stop(){
  car.setSpeed(0);
  car.setAngle(0);
}

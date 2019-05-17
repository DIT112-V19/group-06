#include <Smartcar.h>

int leftMotorForwardPin = 8;
int leftMotorBackwardPin = 10;
int leftMotorSpeedPin = 9;
int rightMotorForwardPin = 12;
int rightMotorBackwardPin = 13;
int rightMotorSpeedPin = 11;
const int TRIGGER_PIN = 52; //D6
const int ECHO_PIN = 53; //D7
int duration;
float distance;

BrushedMotor leftMotor(leftMotorForwardPin,leftMotorBackwardPin, leftMotorSpeedPin);

BrushedMotor rightMotor(rightMotorForwardPin, rightMotorBackwardPin, rightMotorSpeedPin);

DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);


void stopm(void);
void forward(void);
void inputs(void);
void sharpright(void);


void setup(void)
{
  
  Serial.begin (9600);
  pinMode(TRIGGER_PIN, OUTPUT);
  pinMode(ECHO_PIN , INPUT);
  pinMode(leftMotorForwardPin ,OUTPUT);
  pinMode(leftMotorBackwardPin ,OUTPUT);
  pinMode(leftMotorSpeedPin,OUTPUT);
  pinMode(rightMotorForwardPin,OUTPUT);
  pinMode(rightMotorBackwardPin,OUTPUT);
  pinMode(rightMotorSpeedPin,OUTPUT);
  digitalWrite(leftMotorSpeedPin,HIGH);
  digitalWrite(rightMotorSpeedPin,HIGH);
  car.setSpeed(30);
 }


void loop(void) {
 inputs();
if(distance>40){
 stopm();
 }
 else if(distance<10){
sharpright();
 }
else
{
forward();
}
}



void forward(void)
{
   car.setSpeed(100);
  digitalWrite(leftMotorForwardPin, HIGH);
  digitalWrite(leftMotorBackwardPin, LOW);
  digitalWrite(rightMotorForwardPin, HIGH);
  digitalWrite(rightMotorBackwardPin, LOW);
   car.setSpeed(30);
}


void sharpright(void)
{

  digitalWrite(leftMotorForwardPin, LOW);
  digitalWrite(leftMotorBackwardPin, HIGH);
  digitalWrite(rightMotorForwardPin, LOW);
  digitalWrite(rightMotorBackwardPin, HIGH);
  delay(400);
}




void stopm(void)
{
  digitalWrite(leftMotorForwardPin, LOW);
  digitalWrite(leftMotorBackwardPin, LOW);
  digitalWrite(rightMotorForwardPin, LOW);
  digitalWrite(rightMotorBackwardPin, LOW);
}


void inputs()
{
      digitalWrite(TRIGGER_PIN, LOW);
      delayMicroseconds(2); 
      digitalWrite(TRIGGER_PIN, HIGH);
      delayMicroseconds(10);
      digitalWrite(TRIGGER_PIN, LOW);
      duration = pulseIn(ECHO_PIN , HIGH);
      distance = (duration / 2) / 29.1;
      digitalWrite(TRIGGER_PIN, LOW); 
      
      Serial.print("forward Distance: ");
      Serial.println(distance);
}

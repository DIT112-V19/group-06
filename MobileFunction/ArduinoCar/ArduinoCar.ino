#include <Smartcar.h>

const int fSpeed = 70; //70% of the full speed forward
const int bSpeed = -70; //70% of the full speed backward
const int lDegrees = -75; //degrees to turn left
const int rDegrees = 75; //degrees to turn right

//initialize constants for speed control
const int LOW_SPEED = 20; 
const int MED_SPEED = 40;
const int HIGH_SPEED = 60; 

//Initialize IR sensor pin value
const int LEFT_SENSORPIN = 49;
const int CENTER_SENSORPIN = 51;
const int RIGHT_SENSORPIN=  50;

//Initialize Utral Sonic sensor pin value
const int TRIGGER_PIN = 52;
const int ECHO_PIN = 53;

int leftMotorForwardPin = 8;
int leftMotorBackwardPin = 10;
int leftMotorSpeedPin = 9;
int rightMotorForwardPin = 12;
int rightMotorBackwardPin = 13;
int rightMotorSpeedPin = 11;

char input;
int duration;
float distance;

int SPEED = 30;


//Initialize magic value
const unsigned int MAX_DISTANCE = 100;
const int STOP_DISTANCE = 40;



SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

void setup() {
  //start serial
  Serial.begin(9600);
  //define pin modes
pinMode(LEFT_SENSORPIN,INPUT);
pinMode(CENTER_SENSORPIN,INPUT);
pinMode(RIGHT_SENSORPIN,INPUT);

}

void loop() {
  handleInput();
}

void handleInput() { //handle serial input if there is any
  if (Serial.available() > 0) {
    input = Serial.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'a':
        followLine();
        break;
        
      case 'c': 
        avoidObstacle();
        break;

      case 'd': 
        followMe();
        break;

      case 'l': //rotate counter-clockwise going forward
        car.setSpeed(SPEED);
        car.setAngle(lDegrees);
        break;
      
      case 'r': //turn clock-wise
        car.setSpeed(SPEED);
        car.setAngle(rDegrees);
        break;
      
      case 'f': //go ahead
        car.setSpeed(SPEED);
        car.setAngle(0);
        break;
      
      case 'b': //go back
        car.setSpeed(-SPEED);
        car.setAngle(0);
        break;

      case 'p':
           SPEED = 30;
           break;
      
      case 'o':
           SPEED = 60;
           break;
         
      default: //if you receive something that you don't know, just stop
        car.setSpeed(0);
        car.setAngle(0);
    }
  }
}

void followLine(){
    car.setAngle(0);
    car.setSpeed(30);
    
  do{

  byte l=digitalRead(LEFT_SENSORPIN);
  byte c=digitalRead(CENTER_SENSORPIN);
  byte r=digitalRead(RIGHT_SENSORPIN);
  if(l == 1 && c == 0 && r == 1){
    car.setAngle(0);
    car.setSpeed(28);
  }else if(l == 0 && c == 0 && r == 1){
      car.setAngle(-68);
      car.setSpeed(28);
  }else if(l == 0 && c == 1 && r == 1){
        car.setAngle(-68);
              car.setSpeed(28);
  }else if(l == 1 && c == 0 && r == 0){
                car.setAngle(68);
                car.setSpeed(28);
  }else if(l == 1 && c == 1 && r == 0){
                  car.setAngle(68);
                  car.setSpeed(28);
  }else if(l == 1 && c == 1 && r == 1){
                    
  }else if(l == 0 && c == 0 && r == 0){
                      car.setAngle(0);
                      car.setSpeed(0);
                      break;
  }
   if (Serial.available() > 0) {
    input = Serial.read();}}while(input == 'a');
  
}

void avoidObstacle() {

do{
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
  } if (Serial.available() > 0) {
    input = Serial.read();}}while(input == 'c');
}


void followMe(){

  car.setSpeed(30);  
  do{
   

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
   if (Serial.available() > 0) {
    input = Serial.read();}}while(input == 'd');
  
}

//Obstacle
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

//Follow me
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

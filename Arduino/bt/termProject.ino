#include <deprecated.h>
#include <MFRC522.h>
#include <MFRC522Extended.h>
#include <require_cpp11.h>

#include <SoftwareSerial.h>
#include <SPI.h>
#include <MFRC522.h>

// motor
#define LEFT_A 12  // left motor input 1
#define LEFT_B 13  // left motor input 2
#define RIGHT_A 10  // right motor input 1
#define RIGHT_B 11  // right motor input 2

#define piezo 31  // input port of piezo speaker
#define button_PIN 22  // input port from the button -> 1 when pressed
#define trig1 A0  //front middle_ultrasonic sensor trig
#define echo1 A1  //front middle_ultrasonic sensor echo
#define trig2 A4  //back_ultrasonic sensor trig
#define echo2 A5  //back_ultrasonic sensor echo
#define trig3 A6  //right_ultrasonic sensor trig
#define echo3 A7  //right_ultrasonic sensor echo
#define trig4 A2  //left_ultrasonic sensor trig
#define echo4 A3  //left_ultrasonic sensor echo

#define RST_PIN 8 // RFID RST port
#define SS_PIN 9  // RFID SDA port

#define HC06 Serial2

#define distanceThreshold 50  // if the obstacle is closer than this threshold, then stop. (centimeter)
#define rotationTime 600  // length of rotating action (milisecond)

#define alarmTone 494  // tone of piezo alarm

int buttonPressed = 0;   // state of the button. 1 when the button is being pressed

// RFID object for communication
MFRC522 mfrc522(SS_PIN, RST_PIN);
MFRC522 rfid(SS_PIN, RST_PIN); 

char input;  // external input from bluetooth communication

// initialization
void setup() {
  Serial.begin(9600);  // serial communication for debug
  HC06.begin(9600);  // bluetooth communication begins
  
  SPI.begin();      // Initiate SPI bus
  rfid.PCD_Init();   // Initiate MFRC522

  // port setting
  pinMode(LEFT_A,OUTPUT);
  pinMode(LEFT_B,OUTPUT);
  pinMode(RIGHT_A,OUTPUT);
  pinMode(RIGHT_B,OUTPUT);
  pinMode(trig1,OUTPUT);
  pinMode(echo1,INPUT);
  pinMode(trig2,OUTPUT);
  pinMode(echo2,INPUT);
  pinMode(trig3,OUTPUT);
  pinMode(echo3,INPUT);
  pinMode(trig4,OUTPUT);
  pinMode(echo4,INPUT);
  pinMode(piezo, OUTPUT);
}


// return the distance between obstacle - front
float distanceFront()
{
  // hypersonic distance sensor
  digitalWrite(trig1,LOW);
  digitalWrite(echo1,LOW);
  delayMicroseconds(2);
  digitalWrite(trig1,HIGH);
  delayMicroseconds(10);
  digitalWrite(trig1,LOW);
  unsigned long duration = pulseIn(echo1,HIGH);
  float distance = ((float)(340 * duration) / 10000) / 2;  // distance - cm
  return distance;
}

// return the distance between obstacle - back
float distanceBack()
{
  // hypersonic distance sensor
  digitalWrite(trig2,LOW);
  digitalWrite(echo2,LOW);
  delayMicroseconds(2);
  digitalWrite(trig2,HIGH);
  delayMicroseconds(10);
  digitalWrite(trig2,LOW);
  unsigned long duration = pulseIn(echo2,HIGH);
  float distance = ((float)(340 * duration) / 10000) / 2;  // distance - cm
  return distance;
}

// return the distance between obstacle - right
float distanceRight()
{
  // hypersonic distance sensor
  digitalWrite(trig3,LOW);
  digitalWrite(echo3,LOW);
  delayMicroseconds(2);
  digitalWrite(trig3,HIGH);
  delayMicroseconds(10);
  digitalWrite(trig3,LOW);
  unsigned long duration = pulseIn(echo3,HIGH);
  float distance = ((float)(340 * duration) / 10000) / 2;  // distance - cm
  return distance;
}

// return the distance between obstacle - left
float distanceLeft()
{
  // hypersonic distance sensor
  digitalWrite(trig4,LOW);
  digitalWrite(echo4,LOW);
  delayMicroseconds(2);
  digitalWrite(trig4,HIGH);
  delayMicroseconds(10);
  digitalWrite(trig4,LOW);
  unsigned long duration = pulseIn(echo4,HIGH);
  float distance = ((float)(340 * duration) / 10000) / 2;  // distance - cm
  return distance;
}

// move the car forward for the specific time
void forward()
{
  digitalWrite(LEFT_A,HIGH);
  digitalWrite(LEFT_B,LOW);
  digitalWrite(RIGHT_A,LOW);
  digitalWrite(RIGHT_B,HIGH);
  delay(rotationTime);
  stoop();
}

// move the car backward for the specific time
void backward()
{
  digitalWrite(LEFT_A,LOW);
  digitalWrite(LEFT_B,HIGH);
  digitalWrite(RIGHT_A,HIGH);
  digitalWrite(RIGHT_B,LOW);
  delay(rotationTime);
  stoop();
}

// turn the car left
void left()
{
  digitalWrite(LEFT_A,LOW);
  digitalWrite(LEFT_B,HIGH);
  digitalWrite(RIGHT_A,LOW);
  digitalWrite(RIGHT_B,HIGH);
  delay(rotationTime);
  stoop();
}

// turn the car right
void right()
{
  digitalWrite(LEFT_A,HIGH);
  digitalWrite(LEFT_B,LOW);
  digitalWrite(RIGHT_A,HIGH);
  digitalWrite(RIGHT_B,LOW);
  delay(rotationTime);
  stoop();
}

// stop the car
void stoop()
{
  digitalWrite(LEFT_A,LOW);
  digitalWrite(LEFT_B,LOW);
  digitalWrite(RIGHT_A,LOW);
  digitalWrite(RIGHT_B,LOW);
  tone(piezo, alarmTone);  // piezo alarm rings
  delay(100);
}

// when the car automatically drives itself
void autoDrive()
{
  Serial.println("Autodrive enabled.");

  // if an obstacle is in front of the car, 
  // turn the car to the direction where measured distance to the obstacle is farther
  // else, go forward
  if (distanceFront() < distanceThreshold) {
    if (distanceLeft() > distanceRight()) {
      backward();
      left();
    }
    else {
      backward();
      right();
    }
  }
  else {
    forward();
  }
}


// perform RFID detection
// return 0 when there is no detected RFID card
// return 1 and terminate the program when there is any detected RFID card
int checkRFID() {
  // check if new card is detected
  if ( ! mfrc522.PICC_IsNewCardPresent()) 
  {
    return 0;
  }
  if ( ! mfrc522.PICC_ReadCardSerial()) 
  {
    return 0;
  }
  String content= "";
  for (byte i = 0; i < mfrc522.uid.size; i++) 
  {
     content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
     content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }

  // if the flow reaches here, it means that a RFID card is detected
  Serial.println("RFID Passed, program terminated.");
  noTone(piezo);
  return 1;
  
}


// after the button on the car is pressed, the car is controlled by the alarm app
void controlPhase() {
  // the car is controlled by the alarm app until any RFID card is detected
  Serial.println("Controller enabled");
  Serial.println(checkRFID());
  while(checkRFID() == 0) {
    if (HC06.available()) {
      input = HC06.read();
      // control to move forward
      if (input == 'f') {
        Serial.println("f");
        forward();
      }
      // control to move backward
      else if (input == 'b') {
        Serial.println("b");
        backward();
      }
      // control to turn left
      else if (input == 'l') {
        Serial.println("l");
        left();
      }
      // control to turn right
      else if (input == 'r') {
        Serial.println("r");
        right();
      }
      else {
        // error -> invalid control
      }
  }
}
}


// main body of the program
void loop() {
  // wait until any message is sent to the car from the app
  if (HC06.available()) {
    input = HC06.read();
    Serial.println(input);

    // buzzer alarming, auto-drive starts. Terminates if the button is pressed for 2 seconds
    if (input == 'f') {
      while(buttonPressed == 0){
        Serial.println(digitalRead(button_PIN));
        autoDrive();
        if (digitalRead(button_PIN)) {
          buttonPressed = 1;
          Serial.println("ButtonClicked");
        }
      }
      // terminate auto-drive and the car is now controlled by the app
      controlPhase();
    }
    else {
      // invalid input, not 'a'
    }
  }
}

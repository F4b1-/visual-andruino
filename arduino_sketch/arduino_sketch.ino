#include <SoftwareSerial.h>

#define rxPin 50
#define txPin 51

String inString;
SoftwareSerial mySerial(rxPin, txPin); // RX, TX

void setup() {
  // put your setup code here, to run once:
  // read bluetooth
  Serial.begin(9600);
  mySerial.begin(9600);
  // until command SETUP_COMPLETE

}

void loop() {
  // put your main code here, to run repeatedly:

    //String blRead = readBluetooth();

   /* if(blRead.length() > 0) {
      Serial.println(readBluetooth());
    }*/

  
  if (mySerial.available()){
  
    char btData = mySerial.read();
    
    if (btData != ';') {
      //Serial.println(btData);
      // convert the incoming byte to a char and add it to the string:
      inString += (char)btData;
         
    } else { 
      handleCommand(inString);
      inString = "";
    }
    
  }
      

   

  // execute commands

}

void handleCommand(String commandString) {
  Serial.println(commandString);
  commandString.trim();
  if(commandString.equals("1")) {
    Serial.println("analogWrite");
  } else {
    Serial.println("unknown command");
  }
}

/*
String readBluetooth() {

  String inString;
  if (mySerial.available()){
    while(mySerial.peek() != ";" ) {
    char btData = mySerial.read();
  //  if (isDigit(btData)) {
      // convert the incoming byte to a char and add it to the string:
      inString += (char)btData;
      
   // }
    }
      //Serial.println(inString);
      return inString;
  
  }
  
}*/

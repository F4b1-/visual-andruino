#include <SoftwareSerial.h>
#include <string.h>
#include <stdio.h>

#define rxPin 50
#define txPin 51
#define maxNumOfCommandParams 10

String inString;
SoftwareSerial mySerial(rxPin, txPin); // RX, TX

void setup() {
  // put your setup code here, to run once:
  // read bluetooth
  Serial.begin(9600);
  mySerial.begin(9600);
  // until command SETUP_COMPLETE

  /** 
   *  Just for testing
   */

  
}

void loop() {

  if (mySerial.available()){
  
    char btData = mySerial.read();
    
    if (btData != ';') {
      //Serial.println(btData);
      // convert the incoming byte to a char and add it to the string:
      inString += (char)btData;
         
    } else { 
      Serial.println(inString);
      handleCommand(inString);
      inString = "";
    }
    
  }
      

   

  // execute commands

}

void handleCommand(String commandString) {
  commandString.trim();


  char* tokens[maxNumOfCommandParams];
  splitCommand((char*) commandString.c_str(), tokens);
  //Serial.println(tokens[1]);
  char* commandId = tokens[0];


  /**
  *** COMMANDS ***
  */
    // -- pinMode ---
  if(strcmp(commandId, "1") == 0) {
    char* commandParam1 = tokens[1];
    char* commandParam2 = tokens[2];
    
    Serial.println("analogWrite");
    Serial.println(commandParam1);
    Serial.println(commandParam2);
    
    if(strcmp(commandParam2, "OUTPUT") == 0) {
       pinMode(atoi(commandParam1), OUTPUT);  
    } 
    else if(strcmp(commandParam2, "INPUT") == 0) {
       pinMode(atoi(commandParam1), INPUT);  
    } 
     
    
  }
  // -- analogWrite ---
  else if(strcmp(commandId, "2") == 0) {
    char* commandParam1 = tokens[1];
    char* commandParam2 = tokens[2];
    
    Serial.println("analogWrite");
    Serial.println(commandParam1);
    Serial.println(commandParam2);
    analogWrite(atoi(commandParam1), atoi(commandParam2));  
    
  }
  // -- digitalWrite ---
   else if(strcmp(commandId, "3") == 0) {
   char* commandParam1 = tokens[1];
   char* commandParam2 = tokens[2];
    
    Serial.println("digitalWrite");
    Serial.println(commandParam1);
    Serial.println(commandParam2);
    pinMode(atoi(commandParam1), OUTPUT);
    digitalWrite(atoi(commandParam1), atoi(commandParam2));  
  
   }
   // -- tone ---
   else if(strcmp(commandId, "4") == 0) {
   char* commandParam1 = tokens[1];
   char* commandParam2 = tokens[2];
   //char* commandParam3 = tokens[3];
    
    Serial.println("tone");
    Serial.println(commandParam1);
    Serial.println(commandParam2);
    //Serial.println(commandParam3);
    tone(atoi(commandParam1), atoi(commandParam2), 500);
    
  
   }
   // -- analogRead ---
   else if(strcmp(commandId, "-1") == 0) {
    char* commandParam1 = tokens[1];
    pinMode(atoi(commandParam1), INPUT);
    Serial.println("analogRead");
    Serial.println(commandParam1);
    int returnedReadValue = analogRead(atoi(commandParam1));
    Serial.println(returnedReadValue);
    mySerial.write(String(returnedReadValue).c_str());
    mySerial.write(3);
   }
   // -- digitalRead ---
   else if(strcmp(commandId, "-2") == 0) {
    char* commandParam1 = tokens[1];
    pinMode(atoi(commandParam1), INPUT);
    Serial.println("digitalRead");
    Serial.println(commandParam1);
    int returnedReadValue = digitalRead(atoi(commandParam1));
    Serial.println(returnedReadValue);
    mySerial.write(String(returnedReadValue).c_str());
    mySerial.write(3);
   }
  
  else {
    Serial.println("unknown command");
  }

  //delay(1000);
}

/** 
 *  Returns an array containing the command parts
 *  arr[0]-> commandId
 *  arr[1 ... maxNumOfCommandParams - 1]-> command parameters 
  */
void splitCommand(char* str, char* tokens[]) {
  
   const char s[2] = " ";
   char *token;
   
   /* get the first token */
   token = strtok(str, s);
   int counter = 0;
   tokens[counter] = token;
   //tokens[1] = strtok(str, s);
   
   /* walk through other tokens */
   while( token != NULL ) {
      //printf( " %s\n", token );
      counter++;
      token = strtok(NULL, s);
      tokens[counter] = token;
   }
   
}

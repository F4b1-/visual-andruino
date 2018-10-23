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
  
  // -- analogWrite ---
  if(strcmp(commandId, "2") == 0) {
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
   else if(strcmp(commandId, "-1") == 0) {
    char* commandParam1 = tokens[1];

    Serial.println("analogRead");
    Serial.println(commandParam1);
    //mySerial.write(analogRead(atoi(commandParam1)));  
    mySerial.write("testing return value");
    mySerial.write(3);
   }
  
  else {
    Serial.println("unknown command");
  }
}

/** 
 *  Returns an array containing the command parts
 *  arr[0]-> commandId
 *  arr[1 ... 2]-> command parameters 
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


#include <PubSubClient.h>
#include <WiFiClientSecure.h>
#include <ESP8266WiFi.h>
#include <time.h>
#include "config.h"
#include <Wire.h>
#include <Adafruit_AHTX0.h>
#include <Adafruit_BMP280.h>

const char* ssid = WIFI_SSID;
const char* password = WIFI_PASSWD;
const char* mqtt_Broker = MQTT_Broker;
const char* mqtt_user = MQTT_USER;
const char* mqtt_passwd = MQTT_PASSWD;
char* action_response = (char*)malloc(10);;

WiFiClient espClient;
PubSubClient client(espClient);

const char *topic = "tst2/esp32";

//Var global
int ledPin = 2; 
int rele1 = 14;
int rele2 = 12;
int rele3 = 13;
int rele4 = 15;
int buzzer = 0;
int micsPin = 34;

unsigned long milliStart = millis();       

unsigned long millisReboot = millis();

unsigned long micsSensorRead = millis();     

//  Heartbeat
unsigned long HeartbeatMillis = 0;
const long Heartbeatinterval = 1000;

Adafruit_AHTX0 aht;  //Libmanager Adafruit AHT10  / Libmanager Adafruit AHTX0
Adafruit_BMP280 bmp; //Libmanager Adafruit BMP280

void setup() {

  // Set D0 - D3 as OUTPUT
  pinMode(rele1, OUTPUT);
  pinMode(rele2, OUTPUT);
  pinMode(rele3, OUTPUT);
  pinMode(rele4, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(ledPin, OUTPUT);

  // setpin IO34 as a input
  pinMode(micsPin, INPUT);

  
    digitalWrite(rele1, LOW);
    digitalWrite(rele2, LOW);
    digitalWrite(rele3, LOW);
    digitalWrite(rele4, LOW);
    digitalWrite(buzzer, LOW);
    digitalWrite(ledPin, LOW);
    
  client.setServer(mqtt_Broker, 1883);
  client.setCallback(callback);

  WifiConnect();
  mqttConnect();
  
  if (! aht.begin()) {
    Serial.println("Could not find AHT? Check wiring");
    while (1) delay(10);
  }
  Serial.println("AHT10 or AHT20 found");

  
  /*if (!bmp.begin(0x76)) {
      
      Serial.println(F("Device not ready!!!"));
      Serial.println(bmp.sensorID(),16);
      while (1) delay(10);
  }*/

  
}

void WifiConnect()
{
  Serial.print("Iniciando scaning de rede!!");
  delay(5000);
  Serial.begin(115200);
  delay(10);
    // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("MAC:" + WiFi.macAddress());
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED)
  {
   digitalWrite(ledPin, LOW);
   delay(1000);
   Serial.print(".");
   digitalWrite(ledPin, HIGH);
   delay(1000);
  }
   Serial.println("");
   Serial.println("WiFi connected");

  // Print the IP address
  Serial.print("Use this URL to connect: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/");
}

int readMicsSensor()
{
  int sensorValue = analogRead(micsPin);
  float voltage = sensorValue * (5.0 / 1023.0);
  return sensorValue;
}


void blinkLed(int time, int blinkTime)
{

   while(blinkTime>=0)
   {
      digitalWrite(ledPin, LOW);
      delay(time);
      digitalWrite(ledPin, HIGH);
      delay(time);
      blinkTime--;
   } 
}


void mqttConnect(){

  while (!client.connected()) {

        String mqtt_ClientID = "";
        mqtt_ClientID = "hpterm-client-";

        Serial.print("*");        
        mqtt_ClientID += String(WiFi.macAddress());
        Serial.printf("The client %s connects to the public MQTT broker\n", mqtt_ClientID.c_str());
        if (client.connect(mqtt_ClientID.c_str(), mqtt_user, mqtt_passwd )) {
            Serial.println("Public EMQX MQTT broker connected");
        } else {
            Serial.print("failed with state ");
            Serial.print(client.state());
            delay(2000);
        }
    }
}

void callback(char *topic, byte *payload, unsigned int length) {
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message:");
    String input = "";
    for (int i = 0; i < length; i++) {
        Serial.print((char) payload[i]);
        input += (char)payload[i];
    }
    blinkLed(50, 0);
    Serial.println();
    Serial.println("-----------------------");

    if(input == "101")
    {
      Serial.println("Set relay 1 to HIGH");
      digitalWrite(rele1, HIGH);
      strcpy(action_response, "10106");
    }
    if(input == "100")
    {
      Serial.println("Set relay 1 to LOW");
      digitalWrite(rele1, LOW);
      strcpy(action_response, "10006");
    }if(input == "201")
    {
      Serial.println("Set relay 2 to HIGH");
      digitalWrite(rele2, HIGH);
      strcpy(action_response, "20106");
    }if(input == "200")
    {
      Serial.println("Set relay 2 to LOW");
      digitalWrite(rele2, LOW);
      strcpy(action_response, "20006");
    }if(input == "301")
    {
      Serial.println("Set relay 3 to HIGH");
      digitalWrite(rele3, HIGH);
      strcpy(action_response, "30106");
    }if(input == "300")
    {
      Serial.println("Set relay 3 to LOW");
      digitalWrite(rele3, LOW);
      strcpy(action_response, "30006");
    }if(input == "401")
    {
      Serial.println("Set relay 4 to HIGH");
      digitalWrite(rele4, HIGH);
      strcpy(action_response, "40106");
    }if(input == "400")
    {
      Serial.println("Set relay 4 to LOW");
      digitalWrite(rele4, LOW);
      strcpy(action_response, "40006");
    }if(input == "501")
    {
      Serial.println("Set buzzer HIGH");
      digitalWrite(buzzer, HIGH);
      strcpy(action_response, "50106");
    }if(input == "500")
    {
      Serial.println("Set buzzer to LOW");
      digitalWrite(buzzer, LOW);
      strcpy(action_response, "50006");
    }if(input == "mics1")
    {
      Serial.println();
      Serial.println("-----------------------");
      Serial.println("Sensor value: ");
      Serial.println(readMicsSensor());
      Serial.println("-----------------------");
      Serial.println();

      //Send MICs sensor value in action_reponse
      
    }if(input == "601")
    {
      strcpy(action_response, "60106");
    }

//client.disconnect(); #Desabilitado para melhorar performance da conexão
    
}

void loop() 
{

if(WiFi.status() != WL_CONNECTED)
{blinkLed(200, 3); WifiConnect();}
if(!client.connected())
{blinkLed(100, 5); mqttConnect();}

client.loop();


//Return function after apply requested changes
/*if((millis() - milliStart)>=1000){
  if(action_response != NULL){
    client.publish(topic, action_response); 
    action_response = NULL;
  }  
  milliStart = millis();
  }*/

/*if((millis() - micsSensorRead)>=1000){
  Serial.println();
  Serial.println("-----------------------");
  Serial.println("Sensor value: ");
  Serial.println(readMicsSensor());
  Serial.println("-----------------------");
  Serial.println();
  micsSensorRead = millis();
}*/



if(strcmp(action_response, "60106") == 0){
    char buffer[10];

    sensors_event_t humidity, temp;
    client.subscribe(topic);

    aht.getEvent(&humidity, &temp);// populate temp and humidity objects with fresh data
    //Serial.print("Temperature: "); Serial.print(temp.temperature); Serial.println(" degrees C");
    client.publish(topic, dtostrf(temp.temperature, 7, 2, buffer));
    //Serial.print("Humidity: "); Serial.print(humidity.relative_humidity); Serial.println("% rH");
    client.publish(topic, dtostrf(humidity.relative_humidity, 7, 2, buffer));
}


if(strcmp(action_response, "00000") != 0){
    client.subscribe(topic);
    client.publish(topic, action_response); 
    strcpy(action_response, "00000");
}  

//Safety reboot task
/*if((millis() - millisReboot)>=3600000){
  client.publish(topic, "System safety reboot!!!!!");
  client.disconnect();
  WiFi.disconnect();
  millisReboot = millis();
  }*/
}


//https://blog.eletrogate.com/como-utilizar-o-sensor-bmp280-com-arduino/
//https://learn.adafruit.com/adafruit-aht20?view=all
//https://www.electronicshub.org/esp32-pinout/

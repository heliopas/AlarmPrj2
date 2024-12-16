import paho.mqtt
import time

import files.var.globalVar as configVar
from paho.mqtt import client as mqtt_client

global client

# def connectMQTT(clientID, user):
#     client = mqtt_client.Client(clientID)
#     client.username_pw_set(configVar.MQTT_USER, configVar.MQTT_PASSWD)
#     try:
#         client.connect(configVar.MQTT_Broker, configVar.MQTT_PORT)
#     except paho.mqtt.MQTTException as e:
#         print('Error during Mqtt inicialization: %s' % e)
#
#     return client

def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(configVar.ID)
    #client.username_pw_set(configVar.MQTT_USER, configVar.MQTT_PASSWD)
    client.on_connect = on_connect
    client.connect(configVar.MQTT_Broker, configVar.MQTT_PORT)
    return client


def publish(client):
    msg_count = 1
    while True:
        time.sleep(1)
        msg = f"messages: {msg_count}"
        result = client.publish(configVar.MQTT_TOPIC, msg)
        # result: [0, 1]
        status = result[0]
        if status == 0:
            print(f"Send `{msg}` to topic `{configVar.MQTT_TOPIC}`")
        else:
            print(f"Failed to send message to topic {configVar.MQTT_TOPIC}")
        msg_count += 1
        if msg_count > 5:
            break

def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")

    client.subscribe(configVar.MQTT_TOPIC)
    client.on_message = on_message

if __name__ == '__main__':
    client = connect_mqtt()
    client.loop_start()
    publish(client)
    client.loop_forever()
23/12/24 - Corrigido função para conexão com MQTT, adicionado endereço de novo broker
03/01/25 - implementado função safety reboot a cada 1hora
         - Adicionado retorno ao aplicar uma função no arduino
         - Removido função client.disconnect(); para ser chamada a cada request 'melhora performance'
08/01/25 - Removido função para realizar leitura da variavel de retorno action_response
10/01/25 - Adicionado função para ler valores do sensor MICs5524
13/01/25 - Alterado biblioteca de leitura de sensores para Adafruit AHT e BMP
15/01/25 - Alterado função para strcpy e uso somente do sensor aht. Adafruit para sensor BMP parece não ser compativel
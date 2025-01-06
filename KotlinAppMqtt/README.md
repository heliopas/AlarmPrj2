# SmartRelay
Interface Kotlin para ativar e desativar funções de alarme

Tabela de conteúdos
=================
<!--ts-->
* [Sobre](#Sobre)
* [Funções disponíveis](#Features)

<!--te-->

## Sobre
Interface para acionamento para alarme usando Mqtt

## Funções disponíveis
- [ ] MqTT open port

## Libs

## Features and release notes

- 19/12/2024 - Corrigido bug Appcompact Activity
- 19/12/2024 - Adicionado função connect 'quebra execução ao clicar connect'
- 20/12/2024 - Corrigido erro que impedia uso da lib Mqtt 'android.enableJetifier=true' gradle.prop..
               Inserido função para connect and publish
               Corrigir, ainda app não se conecta a Mqtt
- 23/12/2024 - Função connect funcionando.Iniciando debug função callback
- 24/12/2024 - Adicionado lógica para imprimir texto terminal console app, função callback em debug
- 02/01/2025 - Função receive message já recebe valores, faltava implementação da função subscribe
- 03/01/2025 - Ajustes para debug na interface logcat
- 03/01/2025 - Alterado layout app

* Alterar função connect para somente fazer conexão
* Esperar conexão retornar sucesso para acionar função subscribe
* implementar retry de função para acionar funções da esp

## Exemplos

*https://www.emqx.com/en/blog/android-connects-mqtt-using-kotlin
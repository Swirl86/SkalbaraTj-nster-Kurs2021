#Skalbara tjänster 2021 - 20Yhp

## Projekt LegoNetty

En skalbar tjänst med: 
  - Del 1: Spring Boot sökmotortjänst som söker efter Lego teman. 
  - Del : en proxy som kan skala ut tjänsten med flera noder.


### Del 1:
I denna del skall du skapa en Spring Boot applikation.  Applikationen skall kunna fungera i samband med del 2 (där man bygger en proxy). Målet med applikationen är att kunna söka efter Lego teman. Du kan hitta en csv fil med alla Lego teman på Ping Pong.

Skapa en eller flera endpoints som kan användas för att söka efter teman som du hittar i csv filen. Hur du implementerar detta är upp till dig. Sökfunktionen är också upp till dig hur du implementerar den. Även fast uppgiften inte innehåller en frontend del så kan man tänka sig att det finns en webbsida med en ”search bar” som man söker efter teman i.


### Del 2:
I denna del skall du skapa en proxy applikation med Netty. Applikationen skall fungera i samband med del 1 (där applikationen ifrån del 1 agerar som en eller flera noder). För att söka på teman är tanken att man skickar sökmeddelandet till proxyn, som i sin tur dirigerar vidare meddelandet till en av noderna (ifrån del 1). Proxyn skall hantera last balansering med round-robin metoden.


### Extra: 
Implementera säkerhet mellan noderna och proxyn. Endast proxyn skall kunna prata med noderna. Om man skickar meddelanden direkt till noderna (genom postman exempelvis) så skall det inte gå. Du använder valfri metod för att uppnå detta. Tänk på att noderna jobbar med http.

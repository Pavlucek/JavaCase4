# JavaCase4

CASE 4 - Sockety
CASE Study
aplikacje konsolowe w architekturze klient - serwer realizowane z wykorzystaniem socketów

Level 1
Wykonaj aplikację klient - serwer realizującą funkcję ECHO.

Działanie serwera:

Oczekuj na przyłączenie klienta
Oczekuj na odebranie tekstu od klienta
Odeślij tekst klientowi
Jeżeli tekst == bye to rozłącz klienta i idź do kroku 1
Idź do kroku 2
Działanie klienta:

Przyłącz się do serwera
Oczekuj na wpisanie tekstu z klawiatury
Wyślij wpisany tekst serwerowi
Oczekuj na odebranie komunikatu od serwera
Jeśli komunikat == bye - KONIEC
Idź do kroku 2
Level 2
Program będzie działał jak powyżej, jednak do serwera będzie możliwe niezależne podłączenie kilku klientów. Serwer działa następująco:

Słuchaj na porcie 7
Po przyłączeniu klienta uruchom wątek i przenieś rozmowę do wątku
Czekaj na wiadomość od klienta
Odeślij klientowi wiadomość
Jeśli wiadomość bye to zakończ wątek, jeśli nie to a)
Wróć do kroku 1
Level 3
A jeżeli byśmy chcieli, aby wysłany przez klienta komunikat dotarł do pozostałych klientów?
Sprawa nie jest taka prosta, ponieważ każdy z klientów może wysłać komunikat w dowolnym momencie. Pozostali muszą nasłuchiwać komunikatów.
Rozwiązaniem jest zastosowanie dwóch socketów:

od klienta do serwera – dla wysyłania
od serwera do klienta – dla odbierania
Ponieważ oba kanały komunikacji mają działać niezależnie, każde z połączeń powinno działać na osobnym wątku

Level 4
W maksymalnej wersji program będzie działał tak

Każdy klient ma swoje imię i w momencie połączenia rejestruje je w serwerze

Klient może wysyłać komunikat do wszystkich lub tylko jednego klienta pisząc:

ALL Witaj świecie

Wacek Idziesz dzisiaj na basen?

Adresat/adresaci powinni otrzymać na konsoli komunikat który rozpoczniemy od nazwy nadawcy

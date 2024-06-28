# 
>>>>                                

# <span style="font-size: 44px; font-weight: bold; color: blue;">** Applicativo Si Vota** </span>


![Logo dell'applicazione](logo.png)


## Introduzione
Si Vota è un'applicazione che consente di creare e gestire votazioni personalizzate. Gli utenti possono definire domande e risposte, inviare le votazioni tramite email ai partecipanti e calcolare i risultati finali in base ai voti ricevuti.

## Funzionalità
- Creazione di Votazioni: Gli utenti possono creare votazioni personalizzate con domande e risposte specifiche.
- Invio via Email: Le votazioni possono essere inviate ai partecipanti tramite email, inclusi link personalizzati per la votazione.
- Calcolo dei Risultati: Dopo che i partecipanti hanno votato, l'applicazione calcola automaticamente i risultati finali.

## Tecnologie
- Java 17
- Spring Boot 3.3
- Spring Security
- PostgreSQL (per il database)
- Docker (per il deployment)
- AWS (Amazon Web Services, per il deployment e/o hosting)

## Prerequisiti
- Java 17
- Spring Boot 3.3
- Spring Security
- PostgreSQL (per il database)
- Docker (per il deployment)
- AWS (Amazon Web Services, per il deployment e/o hosting)


## Installazione e avvio
Clonare il repository

```shell
git clone <url-del-tuo-repository>
cd sivota
```
Configurare il database

Creare un database PostgreSQL vuoto.
Configurare le credenziali di accesso nel file application.properties.

Build dell'applicazione
```shell
./mvnw clean package
```
Esecuzione dell'applicazione
```shell
java -jar target/sivota-1.0.jar
```

## Utilizzo
Creazione di una nuova votazione

Crea una nuova votazione

Invio della votazione via email

Dopo che i partecipanti hanno completato la votazione, consulta i risultati finali attraverso l'interfaccia dell'applicazione.

## Autore
Questo progetto è stato sviluppato da Andrea Italiano.



=======


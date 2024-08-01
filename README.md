# VotazioniApp

## Descrizione

SiVota è un'applicazione web sviluppata per gestire il processo di creazione, gestione e partecipazione alle votazioni. La piattaforma è progettata per essere sicura e facile da usare, con una serie di funzionalità che consentono agli utenti di:

- Creare votazioni (riservato agli utenti registrati)
- Votare via email
- Visualizzare il conteggio finale dei voti
- Modificare ed eliminare votazioni

## Tecnologie Utilizzate

- **Java**
- **Spring Boot**
- **Maven**
- **PostgreSQL**
- **Docker**
- **Spring Security**
- **Spring Data JPA**
- **MapStruct**
- **Swagger**
- **OpenAPI Generator**

## Setup e Avvio

### Prerequisiti

- **Java 11+**
- **Maven**
- **Docker**

### Configurazione

1. **Clona il repository:**

    ```bash
    git clone https://github.com/SoftwareDoctor/sivota.git
    ```

2. **Naviga nella directory del progetto:**

    ```bash
    cd sivota
    ```

3. **Configura il database:**

   Assicurati di avere Docker installato e avvia un'istanza di PostgreSQL (docker desktop). Nel file application.yml vi è la configurazione relativa al DB. 

4. **Compila e avvia l'applicazione:**

    ```bash
    mvn spring-boot:run
    ```
---

**Autore:** Andrea Italiano


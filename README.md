# Operator Activities Manager Application

[![Java CI with Maven, and SonarCloud in Linux](https://github.com/cerullosalvatore/operatoractivitiesmanager/actions/workflows/sonarcloud.yml/badge.svg?branch=configuration&service=github)](https://github.com/cerullosalvatore/operatoractivitiesmanager/actions/workflows/sonarcloud.yml) [![Coverage Status](https://coveralls.io/repos/github/cerullosalvatore/operatoractivitiesmanager/badge.svg?branch=configuration&service=github)](https://coveralls.io/github/cerullosalvatore/operatoractivitiesmanager?branch=configuration)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=alert_status)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=security_rating)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager)

[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=sqale_index)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=bugs)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=code_smells)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=cerullosalvatore_operatoractivitiesmanager&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=cerullosalvatore_operatoractivitiesmanager)

## 1. Introduzione

L'applicazione che si vuole sviluppare deve essere in grado di gestire, e quindi tenere traccia delle varie attività svolte da un operatore all'interno di un contesto aziendale.

Il software deve permettere ad un utente amministratore di gestire le seguenti entità:

- **Operatori** : coloro che lavorano all'interno dell'organizzazione. Vengono identificati da una _Matricola_ e gli altri due campi sono il _Nome_ ed il _Cognome_;
- **Operazioni Base** : le operazioni che possono essere svolte all'interno dell'azienda (es. Manutenzione, Taglio, Rifinitura, ecc...) le quali sono composte da un _ID_ identificativo, un _nome_ e da una breve _Descrizione_;
- **Attività** : è un'**Operazione Base** svolta da un **Operatore** . Le informazioni che riguardano quest'entità sono quindi la _data di inizio attività_ e la _data di fine attività_, oltre che un _id_ identificativo.

In pratica, grazie a quet'applicazione, l'amministratore di una determinata azienda può eseguire le operazioni di **inserimento**, **modifica**, **eliminazione** degli _operatori_ e delle _Operazioni base_ che riguardano l'azienda stessa. Inoltre può gestire le _Attività_ svolte dai dipendenti durante l'orario lavorativo.

Le 3 entità appena presentate vanno a formare il **Domain Model** dell'applicazione, riportato nella figura seguente:

![](https://github.com/cerullosalvatore/operatoractivitiesmanager/blob/main/DomainModel.jpg)

## 2. Strumenti utilizzati

Lo scopo principale del progetto è quello di utilizzare le tecniche presentate durante il corso di _Advanced Techniques And tools for software development_. In particolare, si vuole realizzare un'applicazione software seguendo la tecnica del **Test Driven Development** e sfruttando le tecniche di **Continuos Integration**.

Il progetto è stato sviluppato in **Java** e sono stati utilizzati i seguenti strumenti:

- **JUnit** come framework di testing java;
- **Maven** per la _Build Automation_;
- **GIT** come sistema di controllo della versione (VCS) ed in particolare è stato utilizzato _GitHub_;
- **GitHub Actions** per gestire il processo della _Continuous Integration_;
- **SonarCloud** per eseguire analisi statica del codice con lo scopo di valutarne la qualità e ricercare bug;
- **Coveralls** per permettere di tenere traccia della _Code Coverage_ direttamente da una build di Github Actions;
- **PIT** come framework di _mutuation testing_;
- **Mockito** framework per effetturare il _mocking_ delle dipendenze esterne del nostro codice;
- **Docker** è un programma di virtualizzazione basato sui container ed è stato utilizzato per configurare ed eseguire il DataBase.

## 3. Model-View-Controller

Per l'applicazione in esame è stato seguito ed utilizato il paradigma del **Model-View-Controller** dove si ritrovano i seguenti 3 componenti fondamentali:

- **Model**: rappresenta il modello di dati e si rifà principalmente alle 3 entità viste in precedenza: _Operator_, _BasicOperation_, _Activity_;
- **View**: visualizza i dati contenuti nel model e si occupa dell'interazione con utenti e agenti;
- **Controller**: hanno il compito di mediare tra il modello e la vista. Riceve i comandi dell'utente (in genere attraverso il view) e li attua modificando lo stato degli altri due componenti. Inoltre hanno anche il compito di effettuare le chiamate al **DataBase** ed effettuare quindi persistenza.

## 4. Sviluppo software

Come detto in precedenza, il software è stato sviluppato seguendo la tecnica del _Test Driven Development_. Sono state affrontate diverse fasi di sviluppo tra cui ritroviamo:

1. Configurazione dell'ambiente di sviluppo e della Continuous Integration: Git, SonarCloud, Mockito, Junit;
2. Creazione del Domain Model;
3. Sviluppo dei Controller attraverso TDD (Unit Test) ;
4. Sviluppo delle MongoRepository attraverso TDD (Unit Test);
5. Integration Test di controller e MongoRepository sfruttando un MongoDB virtualizzato in Docker ed effettuando il mocking della View;
6. Sviluppo della GUI attraverso il TDD;
7. E2E testing.

Per la cronologia completa si rimanda il lettore nella sezione _Pull Request_ dove è possible osservare tutti i passaggi del ciclo di sviluppo.

## 5. Come utilizzare l'applicazione

#### 5.1 Build del progetto

1. Clonare la repository:

   `git clone https://github.com/cerullosalvatore/operatoractivitiesmanager.git`

2. Spostarsi all'interno della directory del progetto:

   `cd operatoractivitiesmanager/operatoractivitiesmanager`

3. Effettuare la build:

   `mvn clean verify`

#### 5.2 Eseguire l'applicazione

Poichè l'applicazione ha bisogno di un database MongoDB bisogna configurare un'istanza MongoDB funzionante. Per questo scopo ci serviamo di Docker e in particolare di Docker-Compose. Una volta spostati all'interno della directory `operatoractivitiesmanager/operatoractivitiesmanager` eseguiamo il comando seguente:
`docker-compose up`

Fatto questo, al fine di poter eseguire l'applicazione, bisogna seguire i seguenti passaggi:

1. Innanzitutto bisogna effettuare il _packaging_ dell'applicazione, quindi nella directory del progetto eseguiamo:

   `mvn clean package`

2. Successivamente bisogna entrare nella directory contenente il file `.jar` appena realizzato:

   `cd target`

3. Eseguiamo quindi l'applicazione (quella che contiene le dipendenze):

   `java -cp operoperatoractivitiesmanager-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.salvatorecerullo.app.operatoractivitiesmanager.app.swing.OperatorActivitiesManagerSwingApp ` 

4. E' possibile settare i seguenti parametri:

   | Opzione                           | Descrizione                                                  |
   | --------------------------------- | ------------------------------------------------------------ |
   | `--mongo-host`                    | Host del DataBase settato di default come `localhost`        |
   | `--mongo-port`                    | Porta del DataBase settata di default come `27017`           |
   | `--db-name`                       | Nome del DataBase settata di defatult come `operatoractivities` |
   | `--db-collection-operators`       | Nome della collezione degli Operatori settata di defatult come `operator` |
   | `--db-collection-basicoperations` | Nome della collezione delle Operazioni di base settata di defatult come `basicoperation` |
   | `--db-collection-activities`      | Nome della collezione delle Attività settata di defatult come `activity` |


#### 5.3 Eseguire servizi in locale

Come accennato in precedenza, al fine di poter eseguire tutti i Test, calcolare la Code Coverage e definire la Code Quality, sono stati utilizzati diversi framework  e diversi servizi. Questi possono essere utilizzati sia attraverso la Continuous Integration (come vedremo successivamente), sia eseguiti in locale. Ovviamente è necessario effettuare alcune configurazioni e installazioni di diversi plugin prima di poterli utilizzare. 

Di seguito vengono presentati i comandi principali per poter sfruttare questi strumenti:

- Per eseguire tutti i test ed effettuare mutuation testing: `mvn clean verify org.pitest:pitest-maven:mutationCoverage`;

- Per generare il report di jacoco durante il processo di build è possibile utilizzare `-Pjacoco`: `mvn clean verify -Pjacoco`;

- Per inviare i report jacoco appena prodotti ci basta sfruttare il comando: `mvn coveralls:report -DrepoToken=<drepo_token> `. Per effettuare questa operazione è necessario effettuare l'autenticazione su coveralls attraverso il token di identificazione `<drepo_token>`. E', inoltre, necessario che la build  `mvn clean verify` sia stata eseguita prima di procedere con questa operazione.

- Per generare sia i report degli Unit Test che quelli degli Integration Test è possibile esegurie il seguente comando: `mvn surefire-report:report-only surefire-report:failsafe-report-only org.apache.maven.plugins:maven-site-plugin:3.7.1:site -DgenerateReports=false`. E' necessario che la build  `mvn clean verify` sia stata eseguita prima di procedere con questa operazione.]

- Per misurare la qualità del codice è stato utilizzato _SonarCloud_. Per eseguirla: 

  `mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=<project_key>`. 

  Come in precedenza è necessario, innanzitutto effettuare la build eseguendo `-Pjacoco` e successivamente eseguire il comando appena presentato. Per eseguire questo processo è richiesto java 11. E' inoltre fondamentale settare la variabile d'ambiente `SONAR_TOKEN` fornitaci da SonarCloud al fine di poter effettuare l'accesso al servizio durante l'esecuzione. `<project_key>` è la chiave unica del progetto che viene fornitaci dalla piattaforma SonarCloud.

Per eseguire il tutto con un unico comando:

` mvn clean verify -Pjacoco org.pitest:pitest-maven:mutationCoverage org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=<project_key> coveralls:report -DrepoToken=<drepo_token> surefire-report:report-only surefire-report:failsafe-report-only org.apache.maven.plugins:maven-site-plugin:3.7.1:site -DgenerateReports=false`

#### 5.4 Integrazione Continua

Al fine di poter eseguire le operazioni precedentemente presentate su un server remoto per la CI è fondamentale configurare un servizio di Continuous Integration. Nel caso in esame è stato utilizzato **GitHub Workflow** sfruttando una repository GitHub.

Il file di configurazione per eseguire questo processo di Continuous Integration è [sonarcloud.yml](https://github.com/cerullosalvatore/operatoractivitiesmanager/blob/main/.github/workflows/sonarcloud.yml). Al fine di poter eseguire e controllare lo stato di Coveralls e SonarCloud sono state impostate alcune variabili d'ambiente:

| Variabile d'ambiente | Decrizione                                                   |
| -------------------- | ------------------------------------------------------------ |
| GITHUB_TOKEN         | impostato di default da GitHub come segreto                  |
| SONAR_TOKEN          | fornito da Sonarcloud durante la fase di inizializzazione; questo deve essere impostato come GitHub Secret |
| COVERALLS_TOKEN      | fornito da Coveralls durante la fase di inizializzazione; questo deve essere impostato come GitHub Secret |

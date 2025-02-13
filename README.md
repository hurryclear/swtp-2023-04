# SWTP-04

## 1. Projektbeschreibung
Die Webanwendung dient der Modulanrechnung an der Universität Leipzig.

## 2. Verwendete Technologien und Tools
Die Microservices werden durch Docker verwaltet, während das Repository auf GitLab gehostet wird. Eine CI/CD-Pipeline mit drei Stufen (Test, Build, Deploy) läuft ebenfalls auf GitLab.

### Frontend
- Node.js wird für das Frontend benötigt.
- Die Frontend-Entwicklung erfolgt mit Vue.js und Vuetify für das Styling. Die relevanten Abhängigkeiten sind:
  - Vue.js
  - Vuetify (für das Styling)
  - Axios (für API-Aufrufe)
  - Vue I18n (für Internationalisierung)
  - Vue Router (für Routing)
  - Vuex (für das Speichern von Zuständen)
- Die Entwicklungsumgebung ist konfiguriert mit Vue CLI und den entsprechenden Plugins für Babel und ESLint.

### Backend
- Das Backend basiert auf Java 17 und dem Spring Boot Framework.
- Maven wird als Build-Tool verwendet.
- PostgreSQL dient als Datenbank.
- Die relevanten Dependencies sind:
  - Spring Boot Web
  - Spring Boot Data JPA
  - Modelmapper
  - Lombok
  - Apache PdfBox
  - Spring Boot Security
  - Auth0 Java Jwt
- Für Tests werden folgende Tools und Frameworks verwendet:
  - Spring Boot Test
  - Spring Security Test
  - H2 Database (für Integrationstests)
  - Surefire Plugin (für Unit-Tests)
  - Failsafe Plugin (für Integrationstests)

### Datenbank
PostgreSQL wird als Datenbank verwendet.

## 3. Einrichtung der Entwicklungsumgebung

### Frontend
Um die Frontend-Abhängigkeiten zu installieren, navigieren Sie zum Frontend-Verzeichnis (`cd frontend`) und führen Sie den Befehl `npm install` aus. Stellen Sie sicher, dass [Node.js](https://nodejs.org/en/) installiert ist.

### Backend
Das Spring Boot Backend kann mit dem integrierten Maven Wrapper gestartet werden. Um die Container zu starten, verwenden Sie den Docker-Daemon von [Docker Desktop](https://www.docker.com/products/docker-desktop/). Verwenden Sie `docker-compose up --build`, um die Anwendung zu starten, und `docker-compose down`, um die Microservices herunterzufahren.

### Gesamtes Projekt lokal starten
Um die Container zu starten, benutzen wir den Docker daemon von Docker Desktop, das heißt, Docker Desktop muss vorher installiert werden.
[Docker Desktop Installation](https://www.docker.com/products/docker-desktop/)
Wenn das Projekt mit Docker Compose gestartet wird, werden die Tests von Surefire und Failsafe übersprungen.

## 4. Lokale Entwicklung
Wichtige Umgebungsvariablen werden im `docker-compose.yml` für Development und im `docker-compose-prod.yml` für die Produktion aufgeführt.

### Frontend
Navigieren Sie zum Frontend-Verzeichnis und verwenden Sie `npm run serve`, um das Frontend ohne Backend zu starten.

### Backend

In den `application.properties` unter `resources` im Haupt- bzw. Testverzeichnis sind zudem wichtige Konfigurationen für die einzelnen Profile enthalten.
Zu Beachten ist, dass zum Testen eine In-Memory H2 Database mit PostgresDialect konfiguriert ist. Im Dev bzw. Prod Profile werden richtige Postgres Datenbanken verwendet.

Das Backend besteht aus:
- Controllern, wo die Endpunkte stehen, um Requests entgegenzunehmen
- Services, wo die eigentliche Businesslogik steht
- Repositories, wo die Dtos sowie Entitys und Repositories stehen, also alles um mit den Daten und der Datenbank zu interagieren.
- Security, dort befindet sich alles zur Authentifizierung und Autorisierung von Nutzern (eigener Controller, Service und Repository inklusive)
- DataInitializer, wo Commandlinerunner enthalten sind, die die Datenbank zum Testen mit Daten füllen.
- Deserializer, wo die JSONs des Frontends in Dto transformiert werden, um besser mit den Daten arbeiten zu können.
- Exceptions, wo eigene Exceptions definiert sind, welche als Fehlermeldung ans Frontend weitergeleitet werden können.

### Weiterentwicklung (mögliche Features zur Implementierung)
- Accountmanagement für Mitarbeitende der Universität
- FAQ bzw. Assistent für Nutzende der Software
- Barrierefreiheit (aktuell nur unterschiedliche Sprachen in Webanwendung)
- Umrechnung verschiedener Credit Point-Modelle
- Generelle Verbesserung der PDF Summarys (Uni-Design, Multilingual, Kontaktinformationen Studienbüro etc.)
- Update-Funktion für Anträge - Nachbearbeitung durch Studenten nach formaler Ablehnung
- Feedback-Möglichkeiten für Studenten
- Optimierung Leistung und Skalierbarkeit

## 5. Testen
Um die maven wrapper Befehle ausführen zu können, muss erst ins Backend-Verzeichnis navigiert werden: `cd .\backend\`
Backend-Unit-Tests können über den Befehl `.\mvnw test` gestartet werden (oder in der IDE wie beispielsweise IntelliJ IDEA).

Integrationstests sollten mit dem Profil "integration" gestartet werden. Beim Ausführen des Befehls `mvnw` wird das hausinterne Flag `skip.ut` auf `false` gesetzt, um die Surefire Unittests zu überspringen und nur die Integrationstests auszuführen. Beispielbefehl:

```bash
 .\mvnw "-Dskip.ut=false" "-Dspring.profiles.active=integration" verify
```

Beim Committen ins Repository werden im Feature-Branch die Backend- und Frontend-Test-Stages ausgeführt, im Development-Branch zusätzlich die Test-Integration-Stage.

## 6. Dokumentation
Der Code ist ausführlich kommentiert.
Im [Frontend directory](./frontend) befindet sich eine README zum Thema Vue.js.
Im [Backend directory](./backend) gibt es eine README zu API Endpoints.

## 7. Deployment und Produktionsumgebung
Für das Deployment und die Konfiguration der Produktionsumgebung nutzen wir Docker und Docker Compose.
Das Deployment übernimmt die CI/CD-Pipeline von GitLab, welche aus dem Frontend und Backend Docker Images erstellt und sie auf Dockerhub pusht.
Danach wird per SSH eine Verbindung zur VM aufgebaut und die Docker Container gebaut und gestartet.

Um mit der Produktionsumgebung, also der VM, zu interagieren, muss man sich im Universitätsnetz der Uni Leipzig befinden oder per Cisco VPN mit dem Uni-Netz verbinden.
Die Webanwendung wird unter 172.26.92.83:3000 erreicht. Dies entspricht unserem Frontend Container, der auch als Proxy für das Backend dient.
Alle Anfragen an 172.26.92.83:3000/api/ werden an das Backend weitergeleitet.
Um sich direkt mit der VM zu verbinden, wird der Private SSH Key benötigt, der im GitLab als Secret Variable gespeichert ist.
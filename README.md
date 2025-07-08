# ⏱️ Timekiller – Produktivität effizient tracken

## 📌 Projektbeschreibung

**Timekiller** ist eine Java-Anwendung zur produktiven Selbstorganisation. Sie ermöglicht es Nutzer:innen, ihre täglichen Aufgaben zu tracken, deren Dauer zu erfassen und die Ergebnisse visuell über ein Kuchendiagramm auszuwerten. Entwickelt wurde das Projekt im Rahmen des Moduls **Software Quality and Testing** (Wintersemester 2023/24) im Masterstudiengang *Computer Science and Media* an der Hochschule der Medien Stuttgart.

## ✨ Hauptfunktionen

- Aufgaben-Tracking per Klick (Start/Stopp)
- Dynamisches Laden und Speichern der Daten über eine SQLite-Datenbank
- Visualisierung der Tätigkeitsverteilung per Pie Chart
- Filterung der Daten über ein DatePicker-Interface
- Aufgaben erstellen, bearbeiten und löschen

## 🧪 Qualitätssicherung & Tests

Dieses Projekt zeichnet sich durch eine konsequente Integration moderner Qualitätssicherungsmaßnahmen aus:

### ✅ Statische Codeanalyse

- **Checkstyle** mit angepasstem Sun-Check-Profil (z. B. Einrückung, Zeilenlängen, Naming-Conventions)
- **SpotBugs** zur Erkennung potenzieller Fehlermuster

### 🧩 Komponententests

- Getestete Klassen: `Task`, `DurationTracker`, `TaskListImpl`, `PieChartHelper`
- Einsatz von JUnit und Mockito
- Whitebox-Tests inkl. Zustandsdiagramm und Pfadüberdeckungsanalyse

### 🔄 Integrationstests

- Tests der Datenbankintegration (CRUD-Funktionalität)
- Tests der GUI-Komponenten (u. a. DatePicker, PieChart)
- Nutzung von Back Door Manipulation und Round-Trip-Verfahren zur Datenverifikation

### 🤖 Continuous Integration

- Jenkins-Pipeline mit automatisierten Build-, Checkstyle-, SpotBugs- und Test-Stufen
- Verwendung von JaCoCo zur Testabdeckungsanalyse
- Testdatenbank zur Isolierung der Testumgebung

### 🧪 Test-Driven Development

- TDD-Umsetzung bei der Entwicklung der `DatePicker`-Komponente
- Zyklus: Test schreiben → minimale Implementierung → Refactoring

## 🔧 Technologie-Stack

- Java 17
- Gradle
- SQLite
- JUnit 5
- Mockito
- Jenkins
- Checkstyle, SpotBugs, JaCoCo

## 🚀 Build & Ausführen

### Voraussetzungen

- JDK 17 oder höher
- Gradle (Wrapper enthalten)
- SQLite

### Projekt bauen

```bash
./gradlew build
```

### Tests ausführen

```bash
./gradlew test
```

### Checkstyle & SpotBugs

```bash
./gradlew customCheckstyle
./gradlew spotbugsMain
```

## 👨‍💻 Mitwirkende

- **Carolin Annie Calvetti** (cc033)
- **Alexander Kraus** (ak364)

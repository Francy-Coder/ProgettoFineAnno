```text
Ora ho letto tutto il codice. Ecco l'analisi completa di chi ha fatto cosa, basata sia sul documento che sul codice effettivo:

---

## 👨‍💻 Tresoldi Francesco — Informatica (Navigazione, GPS & AI)

File: `NavigationActivity.java`, `DestinationActivity.java`, `CameraAnalysisActivity.java`, `obstacle_detector.py`

- `NavigationActivity.java` — Gestisce la navigazione in tempo reale: acquisisce la posizione GPS tramite `LocationManager`, gestisce i provider (GPS, rete, fallback), dà feedback vocale con `TextToSpeech` e vibrazione, mostra le coordinate sullo schermo. Bottoni per ripetere l'istruzione e fermare la navigazione.
- `DestinationActivity.java` — Schermata inserimento destinazione: input testuale + riconoscimento vocale (`RecognizerIntent`), avvio navigazione passando la destinazione a `NavigationActivity`.
- `CameraAnalysisActivity.java` — Gestisce la fotocamera e invia frame al server AI per il rilevamento ostacoli e semafori.
- `obstacle_detector.py` — Server Flask con endpoint `/analizza` e `/semaforo`: utilizza MobileNetV3 e OpenCV HSV per rilevare ostacoli e riconoscere il colore del semaforo, inviando feedback vocali all’app.

---

## 👨‍💻 Bellomo Christian — Informatica (Interazioni utente & Accessibilità)

File: `MainActivity.java`, `FavoritePlacesActivity.java`, `HistoryActivity.java`

- `MainActivity.java` — Schermata principale: gestisce tutti i bottoni di navigazione, il riconoscimento vocale globale (`handleVoiceCommand` con comandi come "vai a casa", "emergenza", ecc.), il pulsante di emergenza che chiama il 112, e la gestione dei permessi (microfono, telefono).
- `FavoritePlacesActivity.java` — Lista luoghi preferiti con bottoni rapidi "Casa" e "Lavoro", carica i dati dal database Room in background.
- `HistoryActivity.java` — Mostra la cronologia degli ultimi 20 percorsi con distanza e durata.

---

## 🖥️ Serghej Roberto Calegari — TPSI (Interfaccia XML)

File: tutta la cartella `res/layout/` e `res/values/`

- `activity_main.xml`, `activity_destination.xml`, `activity_navigation.xml`, `activity_login.xml`, `activity_register.xml`, `activity_favorite_places.xml`, `activity_history.xml`, `activity_camera_analysis.xml`, `item_history.xml` — tutti i layout delle schermate con pulsanti grandi e accessibili.
- `colors.xml`, `dimens.xml`, `strings.xml`, `themes.xml` — colori, dimensioni, stringhe e tema dell'app.
- `btn_round.xml` — stile grafico per i pulsanti arrotondati.

---

## 🗄️ Bartucca Alessio — Sistemi (Database SQL)

File: `schema.sql`, `AppDatabase.java`, `Utente.java` (entity), `LuogoPreferito.java`, `Percorso.java`, `CronologiaPercorso.java`, `FeedbackSicurezza.java`, `UtenteDao.java`, `LuogoDao.java`, `PercorsoDao.java`

- `schema.sql` — Schema MySQL completo con 6 tabelle: `utenti`, `luoghi_preferiti`, `percorsi`, `cronologia_percorsi`, `punti_percorso`, `feedback_sicurezza`, con chiavi esterne, CASCADE, CHECK constraints.
- `AppDatabase.java` — Configurazione Room (SQLite Android) con singleton thread-safe.
- Le entity (`Utente`, `LuogoPreferito`, `Percorso`, ecc.) — Modelli del database con annotazioni Room.
- I DAO (`UtenteDao`, `LuogoDao`, `PercorsoDao`) — Query SQL per inserimento, ricerca, cronologia.

---

## ⚙️ Crippa Mattia — Sistemi (Backend Spring Boot)

File: `VisionGoApplication.java`, `controller/AuthController.java`, `controller/LuoghiController.java`, `controller/CronologiaController.java`, `model/Utente.java`, `repository/UtenteRepository.java`, `security/JwtUtil.java`, `ApiService.java`, `RetrofitClient.java`, `application.properties`

- `AuthController.java` — API REST per login (`POST /api/auth/login`) e registrazione (`POST /api/auth/registrazione`) con hashing password BCrypt e generazione token JWT.
- `LuoghiController.java` — API per gestire i luoghi preferiti (GET, POST, DELETE) con verifica JWT.
- `CronologiaController.java` — API per salvare e recuperare la cronologia dei percorsi.
- `JwtUtil.java` — Generazione e validazione token JWT.
- `ApiService.java` + `RetrofitClient.java` — Client Retrofit lato Android per chiamare il backend.
```

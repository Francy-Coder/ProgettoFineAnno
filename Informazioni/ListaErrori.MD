# Errori

## Icona App
- Cambiare l’icona dell’app utilizzando una bussola viola, sia nell’applicazione sia nella pagina di accesso.
- Utilizzare come riferimento questa immagine: https://it.freepik.com/psd-premium/l-icona-di-un-app-viola-e-bianca-con-un-cerchio-bianco-nella-parte-inferiore_39816842.htm

## Pagina Accesso
- Verificare il corretto funzionamento dell’accesso.
- La funzione “Password dimenticata” non funziona.
- Il pulsante “Registrati” è troppo piccolo: aumentare dimensioni e area cliccabile per migliorare l’accessibilità agli utenti non vedenti o ipovedenti.
- Specificare dove vengono salvati i dati dell’utente e permettere la visualizzazione/modifica delle informazioni personali.
- Aggiungere una nota vocale finale più formale, ad esempio:  
  “Se riscontri difficoltà durante l’accesso, ti consigliamo di chiedere supporto a una persona di fiducia.”
- 🔧 Aggiungere feedback vocali/TTS quando il login viene completato correttamente oppure quando si verifica un errore.
- 🔧 Implementare indicazioni vocali durante la compilazione dei campi (email/password).

## Pagina Registrazione
- Verificare il corretto funzionamento della registrazione.
- Il pulsante “Accedi” è troppo piccolo: aumentare dimensioni e area cliccabile per migliorare l’accessibilità.
- Aggiungere una nota vocale finale più formale, ad esempio:  
  “Se riscontri difficoltà durante la registrazione, ti consigliamo di chiedere supporto a una persona di fiducia.”
- 🔧 Aggiungere conferma vocale dopo la registrazione completata con successo.
- 🔧 Leggere tramite TTS eventuali errori nei campi compilati (email non valida, password troppo corta, ecc.).
- 🔧 Implementare controlli automatici sulla password (sicurezza minima richiesta).

## Pagina Home
Nella tendina/menu mancano:
- Implementare la sezione “Profilo”, mostrando i dati inseriti dall’utente in fase di registrazione/accesso.
- Implementare la sezione “Impostazioni”, con possibilità di scegliere:
  - tema chiaro/scuro;
  - dimensione del testo;
  - velocità della voce TTS.
- Implementare la sezione “Aiuto” con spiegazione delle funzionalità principali dell’app.
- 🔧 Aggiungere una guida vocale introduttiva al primo avvio dell’app.
- 🔧 Inserire un pulsante rapido “Torna alla Home”.

## Funzione Inserisci Destinazione
- Leggere correttamente tutte le istruzioni disponibili, compresi esempi come “Stazione Centrale”.
- Migliorare il riconoscimento dei messaggi inseriti dall’utente sia tramite input testuale sia vocale.
- Migliorare la stabilità della posizione GPS:
  - se l’utente è fermo, l’app non deve fornire istruzioni multiple o contrastanti.
- Migliorare la precisione delle informazioni di navigazione:
  - esempio: se l’utente si trova a Sesto San Giovanni e deve raggiungere il Duomo, l’app non deve indicare erroneamente che la destinazione è vicina.
- Implementare il rilevamento dei semafori:
  - l’app deve notificare la presenza del semaforo;
  - tramite AI deve riconoscere il colore del semaforo;
  - deve comunicare se è sicuro attraversare.
- Descrivere chiaramente ciò che la videocamera rileva, così che l’utente possa comprendere meglio l’ambiente circostante.
- Dopo il calcolo del percorso:
  - notificare che la destinazione è stata trovata;
  - spiegare le funzionalità disponibili (avvia navigazione, percorso alternativo, salva percorso, ecc.).
- Implementare o completare l’AI per il rilevamento ostacoli:
  - riconoscere ostacoli davanti all’utente;
  - indicarne il tipo (pedone, bicicletta, monopattino, oggetto, gradino, ecc.).
- Mostrare visivamente a schermo il rilevamento ostacoli (es. riquadro evidenziato sull’oggetto rilevato).
- Se viene inserita una via inesistente:
  - notificare chiaramente che l’indirizzo non esiste.
- Se il GPS impiega troppo tempo:
  - mostrare un messaggio di errore o stato (“Ricerca posizione in corso…”).
- Durante l’acquisizione del GPS:
  - rimuovere informazioni errate o placeholder come “Stazione Centrale 5 min”.
- Rendere leggermente più grandi i pulsanti:
  - “Ripeti istruzione”;
  - “Ferma navigazione”.
- 🔧 Aggiungere vibrazione/haptic feedback durante:
  - cambio direzione;
  - presenza ostacoli;
  - attraversamenti.
- 🔧 Implementare modalità offline/base nel caso in cui internet non sia disponibile.
- 🔧 Aggiungere comando vocale rapido:
  - “Ripeti”;
  - “Dove sono?”;
  - “Ferma navigazione”.

## Funzione Luoghi Preferiti
- Rendere più grande il pulsante “Aggiungi luogo”.
- Dopo aver premuto il pulsante:
  - spiegare tramite TTS cosa è possibile fare.
- Leggere vocalmente cosa inserire nei campi input.
- I pulsanti “Casa” e “Lavoro” non funzionano.
- Quando si aggiunge un luogo:
  - l’app ritorna erroneamente alla Home.
- Correggere e completare il sistema di aggiunta luoghi preferiti.
- 🔧 Aggiungere conferma vocale:
  - “Luogo salvato correttamente”.
- 🔧 Permettere la modifica e l’eliminazione dei luoghi salvati.

## Funzione Cronologia
- La cronologia attualmente non funziona.
- Implementare la lettura della cronologia tramite TTS.
- 🔧 Permettere di riavviare rapidamente una navigazione dalla cronologia.
- 🔧 Ordinare la cronologia per:
  - data;
  - luoghi più utilizzati.
- 🔧 Aggiungere un pulsante “Cancella cronologia”.

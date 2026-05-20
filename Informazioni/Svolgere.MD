# Come testare
## Prerequisiti
- Avere il codice sorgente.
- Avere le dipendenze installate (es. app, librerie, virtualenv).

## Esecuzione
Aprire il terminale e lanciare:
```bash
python obstacle_detector.py
```

# Svolgimento (attività di test)
1. Eseguire l'applicazione.
2. Riprodurre i casi d'uso principali (inserimento destinazione, navigazione, rilevamento ostacoli).
3. Annotare tutti gli errori e i comportamenti inattesi in una lista strutturata.
4. Creare branch/commit su GitHub per i fix e pushare i cambiamenti con messaggi chiari.

# Lista errori e richieste di miglioramento

## Errore 1 — Inserimento destinazione / riconoscimento messaggio
- La funzionalità "inserisci destinazione" deve:
  - Leggere correttamente il messaggio inserito dall'utente (input/voice).
  - Quando si avvia la navigazione: rimuovere automaticamente la stazione centrale dopo 5 minuti (comportamento richiesto).
  - Migliorare la stabilità della posizione: se l'utente è fermo non deve fornire istruzioni multiple o contrastanti.
  - Appena calcolato il percorso verso la destinazione, notificare l'utente che la destinazione è stata trovata e indicare le funzionalità disponibili (es. avvia navigazione, alternativa, salva percorso).
  - Implementare o completare l'AI per il rilevamento ostacoli davanti all'utente: deve indicare la presenza e il tipo di ostacolo (es. pedone, bicicletta, oggetto).
  - etc...

## Errore 2
- ...

-- Crea prima il database
CREATE DATABASE IF NOT EXISTS visiongo_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE visiongo_db;

-- 1. Tabella utenti (nessuna dipendenza)
CREATE TABLE utenti (
    id_utente        INT PRIMARY KEY AUTO_INCREMENT,
    nome             VARCHAR(100),
    email            VARCHAR(150) UNIQUE NOT NULL,
    password_hash    VARCHAR(255) NOT NULL,
    data_creazione   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usa_audio        BOOLEAN DEFAULT TRUE,
    livello_dettaglio ENUM('basso','medio','alto') DEFAULT 'medio',
    evita_traffico   BOOLEAN DEFAULT TRUE
);

-- 2. Luoghi preferiti (dipende da utenti)
CREATE TABLE luoghi_preferiti (
    id_luogo   INT PRIMARY KEY AUTO_INCREMENT,
    id_utente  INT,
    nome       VARCHAR(100),
    indirizzo  VARCHAR(255),
    latitudine  DECIMAL(10,8),
    longitudine DECIMAL(11,8),
    tipo       ENUM('casa','lavoro','tempo_libero','altro') DEFAULT 'altro',
    FOREIGN KEY (id_utente) REFERENCES utenti(id_utente)
        ON DELETE CASCADE
);

-- 3. Percorsi (dipende da utenti)
CREATE TABLE percorsi (
    id_percorso          INT PRIMARY KEY AUTO_INCREMENT,
    id_utente            INT,
    nome                 VARCHAR(150),
    punto_partenza       VARCHAR(255),
    punto_arrivo         VARCHAR(255),
    distanza_km          DECIMAL(5,2),
    durata_minuti        INT,
    livello_sicurezza    ENUM('basso','medio','alto'),
    presenza_semafori    BOOLEAN,
    presenza_marciapiedi BOOLEAN,
    FOREIGN KEY (id_utente) REFERENCES utenti(id_utente)
        ON DELETE CASCADE
);

-- 4. Cronologia (dipende da utenti E percorsi)
CREATE TABLE cronologia_percorsi (
    id_cronologia      INT PRIMARY KEY AUTO_INCREMENT,
    id_utente          INT,
    id_percorso        INT,
    data_utilizzo      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completato         BOOLEAN DEFAULT TRUE,
    tempo_effettivo    INT,
    difficolta_percepita INT CHECK (difficolta_percepita BETWEEN 1 AND 5),
    FOREIGN KEY (id_utente)   REFERENCES utenti(id_utente)   ON DELETE CASCADE,
    FOREIGN KEY (id_percorso) REFERENCES percorsi(id_percorso) ON DELETE CASCADE
);

-- 5. Punti percorso (dipende da percorsi)
CREATE TABLE punti_percorso (
    id_punto    INT PRIMARY KEY AUTO_INCREMENT,
    id_percorso INT,
    ordine      INT,
    descrizione TEXT,
    latitudine  DECIMAL(10,8),
    longitudine DECIMAL(11,8),
    tipo        ENUM('incrocio','semaforo','attraversamento','ostacolo','altro'),
    FOREIGN KEY (id_percorso) REFERENCES percorsi(id_percorso)
        ON DELETE CASCADE
);

-- 6. Feedback sicurezza (dipende da utenti E percorsi)
CREATE TABLE feedback_sicurezza (
    id_feedback       INT PRIMARY KEY AUTO_INCREMENT,
    id_utente         INT,
    id_percorso       INT,
    descrizione       TEXT,
    tipo              ENUM('ostacolo','lavori','pericolo','altro'),
    gravita           INT CHECK (gravita BETWEEN 1 AND 5),
    data_segnalazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_utente)   REFERENCES utenti(id_utente)   ON DELETE CASCADE,
    FOREIGN KEY (id_percorso) REFERENCES percorsi(id_percorso) ON DELETE CASCADE
);
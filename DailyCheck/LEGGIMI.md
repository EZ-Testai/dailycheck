# DailyCheck — widget personale per la lista giornaliera

Tool privato, nessun server, nessun account: tutti i dati restano sul telefono.

## Come ottenere l'APK (solo browser, nessun software sul PC)

1. Crea un account gratuito su https://github.com
2. Crea un nuovo repository: pulsante **New**, nome `dailycheck`, **Public**
   (Public è gratuito; il codice non è segreto ma il contenuto del widget resta sul tuo telefono)
3. Nella pagina del repository clicca **uploading an existing file**
4. Trascina nella pagina TUTTO il contenuto di questa cartella:
   file e cartelle che vedi, inclusa la cartella `.github`
   (se non la vedi in Esplora risorse di Windows: scheda Visualizza → spunta **Elementi nascosti**)
5. Dopo il caricamento vai sulla scheda **Actions** in alto: il workflow **Build APK**
   parte da solo. Se GitHub chiede di autorizzarlo, premi **I understand my workflows,
   go ahead and enable them** e poi, se serve, **Run workflow**.
6. Quando accanto al nome del workflow compare il segno verde, aprilo:
   in basso trovi **Artifacts → app-debug-apk** → scaricalo.
7. Invia il file `app-debug.apk` sul telefono (email, Telegram, Drive...) e aprilo per installarlo.
   Android chiederà di autorizzare l'installazione da quell'app: consenti.
8. Sulla home del telefono: tieni premuto → **Widget** → **DailyCheck** → trascinalo dove vuoi.
   (esiste solo per la home: sulla schermata di blocco non può comparire, come richiesto)

## Uso

- Il widget mostra **giorno e data in alto** e le attività con la spunta.
- Tocca una riga per spuntarla / togliere la spunta. Le completate restano barrate.
- Il tasto **＋** in basso apre l'app per aggiungere una nuova attività.
- Nell'app: scrivi nel campo e premi **Aggiungi**; tocca una voce per eliminarla.
- Pulsante **Storico giorni passati**: elenco dei giorni con "X/Y completate";
  tocca un giorno per vedere la lista di quel giorno con ✅ e ⬜.
- Ogni notte la lista riparte da zero da sola; i giorni passati restano salvati.
- Le attività aggiunte con **＋** restano nella lista di tutti i giorni successivi.

## Se un giorno vorrai modificare qualcosa

Tutte le modifiche si fanno dal browser su github.com: apri il file, matita in alto a destra,
modifica, Commit. Il workflow ricompila l'APK da solo e lo trovi di nuovo in **Actions → Artifacts**.

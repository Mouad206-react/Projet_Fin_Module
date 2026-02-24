package com.example.bouchpro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bouchpro.models.Client;
import com.example.bouchpro.models.Produit;
import com.example.bouchpro.models.Respo;
import com.example.bouchpro.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    // Singleton
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Constructeur
    private DatabaseHelper(Context context) {
        super(context, "boucherie.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table Client
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS table_client (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nom TEXT, " +
                        "telephone TEXT)"
        );

        // Table des Responsables
        db.execSQL("CREATE TABLE table_respo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT, " +
                "role TEXT)");

        // Modifier Table Produit pour inclure le respo_id
        db.execSQL("CREATE TABLE table_produit (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT, " +
                "prix REAL, " +
                "stock REAL, " +
                "date_jour TEXT, " +
                "respo_id INTEGER, " + // <--- Lien vers le responsable
                "FOREIGN KEY(respo_id) REFERENCES table_respo(id))");

        // Table Transaction (Crédit / Paiement)
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS table_transaction (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "client_id INTEGER, " +
                        "type TEXT, " +
                        "montant REAL, " +
                        "date TEXT, " +
                        "FOREIGN KEY(client_id) REFERENCES table_client(id))"
        );
    }

    public boolean addClient(String nom, String telephone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("telephone", telephone);

        long result = db.insert("table_client", null, values);
        return result != -1; // Retourne vrai si l'insertion a réussi
    }

    public boolean addTransaction(int clientId, String type, double montant, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("client_id", clientId);
        values.put("type", type);
        values.put("montant", montant);
        values.put("date", date);

        long result = db.insert("table_transaction", null, values);
        return result != -1;
    }
    public List<Client> getAllClients() {
        List<Client> clientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM table_client", null);

        if (cursor.moveToFirst()) {
            do {
                Client client = new Client(
                        cursor.getInt(0), // id
                        cursor.getString(1), // nom
                        cursor.getString(2)  // telephone
                );
                clientList.add(client);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return clientList;
    }

    public boolean addRespo(String nom, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("role", role);
        return db.insert("table_respo", null, values) != -1;
    }

    public List<Respo> getAllRespos() {
        List<Respo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM table_respo", null);
        if (c.moveToFirst()) {
            do {
                list.add(new Respo(c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public double getClientSolde(int clientId) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        // On calcule : Somme(CREDIT) - Somme(PAIEMENT)
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "SUM(CASE WHEN type = 'CREDIT' THEN montant ELSE 0 END) - " +
                        "SUM(CASE WHEN type = 'PAIEMENT' THEN montant ELSE 0 END) " +
                        "FROM table_transaction WHERE client_id = ?",
                new String[]{String.valueOf(clientId)});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
    public List<Transaction> getTransactionsByClient(int clientId) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // On récupère tout pour un client donné
        Cursor cursor = db.rawQuery("SELECT * FROM table_transaction WHERE client_id = ? ORDER BY id DESC",
                new String[]{String.valueOf(clientId)});

        if (cursor.moveToFirst()) {
            do {
                Transaction trans = new Transaction(
                        cursor.getInt(0), // id
                        cursor.getInt(1), // client_id
                        cursor.getString(2), // type (CREDIT/PAIEMENT)
                        cursor.getDouble(3), // montant
                        cursor.getString(4)  // date
                );
                transactionList.add(trans);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }

    // Dans DatabaseHelper.java
    public List<Produit> getProduitsByDateSpecifique(int respoId, String dateChoisie) {
        List<Produit> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // On cherche exactement la date sélectionnée
        Cursor c = db.rawQuery("SELECT * FROM table_produit WHERE respo_id = ? AND date_jour = ?",
                new String[]{String.valueOf(respoId), dateChoisie});

        if (c.moveToFirst()) {
            do {
                list.add(new Produit(c.getInt(0), c.getString(1), c.getDouble(2),
                        c.getDouble(3), c.getString(4), c.getInt(5)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean addProduit(String nom, double prix, double stock, String date, int respoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("prix", prix);
        values.put("stock", stock);
        values.put("date_jour", date);
        values.put("respo_id", respoId);
        return db.insert("table_produit", null, values) != -1;
    }

    // 1. Voir tout l'historique d'un responsable
    public Cursor getStockByRespo(int respoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM table_produit WHERE respo_id = ? ORDER BY date_jour DESC",
                new String[]{String.valueOf(respoId)});
    }

    // 2. Voir seulement les produits d'aujourd'hui
    public Cursor getStockToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM table_produit WHERE date_jour = date('now')", null);
    }

    // 3. Voir le bilan de la semaine
    public Cursor getStockThisWeek() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM table_produit WHERE date_jour >= date('now', '-7 days')", null);
    }

    public List<Produit> getProduitsFiltre(int respoId, String periode) {
        List<Produit> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        switch (periode) {
            case "JOUR":
                query = "SELECT * FROM table_produit WHERE respo_id = ? AND date_jour = date('now')";
                break;
            case "SEMAINE":
                query = "SELECT * FROM table_produit WHERE respo_id = ? AND date_jour >= date('now', '-7 days')";
                break;
            case "MOIS":
                query = "SELECT * FROM table_produit WHERE respo_id = ? AND date_jour >= date('now', '-30 days')";
                break;
            default: // TOUT
                query = "SELECT * FROM table_produit WHERE respo_id = ? ORDER BY date_jour DESC";
                break;
        }

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(respoId)});
        if (c.moveToFirst()) {
            do {
                list.add(new Produit(c.getInt(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getString(4), c.getInt(5)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void deleteClient(int clientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 1. Supprimer les transactions du client
        db.delete("table_transaction", "client_id = ?", new String[]{String.valueOf(clientId)});
        // 2. Supprimer le client
        db.delete("table_client", "id = ?", new String[]{String.valueOf(clientId)});
        db.close();
    }

    public void deleteRespo(int respoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 1. Supprimer les produits enregistrés par ce responsable
        db.delete("table_produit", "respo_id = ?", new String[]{String.valueOf(respoId)});
        // 2. Supprimer le responsable
        db.delete("table_respo", "id = ?", new String[]{String.valueOf(respoId)});
        db.close();
    }

    public long addProduitAndGetId(String nom, double prix, double stock, String date, int respoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("prix", prix);
        values.put("stock", stock);
        values.put("date_jour", date);
        values.put("respo_id", respoId);

        // insert() renvoie l'ID de la nouvelle ligne (long)
        return db.insert("table_produit", null, values);
    }
    // Récupérer l'historique d'un responsable précis
    public List<Produit> getProduitsByRespo(int respoId) {
        List<Produit> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM table_produit WHERE respo_id = ? ORDER BY date_jour DESC", new String[]{String.valueOf(respoId)});
        if (c.moveToFirst()) {
            do {
                // Cette ligne va maintenant fonctionner car le constructeur accepte 6 paramètres
                list.add(new Produit(
                        c.getInt(0),      // id
                        c.getString(1),   // nom
                        c.getDouble(2),   // prix
                        c.getDouble(3),   // stock
                        c.getString(4),   // date
                        c.getInt(5)       // respoId
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS table_transaction");
        db.execSQL("DROP TABLE IF EXISTS table_produit");
        db.execSQL("DROP TABLE IF EXISTS table_client");
        onCreate(db);
    }
}
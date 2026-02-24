package com.example.bouchpro.models;

public class Transaction {
    private int id;
    private int clientId;      // relation avec Client
    private String type;
    private double montant;    // montant en valeur numérique
    private String date;

    // Constructeur
    public Transaction(int id, int clientId, String type, double montant, String date) {
        this.id = id;
        this.clientId = clientId;
        this.type = type;
        this.montant = montant;
        this.date = date;
    }

    // Getters
    public int getId() { return id; }
    public int getClientId() { return clientId; }
    public String getType() { return type; }
    public double getMontant() { return montant; }
    public String getDate() { return date; }

    // Setters
    public void setClientId(int clientId) { this.clientId = clientId; }
    public void setType(String type) { this.type = type; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setDate(String date) { this.date = date; }

}
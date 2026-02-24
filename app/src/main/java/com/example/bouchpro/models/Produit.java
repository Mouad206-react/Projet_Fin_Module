package com.example.bouchpro.models;

public class Produit {
    private int id;
    private String nom;
    private double prix;
    private double stock;
    private String date;    // Nouveau
    private int respoId;    // Nouveau

    // CONSTRUCTEUR MIS À JOUR (avec 6 paramètres)
    public Produit(int id, String nom, double prix, double stock, String date, int respoId) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.stock = stock;
        this.date = date;
        this.respoId = respoId;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    public double getStock() { return stock; }
    public String getDate() { return date; }
    public int getRespoId() { return respoId; }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRespoId(int respoId) {
        this.respoId = respoId;
    }
}
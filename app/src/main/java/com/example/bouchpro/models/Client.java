package com.example.bouchpro.models;

public class Client {
    private int id;
    private String nom;
    private String telephone;

    //Constructeur
    public Client(int id, String nom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
}
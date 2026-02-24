package com.example.bouchpro.models;

public class Respo {
    private int id;
    private String nom;
    private String role;

    public Respo(int id, String nom, String role) {
        this.id = id;
        this.nom = nom;
        this.role = role;
    }
    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getRole() { return role; }
}
# 🥩 BouchPro - Gestionnaire de Boucherie Moderne

**BouchPro** est une application Android native développée en **Java** permettant de digitaliser la gestion d'une boucherie. Elle remplace le carnet papier par un système sécurisé pour le suivi des crédits clients, la gestion du personnel et l'inventaire des produits.

---

## 🚀 Fonctionnalités Clés

### 👤 Gestion des Clients & Crédits
- **Suivi des Dettes :** Calcul automatique du solde en temps réel (Somme Crédits - Somme Paiements).
- **Historique Visuel :** Les dettes s'affichent en **rouge** et les paiements en **vert**.
- **Recherche Instantanée :** Filtrage des clients par nom ou téléphone via une barre de recherche.
- **Sécurité :** Confirmation avant toute suppression de client ou d'employé.

### 📦 Gestion du Stock & Responsables
- **Attribution du Personnel :** Création de responsables avec rôles spécifiques (Boucher, Gérant, etc.).
- **Saisie en Direct :** Formulaire de saisie continue pour ajouter plusieurs produits rapidement.
- **Calcul Automatique :** Calcul instantané de la valeur totale de chaque produit (Quantité × Prix au KG).

### 📅 Consultation & Archivage
- **Filtres de Temps :** Consultation par Jour, Semaine ou Mois.
- **Calendrier Intégré :** Sélection d'une date précise via un calendrier pour voir l'historique ancien.

---

## 🛠 Stack Technique

- **Langage :** Java (Android SDK)
- **Base de données :** SQLite (Stockage local pour un fonctionnement sans connexion Internet)
- **Design :** Material Design avec utilisation de `CardView`, `RecyclerView`, et thèmes personnalisés.
- **Architecture :** Pattern Singleton pour la gestion de la base de données.
- **Compatibilité :** Minimum SDK 24 (Android 7.0+).

---

## 🎨 Interface Utilisateur
- **Splash Screen :** Écran de démarrage professionnel.
- **Thème "Butcher Red" :** Style harmonieux basé sur un rouge profond et un gris moderne.
- **Navigation intuitive :** Gestion des boutons de retour et des titres dynamiques dans la Toolbar.

---

## 📂 Structure du Projet

```text
com.example.bouchpro
├── activities        # Écrans (Main, Client, Stock, Respo, History, Splash)
├── adapters          # Liaison données-listes (RecyclerView Adapters)
├── database          # DatabaseHelper (Logique SQLite)
├── models            # Modèles d'objets (Client, Produit, Respo, Transaction)
└── res/layout        # Fichiers de design XML

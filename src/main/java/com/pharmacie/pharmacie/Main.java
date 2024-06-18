package com.pharmacie.pharmacie;

import com.pharmacie.pharmacie.components.Medicament;


public class Main {
    public static void main(String[] args) {



        // Ajouter des medicaments
        //Achat med1 = new Achat("001", "002","Jheim", 5);
        //med1.ajouterAchat(med1);

        Medicament m= new Medicament();
        m.stockMaj();
        m.getAllMedicaments();
    }

}

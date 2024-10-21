package main.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Cette classe implémente le comportement des boutons de la fenêtre du choix de la couleur
 * @author Stoufa
 *
 */
public class GetColorDialogBtnActionListener implements ActionListener {

    /**
     * le nom de la couleur choisie
     */
    String         couleur;
    /**
     * référence sur la fenêtre associée
     */
    GetColorDialog dialog;

    /**
     * constructeur
     * @param dialog
     * @param couleur
     */
    public GetColorDialogBtnActionListener( GetColorDialog dialog, String couleur ) {
        this.dialog = dialog;
        this.couleur = couleur;
    }

    /**
     * cette méthode contient le code à éxécuter lors d'un clique
     */
    @Override
    public void actionPerformed( ActionEvent e ) {
        dialog.selectedColor = couleur; // sauvegarder la couleur choisie
        System.out.println( "SelectedColor = " + couleur );
        dialog.dispose(); // fermer la fenêtre
    }
}

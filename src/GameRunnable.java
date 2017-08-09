package main;

import org.newdawn.slick.state.StateBasedGame;

import main.gameObjects.Jeu;

/**
 * La classe qui va contenir le code à éxécuter par le processus / Thread lancée
 * du processus principale ( lancement du jeu )
 * @author Stoufa
 *
 */
public class GameRunnable implements Runnable {
    /**
     * objet représentant le jeu
     */
    Jeu            jeu = null;
    /**
     * l'orchestrateur des états du jeu
     * avec cet objet on peut changer d'un état à un autre
     */
    StateBasedGame sbg = null;

    /**
     * constructeur
     * @param jeu
     * @param sbg
     */
    public GameRunnable( Jeu jeu, StateBasedGame sbg ) {
        this.jeu = jeu;
        this.sbg = sbg;
    }

    /**
     * le code à éxécuter par le Thread
     */
    @Override
    public void run() {
        try {
            jeu.lancer( sbg ); // lancer le jeu
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}

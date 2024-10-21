package main.states;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.GameRunnable;
import main.gameObjects.Jeu;

/**
 * l'état ( State ) du jeu
 * @author Stoufa
 */
public class GameState extends BasicGameState {
    /**
     * identificateur de l'état ( State )
     */
    public static int stateID;
    /**
     * objet représentant le jeu
     */
    Jeu               jeu = null;

    /**
     * constructeur
     * @param stateID identificateur de l'état ( state )
     */
    public GameState( int stateID ) {
        GameState.stateID = stateID;
    }

    /**
     * initialiser l'état ( state )
     */
    @Override
    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        try {
            jeu = new Jeu(); // initialisation du jeu
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e ) {
            e.printStackTrace();
        }
        // démarrage du jeu dans un processus ( Thread ) à part
        // on aura besoin de bloquer la logique du jeu jusqu'à obtenir
        // un clique sur une carte
        Thread thread = new Thread( new GameRunnable( jeu, sbg ) );
        thread.start();
    }

    /**
     * delta : temps en millisecondes passé depuis l'appel précédant de la méthode update()
     * par exemple : 2 FPS -> 2 mises à jour par seconde => delta = 500
     * chaque 500 milli-secondes update() est invoquée
     * 60 FPS => 1 second / 60 => 1000 ms / 60 = 16.666..
     */
    @Override
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        jeu.update( container ); // mettre à jour le jeu
    }

    /**
     * dessiner la logique de l'état ( state ) du jeu
     */
    @Override
    public void render( GameContainer container, StateBasedGame arg1, Graphics g ) throws SlickException {
        jeu.render( g );
    }

    /**
     * retourne l'identificateur de l'état
     * chaque état ( state ) posséde un identificateur ID unique
     * cet ID est utile dans la commutation entre les différents états du jeu
     */
    @Override
    public int getID() {
        return stateID;
    }
}

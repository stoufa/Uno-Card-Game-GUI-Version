package main.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.gameObjects.Jeu;

/**
 * @author Stoufa
 * l'état ( State ) de fin du  jeu
 */
public class GameOverState extends BasicGameState {
    /**
     * identificateur de l'état ( State )
     */
    public static int stateID;

    /**
     * constructeur
     * @param stateID identificateur de l'état ( state )
     */
    public GameOverState( int stateID ) {
        GameOverState.stateID = stateID;
    }

    /**
     * initialiser l'état ( state )
     */
    @Override
    public void init( GameContainer arg0, StateBasedGame arg1 ) throws SlickException {
        // rien à initialiser !
    }

    /**
     * mettre à jour la logique de l'état
     */
    @Override
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = container.getInput(); // utilisé pour tester l'entrée de l'utilisateur ( clavier et souris )
        if ( input.isKeyPressed( Input.KEY_ESCAPE ) | input.isKeyPressed( Input.KEY_N ) ) { // touche échap ou n
            System.exit( 0 ); // Quitter l'application
        }
        if ( input.isKeyPressed( Input.KEY_ENTER ) | input.isKeyPressed( Input.KEY_O ) ) { // touche entrée ou o
            // réinitialisation de l'état du jeu
            sbg.getState( GameState.stateID ).init( container, sbg );
            // commuter vers l'état du jeu
            sbg.enterState( GameState.stateID );
        }
    }

    /**
     * dessiner la logique du menu
     */
    @Override
    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        g.setColor( Color.white );
        String message = String.format( "Fin du jeu, %s a gagné !", Jeu.joueurCourant.pseudo );
        // texte centré et au milieu de l'écran
        int x = gc.getWidth() / 2 - g.getFont().getWidth( message ) / 2, y = gc.getHeight() / 2;
        g.drawString( message, x, y );
        message = "Jouer encore ? [O/N]";
        x = gc.getWidth() / 2 - g.getFont().getWidth( message ) / 2;
        g.drawString( message, x, y + 20 );
    }

    /**
     * retourne l'identificateur de l'état
     */
    @Override
    public int getID() {
        return stateID;
    }
}
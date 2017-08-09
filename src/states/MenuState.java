package main.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * l'état du menu affiché au début du jeu
 * @author Stoufa
 *
 */
public class MenuState extends BasicGameState {
    /**
     * liste des options
     */
    private String[]  menuOptions = new String[] {
            "Jouer",
            "Quitter"
    };
    /**
     * permet de savoir quelle option on a séléctionné ( initialement la premiére )
     */
    private int       index       = 0;
    /**
     * identificateur de l'état ( State )
     */
    public static int stateID;

    /**
     * constructeur
     * @param stateID identificateur de l'état ( state )
     */
    public MenuState( int stateID ) {
        MenuState.stateID = stateID;
    }

    /**
     * initialiser l'état du menu
     */
    @Override
    public void init( GameContainer arg0, StateBasedGame arg1 ) throws SlickException {
        // rien à initialiser !
    }

    @Override
    /**
     * mettre à jour la logique de l'état
     */
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = container.getInput(); // utilisé pour tester l'entrée de l'utilisateur ( clavier et souris )
        if ( input.isKeyPressed( Input.KEY_UP ) ) { // si la touche Fléche-Haut est appuiyé
            index--;
            if ( index == -1 ) { // si on a débordé le tableau du haut ...
                index = menuOptions.length - 1; // ... on recommance du bas
            }
        } else if ( input.isKeyPressed( Input.KEY_DOWN ) ) { // si la touche Fléche-Bas est appuiyé
            index++;
            if ( index == menuOptions.length ) { // si on a débordé le tableau du bas ...
                index = 0; // ... on recommance du haut
            }
        } else if ( input.isKeyPressed( Input.KEY_ENTER ) ) { // si la touche Entrée est appuiyé
            if ( index == 0 ) { // premiére option : Jouer
                // On entre l'état ( State ) du jeu avec des transitions
                sbg.enterState( GameState.stateID, new FadeOutTransition(), new FadeInTransition() );
            } else if ( index == 1 ) { // 2eme option : Quitter
                // Quitter l'application
                System.exit( 0 );
            }
        }
    }

    /**
     * dessiner la logique du menu
     */
    @Override
    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        for ( int i = 0; i < menuOptions.length; ++i ) { // On parcourt toutes les options
            if ( i == index ) { // l'option séléctionnée ...
                g.setColor( Color.red ); // ... est colorée en rouge
            } else { // les autres
                g.setColor( Color.white ); // sont colorées en blanc
            }
            int step = gc.getHeight() / ( menuOptions.length + 1 ); // l'espace entre les différents options
            /*
             * ----------
             * <step>
             * option1
             * <step>
             * option2
             * <step>
             * ----------
             * On doit donc diviser la hauteur de la fenêtre par (le nombre d'options + 1)
             */
            // afficher l'option centré
            g.drawString(
                    menuOptions[i],
                    gc.getWidth() / 2 - g.getFont().getWidth( menuOptions[i] ) / 2,
                    step * ( i + 1 ) );
        }
    }

    /**
     * retourne l'identificateur de l'état
     */
    @Override
    public int getID() {
        return stateID;
    }
}

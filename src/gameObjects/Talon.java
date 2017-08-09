package main.gameObjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.common.Config;

/**
 * classe représentant le talon du jeu
 * @author Stoufa
 *
 */
public class Talon extends Pile {

    /**
     * constructeur
     * @param pioche : la pioche du jeu dont on doit prendre une carte pour quelle soit la 1ere carte du talon
     */
    public Talon( Pioche pioche ) {
        ajouterPremiereCarte( pioche );
    }

    /**
     * permet d'ajouter la premiére carte ( au début du jeu )
     * @param pioche : la pioche d'où on veut tirer la carte
     */
    private void ajouterPremiereCarte( Pioche pioche ) {
        empiler( pioche.premiereCarteTalon() );
        /*
        while (true) {
        	// Retirer une carte
        	Carte carte = pioche.depiler();
        	if (carte instanceof CarteSpecial) {	// C'est une carte spéciale
        		// Il faut dans ce cas la rajouter aléatoirement dans la pioche
        		pioche.retournerCarte(carte);
        		//System.out.println("Oops carte spécial , ...");
        		//System.out.println(carte);
        	} else {
        		this.empiler(carte);
        		return;
        	}
        }
        */
    }

    /**
     * retourne une chaîne décrivant le talon
     */
    @Override
    public String toString() {
        String str = "";
        if ( cartes.isEmpty() ) {
            str = "[VIDE]";
        }
        for ( int i = 0; i < cartes.size(); ++i ) {
            Carte carte = cartes.get( i );
            str = str + i + ") " + carte.toString();
            if ( i != cartes.size() - 1 ) { // si ce n'est pas la dernière itèration
                str = str + "\n"; // ajouter un retour chariot
            }
        }
        return str;
    }

    /**
     * permet d'afficher le talon
     */
    public void afficher() {
        System.out.println( this );
    }

    /**
     * permet d'afficher la carte au sommet du talon
     */
    public void afficherSommet() {
        System.out.println( "Sommet talon : " + sommet().toString() );
    }

    /**
     * la méthode responsable d'afficher le talon sur l'écran
     * @param g
     * @throws SlickException 
     */
    public void render( Graphics g ) throws SlickException {
        Carte carteSommet = this.sommet();

        Image image = this.sommet().image;
        image.setRotation( 0 );

        carteSommet.x = Game.WIDTH / 2 - Carte.WIDTH / 2 - Integer.parseInt( Config.get( "offsetPiocheTalon" ) );
        carteSommet.y = Game.HEIGHT / 2 - Carte.HEIGHT / 2;
        carteSommet.angle = 0;
        carteSommet.updateBounds();

        // Pour voir à peu prés combien y en a de cartes
        float yVal = carteSommet.y;
        for ( int i = 0; i < cartes.size(); ++i ) {
            g.drawImage( image, carteSommet.x, yVal );
            if ( i % 5 == 0 ) {
                yVal -= 2;
            }
        }

        // showing number of cards
        int offset = 20;
        String str = String.valueOf( this.cartes.size() );
        g.drawString( str,
                // centered over the last card
                Game.WIDTH / 2 - Integer.parseInt( Config.get( "offsetPiocheTalon" ) )
                        - g.getFont().getWidth( str ) / 2,
                // a little bit above the last card
                yVal - offset // pour que ça soit prés de la carte du sommet de la pile
        //Game.HEIGHT / 2 - Carte.HEIGHT / 2 - offset
        );
    }

    //	On n'a pas besoin d'implémenter la méthode update() pour le talon, le click
    //	sur le talon n'a pas un effet spécial !
    //	public void update(GameContainer container) throws SlickException {
    //		Input input = container.getInput();
    //		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {	// click detected			
    //			int mx = input.getMouseX(), my = input.getMouseY();
    //			Carte carte = this.sommet();
    //			if (carte.isClicked(new Point(mx, my))) {
    //				Debug.log("talon, update()");
    //				Audio.playSound("clickSound");
    //			}
    //		}
    //	}

}

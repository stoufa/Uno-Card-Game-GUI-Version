package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.common.Config;
import main.gfx.Sprite;

/**
 * classe représentant la pioche du jeu
 * @author Stoufa
 *
 */
public class Pioche extends Pile {
    // TODO : on doit traiter le cas où la pioche devient vide et on doit prendre une carte
    // dans ce cas, on a besoin de mélanger le talon ( sauf le sommet ) et le déposer dans la pioche

    /**
     * constructeur : permet de construire la pioche et d'y insérer toutes les cartes nécéssaires
     * @throws SlickException 
     */
    public Pioche() throws SlickException {
        for ( Couleur couleur : Couleur.values() ) {
            if ( couleur == Couleur.NOIR ) {
                for ( int i = 0; i < 4; i++ ) { // la pioche 4 cartes joker et 4 cartes +4
                    ajouter( new CarteSpecial( Couleur.NOIR, Symbole.JOKER ) );
                    ajouter( new CarteSpecial( Couleur.NOIR, Symbole.PLUS4 ) );
                }
                continue; // toutes les cartes noirs sont ajoutées, on passe à la couleur suivante
            }
            // 1 Carte 0 pour chaque couleur
            ajouter( new CarteChiffre( couleur, 0 ) );
            // 2 Cartes 1..9 pour chaque couleur
            for ( int i = 1; i <= 9; i++ ) {
                ajouter( new CarteChiffre( couleur, i ) );
                ajouter( new CarteChiffre( couleur, i ) );
            }
            // 2 Cartes passer, inverser, +2 pour chaque couleur
            for ( int i = 0; i < 2; i++ ) {
                ajouter( new CarteSpecial( couleur, Symbole.PASSER ) );
                ajouter( new CarteSpecial( couleur, Symbole.INVERSER ) );
                ajouter( new CarteSpecial( couleur, Symbole.PLUS2 ) );
            }
        }

        //afficher();	// test
    }

    /**
     * Cette méthode est utilisée pour retourner une carte aléatoirement dans la pioche
     * dans le cas où la premiére carte est une carte spéciale ( au début du jeu )
     * @param carte
     */
    private void retournerCarte( Carte carte ) {
        int i = rand.nextInt( cartes.size() ); // Entier aléatoire entre 0 et cartes.size() - 1
        cartes.add( i, carte );
    }

    /**
     * cette méthode est appelée par le talon pour qu'elle lui retourne sa premiére carte
     * la carte ne doit pas être spéciale
     * @return
     */
    public Carte premiereCarteTalon() {
        Carte carte;
        while ( true ) {
            carte = depiler(); // Retirer une carte
            //Debug.log(carte.toString());
            if ( carte instanceof CarteSpecial ) { // C'est une carte spéciale
                // Il faut dans ce cas la rajouter aléatoirement dans la pioche
                retournerCarte( carte );
                //System.out.println("Oops carte spécial , ...");
                //System.out.println(carte);
            } else {
                return carte;
            }
        }
    }

    /**
     * la méthode responsable d'afficher la pioche sur l'écran
     * @param g
     * @throws SlickException 
     */
    public void render( Graphics g ) throws SlickException {
        //g.drawString("testPioche", Game.WIDTH / 2, Game.HEIGHT / 2);
        Carte carteSommet = this.sommet();
        if ( carteSommet == null ) {
            return; // wait untill the draw card is populated again
        }

        Image image = Sprite.getHiddenCard();
        image.setRotation( 0 );

        carteSommet.x = Game.WIDTH / 2 - Carte.WIDTH / 2 + Integer.parseInt( Config.get( "offsetPiocheTalon" ) );
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
                Game.WIDTH / 2 + Integer.parseInt( Config.get( "offsetPiocheTalon" ) )
                        - g.getFont().getWidth( str ) / 2,
                // a little bit above the last card
                yVal - offset // pour que ça soit prés de la carte du sommet de la pile
        //Game.HEIGHT / 2 - Carte.HEIGHT / 2 - offset
        );
    }

    public void update( GameContainer container ) throws SlickException {
        //Debug.log("pioche bounds : " + this.sommet().bounds);

        //Input input = container.getInput();
        //		if (Jeu.input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {	// click detected			
        //			int mx = Jeu.input.getMouseX(), my = Jeu.input.getMouseY();
        //			Carte carte = this.sommet();
        //			//carte.updateBounds();
        //			Debug.log("pioche bounds : " + carte.bounds);
        //			if (carte.isClicked(new Point(mx, my))) {
        //				Debug.log("pioche, update()");
        //				Audio.playSound("clickSound");
        //			}
        //		}
    }

    // test
    //	public void afficher() {
    //		for(Carte carte : cartes) {
    //			System.out.println(carte);
    //		}
    //	}

}

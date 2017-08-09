package main.gfx;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.common.Config;
import main.common.Debug;
import main.gameObjects.Couleur;
import main.gameObjects.Symbole;

/**
 * classe utilisée pour charger et retourner les sprites / images du jeu
 * @author Stoufa
 *
 */
public class Sprite {
    /**
     * inactive : la sprite / image grise transparente à ajouter au dessus des cartes non jouables !
     * hidden : la sprite / image de la carte cachée
     * séparés de la liste des sprites, car elles sont les plus utilisées ( optimisation )
     */
    //public static Image inactive = null, hidden = null;
    /**
     * liste des sprites
     */
    public static HashMap<String, Image> sprites = new HashMap<>();

    /**
     * retourne la sprite de la carte ayant le symbole passé en paramétre
     * @param symbole	symbole de la carte dont on veut afficher
     * @return	l'image de la carte demandée
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image get( Symbole symbole ) throws SlickException { // +4, Joker
        switch ( symbole ) {
        case PLUS4:
            return get( "+4" );
        case JOKER:
            return get( "wild" );
        default:
            break;
        }
        Debug.err( "Image null ! " + symbole );
        return null;
    }

    /**
     * retourne la sprite de la carte ayant le nom passé en paramétre
     * @param spriteName nom de la carte dont on veut afficher
     * @return	l'image de la carte demandée
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    private static Image get( String spriteName ) throws SlickException {
        if ( !sprites.containsKey( spriteName ) )
            Debug.err( "Image null ! " + spriteName );
        return sprites.get( spriteName );
    }

    /**
     * retourne la sprite de la carte ayant le symbole et la couleur passés en paramétres
     * @param symbole	symbole de la carte dont on veut afficher
     * @param couleur	couleur de la carte dont on veut afficher
     * @return	l'image de la carte demandée
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image get( Symbole symbole, Couleur couleur ) throws SlickException { // exemple : passer-rouge
        String nomCouleur = null;
        switch ( couleur ) {
        case ROUGE:
            nomCouleur = "red";
            break;
        case JAUNE:
            nomCouleur = "yellow";
            break;
        case VERT:
            nomCouleur = "green";
            break;
        case BLEU:
            nomCouleur = "blue";
            break;
        case NOIR:
            //
            break;
        }
        switch ( symbole ) {
        case INVERSER:
            return get( "reverse-" + nomCouleur );
        case JOKER:
            return get( symbole );
        case PASSER:
            return get( "skip-" + nomCouleur );
        case PLUS2:
            return get( "+2-" + nomCouleur );
        case PLUS4:
            return get( symbole );
        default:
            break;
        }
        return null;
    }

    /**
     * retourne la sprite de la carte ayant la valeur et la couleur passés en paramétres
     * @param valeur	valeur de la carte dont on veut afficher
     * @param couleur	couleur de la carte dont on veut afficher
     * @return	l'image de la carte demandée
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image get( int valeur, Couleur couleur ) throws SlickException { // exemple : 0-bleu
        String nomCouleur = null;
        switch ( couleur ) {
        case ROUGE:
            nomCouleur = "red";
            break;
        case JAUNE:
            nomCouleur = "yellow";
            break;
        case VERT:
            nomCouleur = "green";
            break;
        case BLEU:
            nomCouleur = "blue";
            break;
        case NOIR:
            //
            break;
        }
        return get( valeur + "-" + nomCouleur );
    }

    /**
     * retourne la sprite / image grise transparente à ajouter au dessus des cartes non jouables !
     * @return	la sprite / image grise transparente à ajouter au dessus des cartes non jouables !
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image getInactiveCard() throws SlickException {
        if ( !sprites.containsKey( "inactiveCard" ) )
            Debug.err( "getInactiveCard() null !" );
        return sprites.get( "inactiveCard" );
    }

    /**
     * retourne la sprite / image de la carte cachée
     * @return	la sprite / image de la carte cachée
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image getHiddenCard() throws SlickException {
        if ( !sprites.containsKey( "hidden" ) )
            Debug.err( "getHiddenCard() null !" );
        return sprites.get( "hidden" );
    }

    /**
     * retourne la sprite / image de l'arriére-plan du jeu ( selon la couleur passée en paramétre )
     * @param couleur	la couleur de l'arriére-plan
     * @return	la sprite / image de l'arriére-plan du jeu ( selon la couleur passée en paramétre )
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image getBackground( Couleur couleur ) throws SlickException {
        String nomCouleur = null;
        switch ( couleur ) {
        case ROUGE:
            nomCouleur = "red";
            break;
        case JAUNE:
            nomCouleur = "yellow";
            break;
        case VERT:
            nomCouleur = "green";
            break;
        case BLEU:
            nomCouleur = "blue";
            break;
        case NOIR:
            //
            break;
        }
        return get( "bg-" + nomCouleur );
    }

    /**
     * cette méthode permet de charger toutes les images du jeu
     * N.B. : TOUTES les images / sprites DOIVENT être chargés à partir du Thread principale ( GL Thread )
     * sinon on va avoir l'exception suivante : java.lang.RuntimeException: No OpenGL context found in the current thread.
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static void load() throws SlickException {
        String colorNames[] = { "blue", "green", "red", "yellow" };
        String imageFileName;
        for ( String colorName : colorNames ) {
            // cartes chiffres : 0 .. 9
            for ( int i = 0; i <= 9; i++ ) {
                imageFileName = String.format( "%d-%s", i, colorName );
                sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            }
            // cartes +2
            imageFileName = String.format( "+2-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            // cartes passer
            imageFileName = String.format( "skip-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            // cartes inverser
            imageFileName = String.format( "reverse-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            // sprites / images de l'arriére-plan
            imageFileName = String.format( "bg-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        }
        // cartes joker
        imageFileName = "wild";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        // cartes +4
        imageFileName = "+4";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        // carte cachée ( le dos d'une carte )
        imageFileName = "hidden";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        // carte non jouable ( filtre transparent gris )
        imageFileName = "inactiveCard";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
    }
}

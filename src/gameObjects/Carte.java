package main.gameObjects;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.gfx.Sprite;

/**
 * cette classe regroupe les aspets communs de toutes les cartes
 * @author Stoufa
 *
 */
public abstract class Carte extends GameObject {

    /**
     * Dimensions de la carte
     */
    public static final int WIDTH = 86, HEIGHT = 129;

    /**
     * la couleur de la carte
     * @see Couleur
     */
    public Couleur          couleur;

    /**
     * l'image / sprite de la carte
     */
    public Image            image = null;

    /**
     * carte jouable ou pas, c.-à-d. compatible avec la carte du sommet du talon ou pas
     */
    public boolean          jouable;

    /**
     * angle de rotation de l'image / sprite
     */
    public float            angle;
    /**
     * Surface clickable de la carte
     */
    public Shape            bounds;

    /**
     * constructeur
     * @param couleur : la couleur de la carte
     */
    public Carte( Couleur couleur ) {
        this.couleur = couleur;
        //this.bounds = new Rectangle();	// init bounds
    }

    /**
     * @param carte : la carte qu'on voit sur le sommet du talon
     * @return true : Si la carte courante peut être déposée sur l'objet ( carte )
     * @return false : sinon
     * abstraite carte le méchanisme de comparaison différe entre la carte chiffre et la carte spéciale
     */
    public abstract boolean compatible( Carte carte );

    /**
     * la méthode qui permet d'afficher la carte sur l'écran
     * @throws SlickException 
     */
    @Override
    public void render( Graphics g ) throws SlickException {
        //Image img = image;

        //test
        if ( image == null )
            Debug.err( this );

        //rotate(angle);
        //		bounds.transform(Transform.createRotateTransform(angle));

        this.rotate( angle ); // rotates image + update card's bounds
        //img.setRotation(angle);	//test
        image.draw( x, y );

        //		Graphics2D g2d = (Graphics2D)g;
        //		g2d.draw(bounds);

        if ( !jouable ) { // si la carte n'est pas jouable ajouter un filtre gris sur la carte
            Image inactiveCardImg = Sprite.getInactiveCard();
            inactiveCardImg.setRotation( angle );
            inactiveCardImg.draw( x, y );
        }

        //updateBounds();
    }

    public void rotate( float degree ) {
        this.angle = degree;
        image.setRotation( degree ); // rotate sprite
        updateBounds();

        /*
        int rectX = (int) x, rectY = (int) y, rectWidth = WIDTH, rectHeight = HEIGHT;
        Shape rect = (Shape) new Rectangle(rectX, rectY, rectWidth, rectHeight); //creating the rectangle you want to rotate
        //Rectangle rect2 = new Rectangle(400, 100, rectWidth, rectHeight); //creating other rectangle to check intersection
        AffineTransform transform = new AffineTransform();
        
        //        Polygon rect = new Polygon();
        //        // rectangle (x,y) ; (x+w,y) ; (x,y) ; (x,y)
        //        rect.addPoint(rectX, rectY);
        //        rect.addPoint(rectX + rectWidth, rectY);
        //        rect.addPoint(rectX + rectWidth, rectY + rectHeight);
        //        rect.addPoint(rectX, rectY + rectHeight);
        //        // rotate the rectangle
        //        Transform transform = Transform.createRotateTransform((float) Math.toRadians(degree), rectX + rectWidth / 2, rectY + rectHeight / 2);
        //        rect.transform(transform);
        
        //rotate or do other things with the rectangle (shear, translate, scale and so on)
        transform.rotate(Math.toRadians(degree), rectX + rectWidth /2, rectY + rectHeight /2); //rotating in central axis
        //rect receiving the rectangle after rotate
        //rect.transform(transform);
        
        rect = transform.createTransformedShape(rect);
        //g2d.draw(rect); //Graphics2D object drawing the rectangle on the screen
        
        this.bounds = rect;
        */
    }

    public boolean isClicked( Point2D point ) {
        return bounds.contains( point );
    }

    public void updateBounds() {
        int rectX = (int) x, rectY = (int) y, rectWidth = WIDTH, rectHeight = HEIGHT;
        Shape rect = new Rectangle( rectX, rectY, rectWidth, rectHeight ); //creating the rectangle you want to rotate
        //Rectangle rect = new Polygon();
        // rectangle (x,y) ; (x+w,y) ; (x,y) ; (x,y)
        //        rect.addPoint(rectX, rectY);
        //        rect.addPoint(rectX + rectWidth, rectY);
        //        rect.addPoint(rectX + rectWidth, rectY + rectHeight);
        //        rect.addPoint(rectX, rectY + rectHeight);
        if ( angle == 0 ) {
            this.bounds = rect;
            return;
        }
        // rotate the rectangle
        AffineTransform transform = new AffineTransform();
        //rect.transform(transform);
        //AffineTransform transform = new AffineTransform();
        transform.rotate( Math.toRadians( angle ), rectX + rectWidth / 2, rectY + rectHeight / 2 ); //rotating in central axis
        rect = transform.createTransformedShape( rect );
        this.bounds = rect;
    }

}

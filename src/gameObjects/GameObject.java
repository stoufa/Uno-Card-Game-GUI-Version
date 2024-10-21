package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Classe abstraite qui représente un objet du jeu
 * @author Stoufa
 *
 */
public abstract class GameObject {

    /**
     * positions de l'objet sur l'écran
     */
    public float x    = 0, y = 0;
    /**
     * vitesses de déplacement de l'objet
     */
    public float velX = 0, velY = 0; // velocity : speed

    /**
     * permet de mettre à jour l'état de l'objet
     * @param container
     * @throws SlickException 
     */
    public abstract void update( GameContainer container ) throws SlickException;

    /**
     * permet d'afficher l'état actuel de l'objet
     * @param g
     * @throws SlickException
     */
    public abstract void render( Graphics g ) throws SlickException;

}

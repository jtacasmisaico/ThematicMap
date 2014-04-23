package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Class to draw a geographic point in Processing.
 * @author Iain Dillingham
 */
public class Point extends Feature
{
    private float x, y;

    /**
     * Constructs a geographic point with the given record number within the given parent sketch.
     * @param recordNumber The record number.
     * @param p The parent sketch.
     */
    public Point(int recordNumber, PApplet p)
    {
        super(recordNumber, p);

        x = 0;

        y = 0;
    }

    public void draw(ThematicMap thematicMap)
    {
        PVector screen = thematicMap.geoToScreen(new PVector(x, y));

        getParent().ellipse(screen.x, screen.y, 2, 2);
    }

    public FeatureType getFeatureType()
    {
        return FeatureType.POINT;
    }

    public Rectangle2D getBounds()
    {
        return new Rectangle2D.Float(x, y, 0, 0);
    }

    public boolean contains(float x, float y)
    {
        return (this.x == x && this.y == y);
    }

    /**
     * Gets the x coordinate of the point.
     * @return The x coordinate of the point.
     */
    public float getX()
    {
        return x;
    }

    /**
     * Sets the x coordinate of the point to the given value.
     * @param x The x coordinate of the point.
     */
    public void setX(float x)
    {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the point.
     * @return The y coordinate of the point.
     */
    public float getY()
    {
        return y;
    }

    /**
     * Sets the y coordinate of the point to the given value.
     * @param y The y coordinate of the point.
     */
    public void setY(float y)
    {
        this.y = y;
    }
}

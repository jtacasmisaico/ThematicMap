package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

import processing.core.PVector;

/**
 * Class to draw a point in Processing.
 * @author Iain Dillingham
 */
public class Point extends Feature
{
    private float x, y;

    /**
     * Constructs a point with the given record number within the given thematic map.
     * @param recordNumber The record number.
     * @param thematicMap The thematic map.
     */
    public Point(int recordNumber, ThematicMap thematicMap)
    {
        super(recordNumber, thematicMap);

        x = 0;

        y = 0;
    }

    public void draw(ThematicMap thematicMap)
    {
        PVector screen = thematicMap.geoToScreen(new PVector(x, y));

        getThematicMap().getParent().ellipse(screen.x, screen.y, 2, 2);
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
     * Sets the x coordinate of the point.
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
     * Sets the y coordinate of the point.
     * @param y The y coordinate of the point.
     */
    public void setY(float y)
    {
        this.y = y;
    }
}

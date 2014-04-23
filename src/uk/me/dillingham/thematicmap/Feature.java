package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;

/**
 * Class to draw a geographic feature in Processing.
 * @author Iain Dillingham
 */
public abstract class Feature
{
    private final int recordNumber;
    private final PApplet p;

    /**
     * Constructs a geographic feature with the given record number within the given parent sketch.
     * @param recordNumber The record number.
     * @param p The parent sketch.
     */
    protected Feature(int recordNumber, PApplet p)
    {
        this.recordNumber = recordNumber;

        this.p = p;
    }

    /**
     * Gets the record number of the feature.
     * @return The record number of the feature.
     */
    public int getRecordNumber()
    {
        return recordNumber;
    }

    /**
     * Gets the parent sketch of the feature.
     * @return The parent sketch of the feature.
     */
    public PApplet getParent()
    {
        return p;
    }

    /**
     * Draws the feature in the given thematic map.
     * @param thematicMap The thematic map.
     */
    public abstract void draw(ThematicMap thematicMap);

    /**
     * Gets the type of the feature.
     * @return The type of the feature.
     */
    public abstract FeatureType getFeatureType();

    /**
     * Gets the bounds of the feature.
     * @return The bounds of the feature.
     */
    public abstract Rectangle2D getBounds();

    /**
     * Tests whether the given point is contained by the feature.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @return True if the given point is contained by the feature.
     */
    public abstract boolean contains(float x, float y);
}

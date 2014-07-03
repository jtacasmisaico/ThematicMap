package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

import processing.core.PGraphics;

/**
 * Class to draw a feature in Processing.
 * @author Iain Dillingham
 */
public abstract class Feature
{
    private final int recordNumber;
    private final ThematicMap thematicMap;

    /**
     * Constructs a feature with the given record number within the given thematic map.
     * @param recordNumber The record number.
     * @param thematicMap The thematic map.
     */
    protected Feature(int recordNumber, ThematicMap thematicMap)
    {
        this.recordNumber = recordNumber;

        this.thematicMap = thematicMap;
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
     * Gets the thematic map associated with the feature.
     * @return The thematic map associated with the feature.
     */
    public ThematicMap getThematicMap()
    {
        return thematicMap;
    }

    /**
     * Draws the feature within the associated thematic map.
     */
    public abstract void draw();

    /**
     * Draws the feature within the given graphics context.
     * @param g The graphics context.
     */
    public abstract void draw(PGraphics g);

    /**
     * Gets the type of the feature.
     * @return The type of the feature.
     */
    public abstract FeatureType getFeatureType();

    /**
     * Gets the bounds of the feature in geographic coordinates.
     * @return The bounds of the feature in geographic coordinates.
     */
    public abstract Rectangle2D getGeoBounds();

    /**
     * Tests whether the given point is contained by the feature.
     * @param x The x coordinate of the point in geographic coordinates.
     * @param y The y coordinate of the point in geographic coordinates.
     * @return True if the given point is contained by the feature.
     */
    public abstract boolean contains(float x, float y);
}

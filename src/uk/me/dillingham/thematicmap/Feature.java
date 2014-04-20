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
     * Gets the bounds of the feature in geographic coordinates.
     * @return The bounds of the feature in geographic coordinates.
     */
    public abstract Rectangle2D getGeoBounds();
}

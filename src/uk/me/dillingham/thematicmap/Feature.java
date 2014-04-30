package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

/**
 * Class to draw a feature in Processing.
 * @author Iain Dillingham
 */
public abstract class Feature
{
    private int recordNumber;
    private ThematicMap thematicMap;

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
     * Sets the thematic map associated with the feature.
     * @param thematicMap The thematic map associated with the feature.
     */
    public void setThematicMap(ThematicMap thematicMap)
    {
        this.thematicMap = thematicMap;
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

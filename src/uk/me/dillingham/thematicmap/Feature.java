package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class to draw a feature in Processing.
 * @author Iain Dillingham
 */
public abstract class Feature
{
    private final int recordNumber;
    private final Geometry geometry;
    private final ThematicMap thematicMap;

    private static final ShapeWriter SHAPE_WRITER = new ShapeWriter();

    /**
     * Constructs a feature with the given record number and geometry within the given thematic map.
     * @param recordNumber The record number.
     * @param geometry The geometry.
     * @param thematicMap The thematic map.
     */
    protected Feature(int recordNumber, Geometry geometry, ThematicMap thematicMap)
    {
        this.recordNumber = recordNumber;

        this.geometry = geometry;

        this.thematicMap = thematicMap;
    }

    /**
     * Gets the record number associated with the feature.
     * @return The record number associated with the feature.
     */
    public int getRecordNumber()
    {
        return recordNumber;
    }

    /**
     * Gets the geometry associated with the feature;
     * @return The geometry associated with the feature.
     */
    public Geometry getGeometry()
    {
        return geometry;
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
     * Gets the type of the feature.
     * @return The type of the feature.
     */
    public abstract FeatureType getFeatureType();

    /**
     * Gets the bounds of the feature in geographic coordinates.
     * @return The bounds of the feature in geographic coordinates.
     */
    public Rectangle2D getGeoBounds()
    {
        return SHAPE_WRITER.toShape(geometry).getBounds2D();
    }

    /**
     * Tests whether the given point is contained by the feature.
     * @param x The x coordinate of the point in geographic coordinates.
     * @param y The y coordinate of the point in geographic coordinates.
     * @return True if the given point is contained by the feature.
     */
    public boolean contains(float x, float y)
    {
        return geometry.contains(geometry.getFactory().createPoint(new Coordinate(x, y)));
    }

    /**
     * Draws the feature within the associated thematic map.
     */
    public abstract void draw();
}

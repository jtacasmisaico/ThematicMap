package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;

import processing.core.PGraphics;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class to draw a feature in Processing.
 * @author Iain Dillingham
 */
public abstract class Feature
{
    private final int featureIndex;
    private final Geometry geometry;
    private final ThematicMap thematicMap;

    private static final ShapeWriter SHAPE_WRITER = new ShapeWriter();

    /**
     * Constructs a feature with the given index and geometry within the given thematic map.
     * @param featureIndex The index.
     * @param geometry The geometry.
     * @param thematicMap The thematic map.
     */
    protected Feature(int featureIndex, Geometry geometry, ThematicMap thematicMap)
    {
        this.featureIndex = featureIndex;

        this.geometry = geometry;

        this.thematicMap = thematicMap;
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
     * Draws the feature.
     */
    public abstract void draw();

    /**
     * Draws the feature using the given graphics context.
     * @param g The graphics context.
     */
    public abstract void draw(PGraphics g);

    /**
     * Gets the index associated with the feature.
     * @return The index associated with the feature.
     */
    public int getFeatureIndex()
    {
        return featureIndex;
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
}

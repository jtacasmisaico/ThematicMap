package uk.me.dillingham.thematicmap;

import processing.core.PVector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class to draw a point in Processing.
 * @author Iain Dillingham
 */
public class Point extends Feature
{
    /**
     * Constructs a point with the given record number and geometry within the given thematic map.
     * @param recordNumber The record number.
     * @param geometry The geometry.
     * @param thematicMap The thematic map.
     */
    public Point(int recordNumber, Geometry geometry, ThematicMap thematicMap)
    {
        super(recordNumber, geometry, thematicMap);
    }

    public void draw()
    {
        for (int i = 0; i < getGeometry().getNumGeometries(); i++)
        {
            Coordinate geo = getGeometry().getGeometryN(i).getCoordinate();

            PVector screen = getThematicMap().geoToScreen(new PVector((float) geo.x, (float) geo.y));

            getThematicMap().getParent().ellipse(screen.x, screen.y, 2, 2);
        }
    }

    public FeatureType getFeatureType()
    {
        return FeatureType.POINT;
    }
}

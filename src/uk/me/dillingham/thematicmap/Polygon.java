package uk.me.dillingham.thematicmap;

import processing.core.PConstants;
import processing.core.PVector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class to draw a polygon in Processing.
 * @author Iain Dillingham
 */
public class Polygon extends Feature
{
    /**
     * Constructs a polygon with the given record number and geometry within the given thematic map.
     * @param recordNumber The record number.
     * @param geometry The geometry.
     * @param thematicMap The thematic map.
     */
    public Polygon(int recordNumber, Geometry geometry, ThematicMap thematicMap)
    {
        super(recordNumber, geometry, thematicMap);
    }

    public void draw()
    {
        for (int i = 0; i < getGeometry().getNumGeometries(); i++)
        {
            getThematicMap().getParent().beginShape();

            Coordinate[] coordinates = getGeometry().getGeometryN(i).getCoordinates();

            for (Coordinate coordinate : coordinates)
            {
                PVector screen = getThematicMap().geoToScreen(new PVector((float) coordinate.x, (float) coordinate.y));

                getThematicMap().getParent().vertex(screen.x, screen.y);
            }

            getThematicMap().getParent().endShape(PConstants.CLOSE);
        }
    }

    public FeatureType getFeatureType()
    {
        return FeatureType.POLYGON;
    }
}

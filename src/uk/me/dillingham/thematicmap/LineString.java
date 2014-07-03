package uk.me.dillingham.thematicmap;

import processing.core.PVector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class to draw a line string in Processing.
 * @author Iain Dillingham
 */
public class LineString extends Feature
{
    /**
     * Constructs a line string with the given record number and geometry within the given thematic map.
     * @param recordNumber The record number.
     * @param The geometry.
     * @param thematicMap The thematic map.
     */
    public LineString(int recordNumber, Geometry geometry, ThematicMap thematicMap)
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

            getThematicMap().getParent().endShape();
        }
    }

    public FeatureType getFeatureType()
    {
        return FeatureType.LINE_STRING;
    }
}

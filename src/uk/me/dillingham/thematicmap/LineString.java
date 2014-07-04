package uk.me.dillingham.thematicmap;

import processing.core.PConstants;
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
     * Constructs a line string with the given feature index and geometry within the given thematic map.
     * @param featureIndex The feature index.
     * @param geometry The geometry.
     * @param thematicMap The thematic map.
     * @throws ClassCastException If geometry is neither a LineString nor a MultiLineString.
     */
    public LineString(int featureIndex, Geometry geometry, ThematicMap thematicMap)
    {
        super(featureIndex, geometry, thematicMap);

        if (!geometry.getGeometryType().equals("LineString") && !geometry.getGeometryType().equals("MultiLineString"))
        {
            throw new ClassCastException();
        }
    }

    public void draw()
    {
        for (int i = 0; i < getGeometry().getNumGeometries(); i++)
        {
            getThematicMap().getParent().beginShape(PConstants.LINES);

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

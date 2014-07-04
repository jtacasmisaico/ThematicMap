package uk.me.dillingham.thematicmap;

import processing.core.PGraphics;
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
     * Constructs a point with the given feature index and geometry within the given thematic map.
     * @param featureIndex The feature index.
     * @param geometry The geometry.
     * @param thematicMap The thematic map.
     * @throws ClassCastException If geometry is neither a Point nor a MultiPoint.
     */
    public Point(int featureIndex, Geometry geometry, ThematicMap thematicMap)
    {
        super(featureIndex, geometry, thematicMap);

        if (!geometry.getGeometryType().equals("Point") && !geometry.getGeometryType().equals("MultiPoint"))
        {
            throw new ClassCastException();
        }
    }

    public void draw()
    {
        draw(getThematicMap().getGraphics());
    }

    public void draw(PGraphics g)
    {
        for (int i = 0; i < getGeometry().getNumGeometries(); i++)
        {
            Coordinate geo = getGeometry().getGeometryN(i).getCoordinate();

            PVector screen = getThematicMap().geoToScreen(new PVector((float) geo.x, (float) geo.y));

            g.ellipse(screen.x, screen.y, 2, 2);
        }
    }

    public FeatureType getFeatureType()
    {
        return FeatureType.POINT;
    }
}

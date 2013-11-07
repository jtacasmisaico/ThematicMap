package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import uk.me.dillingham.thematicmap.projections.Projection;

public class FeatureCollection implements Feature, PConstants
{
    private List<Feature> features;

    public FeatureCollection()
    {
        this(new ArrayList<Feature>());
    }

    public FeatureCollection(List<Feature> features)
    {
        this.features = features;
    }

    public void draw(PApplet p, Projection projection)
    {
        for (Feature feature : features)
        {
            feature.draw(p, projection);
        }
    }

    public Rectangle2D getBounds()
    {
        float xMin = MAX_FLOAT;
        float yMin = MAX_FLOAT;
        float xMax = MIN_FLOAT;
        float yMax = MIN_FLOAT;

        for (Feature feature : features)
        {
            Rectangle2D bounds = feature.getBounds();

            xMin = PApplet.min(xMin, (float) bounds.getMinX());
            yMin = PApplet.min(yMin, (float) bounds.getMinY());
            xMax = PApplet.max(xMax, (float) bounds.getMaxX());
            yMax = PApplet.max(yMax, (float) bounds.getMaxY());
        }

        return new Rectangle2D.Float(xMin, yMin, xMax - xMin, yMax - yMin);
    }
}

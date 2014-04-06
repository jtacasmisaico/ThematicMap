package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import uk.me.dillingham.thematicmap.ThematicMap;

public class Polygon extends Feature
{
    private Path2D.Float path;

    public Polygon(int recordNumber, PApplet p)
    {
        super(recordNumber, p);

        path = new Path2D.Float();
    }

    public void addPart(float[] x, float[] y)
    {
        if (x.length == y.length)
        {
            path.moveTo(x[0], y[0]);

            for (int i = 1; i < x.length; i++)
            {
                path.lineTo(x[i], y[i]);
            }

            path.closePath();
        }
        else
        {
            System.err.println("Cannot add part to polygon " + getRecordNumber());
        }
    }

    public void draw(ThematicMap thematicMap)
    {
        PathIterator iterator = path.getPathIterator(null);

        float[] geo = new float[6];

        while (!iterator.isDone())
        {
            int segmentType = iterator.currentSegment(geo);

            if (segmentType == PathIterator.SEG_MOVETO)
            {
                getParent().beginShape();
            }

            if (segmentType == PathIterator.SEG_MOVETO || segmentType == PathIterator.SEG_LINETO)
            {
                PVector screen = thematicMap.geoToScreen(new PVector(geo[0], geo[1]));

                getParent().vertex(screen.x, screen.y);
            }

            if (segmentType == PathIterator.SEG_CLOSE)
            {
                getParent().endShape(PConstants.CLOSE);
            }

            iterator.next();
        }
    }

    public Rectangle2D getGeoBounds()
    {
        return path.getBounds2D();
    }
}

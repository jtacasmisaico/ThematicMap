package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import uk.me.dillingham.thematicmap.projections.Projection;

public class Polygon implements Feature
{
    private int recNumber;
    private Path2D.Float path;

    public Polygon(int recNumber)
    {
        this.recNumber = recNumber;

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
            System.err.println("Cannot add part to polygon " + recNumber);
        }
    }

    public void draw(PApplet p, Projection projection)
    {
        PathIterator iterator = path.getPathIterator(null);

        float[] geo = new float[6];

        while (!iterator.isDone())
        {
            int segmentType = iterator.currentSegment(geo);

            if (segmentType == PathIterator.SEG_MOVETO)
            {
                p.beginShape();
            }

            if (segmentType == PathIterator.SEG_MOVETO || segmentType == PathIterator.SEG_LINETO)
            {
                PVector screen = projection.geoToScreen(new PVector(geo[0], geo[1]));

                p.vertex(screen.x, screen.y);
            }

            if (segmentType == PathIterator.SEG_CLOSE)
            {
                p.endShape(PConstants.CLOSE);
            }

            iterator.next();
        }
    }

    public Rectangle2D getBounds()
    {
        return path.getBounds2D();
    }
}

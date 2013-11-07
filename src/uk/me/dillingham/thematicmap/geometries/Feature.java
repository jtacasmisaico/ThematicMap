package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import uk.me.dillingham.thematicmap.projections.Projection;

public interface Feature
{
    public void draw(PApplet p, Projection projection);

    public Rectangle2D getBounds();
}

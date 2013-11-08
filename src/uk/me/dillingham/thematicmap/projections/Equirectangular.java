package uk.me.dillingham.thematicmap.projections;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PVector;

public class Equirectangular extends Projection
{
    public Equirectangular()
    {
        super();

        setGeoBounds(new Rectangle2D.Float(-180, -90, 360, 180));

        setScreenBounds(new Rectangle2D.Float(0, 0, 360, 180));
    }

    public PVector geoToScreen(PVector geo)
    {
        float screenX = PApplet.map(geo.x, (float) getGeoBounds().getMinX(), (float) getGeoBounds().getMaxX(),
                (float) getScreenBounds().getMinX(), (float) getScreenBounds().getMaxX());

        float screenY = PApplet.map(geo.y, (float) getGeoBounds().getMinY(), (float) getGeoBounds().getMaxY(),
                (float) getScreenBounds().getMaxY(), (float) getScreenBounds().getMinY());

        return new PVector(screenX, screenY);
    }

    public PVector screenToGeo(PVector screen)
    {
        float geoX = PApplet.map(screen.x, (float) getScreenBounds().getMinX(), (float) getScreenBounds().getMaxX(),
                (float) getGeoBounds().getMinX(), (float) getGeoBounds().getMaxX());

        float geoY = PApplet.map(screen.y, (float) getScreenBounds().getMinY(), (float) getScreenBounds().getMaxY(),
                (float) getGeoBounds().getMaxY(), (float) getGeoBounds().getMinY());

        return new PVector(geoX, geoY);
    }
}

package uk.me.dillingham.thematicmap.projections;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PVector;

public class Equirectangular implements Projection
{
    private Rectangle2D geoBounds, screenBounds;

    public Equirectangular()
    {
        geoBounds = new Rectangle2D.Float(-180, -90, 360, 180);

        screenBounds = new Rectangle2D.Float(0, 0, 360, 180);
    }

    public PVector geoToScreen(PVector geo)
    {
        float screenX = PApplet.map(geo.x, (float) geoBounds.getMinX(), (float) geoBounds.getMaxX(),
                (float) screenBounds.getMinX(), (float) screenBounds.getMaxX());

        float screenY = PApplet.map(geo.y, (float) geoBounds.getMinY(), (float) geoBounds.getMaxY(),
                (float) screenBounds.getMaxY(), (float) screenBounds.getMinY());

        return new PVector(screenX, screenY);
    }

    public PVector screenToGeo(PVector screen)
    {
        float geoX = PApplet.map(screen.x, (float) screenBounds.getMinX(), (float) screenBounds.getMaxX(),
                (float) geoBounds.getMinX(), (float) geoBounds.getMaxX());

        float geoY = PApplet.map(screen.y, (float) screenBounds.getMinY(), (float) screenBounds.getMaxY(),
                (float) geoBounds.getMaxY(), (float) geoBounds.getMinY());

        return new PVector(geoX, geoY);
    }

    public Rectangle2D getGeoBounds()
    {
        return geoBounds;
    }

    public void setGeoBounds(Rectangle2D geoBounds)
    {
        this.geoBounds = geoBounds;
    }

    public Rectangle2D getScreenBounds()
    {
        return screenBounds;
    }

    public void setScreenBounds(Rectangle2D screenBounds)
    {
        this.screenBounds = screenBounds;
    }
}

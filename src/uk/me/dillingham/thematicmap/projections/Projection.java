package uk.me.dillingham.thematicmap.projections;

import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public abstract class Projection
{
    private Rectangle2D geoBounds, screenBounds;

    protected Projection()
    {
        geoBounds = new Rectangle2D.Float();

        screenBounds = new Rectangle2D.Float();
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

    public int getScreenWidth()
    {
        return (int) screenBounds.getWidth();
    }

    public int getScreenHeight()
    {
        return (int) screenBounds.getHeight();
    }

    public abstract PVector geoToScreen(PVector geo);

    public abstract PVector screenToGeo(PVector screen);
}

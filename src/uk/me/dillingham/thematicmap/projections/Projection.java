package uk.me.dillingham.thematicmap.projections;

import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public interface Projection
{
    public PVector geoToScreen(PVector geo);

    public PVector screenToGeo(PVector screen);

    public Rectangle2D getGeoBounds();

    public void setGeoBounds(Rectangle2D geoBounds);

    public Rectangle2D getScreenBounds();

    public void setScreenBounds(Rectangle2D screenBounds);
}

package uk.me.dillingham.thematicmap.components;

import processing.core.PApplet;
import uk.me.dillingham.thematicmap.projections.Projection;

public interface Drawable
{
    public void draw(PApplet p, Projection projection, float x, float y);
}

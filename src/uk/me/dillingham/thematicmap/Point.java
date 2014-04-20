package uk.me.dillingham.thematicmap;

import processing.core.PApplet;
import processing.core.PVector;

public class Point extends Feature
{
    private float x, y;

    public Point(int recordNumber, PApplet p)
    {
        super(recordNumber, p);

        x = 0;

        y = 0;
    }

    public void draw(ThematicMap thematicMap)
    {
        PVector screen = thematicMap.geoToScreen(new PVector(x, y));

        getParent().ellipse(screen.x, screen.y, 2, 2);
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}

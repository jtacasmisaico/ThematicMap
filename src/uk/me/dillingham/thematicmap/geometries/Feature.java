package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import uk.me.dillingham.thematicmap.ThematicMap;

public abstract class Feature
{
    private final int recordNumber;
    private final PApplet p;

    protected Feature(int recordNumber, PApplet p)
    {
        this.recordNumber = recordNumber;

        this.p = p;
    }

    public int getRecordNumber()
    {
        return recordNumber;
    }

    public PApplet getParent()
    {
        return p;
    }

    public abstract void draw(ThematicMap thematicMap);

    public abstract Rectangle2D getGeoBounds();
}

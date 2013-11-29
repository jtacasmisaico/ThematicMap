package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Rectangle2D;

import uk.me.dillingham.thematicmap.components.Drawable;

public abstract class Feature implements Drawable
{
    private final int recordNumber;

    protected Feature(int recordNumber)
    {
        this.recordNumber = recordNumber;
    }

    public int getRecordNumber()
    {
        return recordNumber;
    }

    public abstract Rectangle2D getGeoBounds();
}

package uk.me.dillingham.thematicmap.geometries;

import java.awt.geom.Rectangle2D;

import uk.me.dillingham.thematicmap.components.Drawable;
import uk.me.dillingham.thematicmap.components.Record;

public abstract class Feature extends Record implements Drawable
{
    private Record attributes;

    protected Feature(int recordNumber)
    {
        super(recordNumber);

        attributes = null;
    }

    public Record getAttributes()
    {
        return attributes;
    }

    public void setAttributes(Record attributes)
    {
        this.attributes = attributes;
    }

    public abstract Rectangle2D getGeoBounds();
}

package uk.me.dillingham.thematicmap;

import java.io.File;

import processing.core.PApplet;
import processing.core.PVector;
import uk.me.dillingham.thematicmap.geometries.FeatureCollection;
import uk.me.dillingham.thematicmap.io.ShpFileReader;
import uk.me.dillingham.thematicmap.projections.Projection;

public class ThematicMap
{
    private PApplet p;
    private Projection projection;
    private FeatureCollection featureCollection;

    public ThematicMap(PApplet p, Projection projection)
    {
        this.p = p;

        this.projection = projection;

        featureCollection = new FeatureCollection();
    }

    public PVector geoToScreen(PVector geo)
    {
        return projection.geoToScreen(geo);
    }

    public PVector screenToGeo(PVector screen)
    {
        return projection.screenToGeo(screen);
    }

    public void read(String file)
    {
        file = "data" + File.separator + file;

        featureCollection = ShpFileReader.read(new File(file));
    }

    public void draw()
    {
        featureCollection.draw(p, projection);
    }
}

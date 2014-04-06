package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.Table;
import uk.me.dillingham.thematicmap.io.ShpFileReader;

public class ThematicMap
{
    private List<Feature> features;
    private Table attributeTable;
    private Rectangle2D geoBounds, screenBounds;
    private PApplet p;

    public ThematicMap(PApplet p)
    {
        this(0, 0, p.width, p.height, p);
    }

    public ThematicMap(float x, float y, float width, float height, PApplet p)
    {
        features = new ArrayList<Feature>();

        attributeTable = new Table();

        geoBounds = null;

        screenBounds = new Rectangle2D.Float(x, y, width, height);

        this.p = p;
    }

    public void read(String file)
    {
        File shpFile = new File("data" + File.separator + file + ".shp");

        if (shpFile.exists())
        {
            try
            {
                features = ShpFileReader.read(shpFile, p);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        File csvFile = new File("data" + File.separator + file + ".csv");

        if (csvFile.exists())
        {
            try
            {
                attributeTable = new Table(csvFile, "header");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (geoBounds == null)
        {
            geoBounds = features.get(0).getGeoBounds();

            for (Feature feature : features)
            {
                geoBounds.add(feature.getGeoBounds());
            }
        }
    }

    public void draw()
    {
        for (Feature feature : features)
        {
            feature.draw(this);
        }
    }

    public void draw(int recordNumber)
    {
        features.get(recordNumber).draw(this);
    }

    public void draw(String value, int column)
    {
        int recordNumber = attributeTable.findRowIndex(value, column);

        draw(recordNumber);
    }

    public Collection<Feature> getFeatures()
    {
        return new ArrayList<Feature>(features);
    }

    public Feature getFeature(int recordNumber)
    {
        return features.get(recordNumber);
    }

    public Feature getFeature(String value, int column)
    {
        int recordNumber = attributeTable.findRowIndex(value, column);

        return getFeature(recordNumber);
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

    public Table getAttributeTable()
    {
        return attributeTable;
    }

    public void setAttributeTable(Table attributeTable)
    {
        this.attributeTable = attributeTable;
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

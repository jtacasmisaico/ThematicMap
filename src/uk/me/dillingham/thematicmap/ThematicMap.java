package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import processing.core.PApplet;
import processing.data.Table;
import uk.me.dillingham.thematicmap.components.Drawable;
import uk.me.dillingham.thematicmap.geometries.Feature;
import uk.me.dillingham.thematicmap.io.ShpFileReader;
import uk.me.dillingham.thematicmap.projections.Projection;

public class ThematicMap implements Drawable
{
    private List<Feature> features;
    private Table attributeTable;

    public ThematicMap()
    {
        features = new ArrayList<Feature>();

        attributeTable = new Table();
    }

    public void read(String file)
    {
        File shpFile = new File("data" + File.separator + file);

        if (shpFile.exists())
        {
            try
            {
                features = ShpFileReader.read(shpFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        File csvFile = new File("data" + File.separator + file.replaceFirst(".shp", ".csv"));

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
    }

    public void draw(PApplet p, Projection projection, float x, float y)
    {
        for (Feature feature : features)
        {
            feature.draw(p, projection, x, y);
        }
    }

    public Feature getFeature(int recordNumber)
    {
        return features.get(recordNumber);
    }

    public Feature getFeature(String value, int column)
    {
        int recordNumber = attributeTable.findRowIndex(value, column);

        return features.get(recordNumber);
    }

    public Collection<Feature> getFeatures()
    {
        return new ArrayList<Feature>(features);
    }

    public Table getAttributeTable()
    {
        return attributeTable;
    }

    public Rectangle2D getGeoBounds()
    {
        Rectangle2D geoBounds = features.get(0).getGeoBounds();

        for (Feature feature : features)
        {
            geoBounds.add(feature.getGeoBounds());
        }

        return geoBounds;
    }
}

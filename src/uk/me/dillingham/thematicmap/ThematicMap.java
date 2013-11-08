package uk.me.dillingham.thematicmap;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import processing.core.PApplet;
import uk.me.dillingham.thematicmap.components.Drawable;
import uk.me.dillingham.thematicmap.geometries.Feature;
import uk.me.dillingham.thematicmap.io.ShpFileReader;
import uk.me.dillingham.thematicmap.projections.Projection;

public class ThematicMap implements Drawable
{
    private Map<Integer, Feature> featureByRecordNumber;

    public ThematicMap()
    {
        featureByRecordNumber = new LinkedHashMap<Integer, Feature>();
    }

    public void read(String file)
    {
        file = "data" + File.separator + file;

        for (Feature feature : ShpFileReader.read(new File(file)))
        {
            featureByRecordNumber.put(feature.getRecordNumber(), feature);
        }
    }

    public void draw(PApplet p, Projection projection, float x, float y)
    {
        for (Feature feature : featureByRecordNumber.values())
        {
            feature.draw(p, projection, x, y);
        }
    }

    public Feature getFeature(int recordNumber)
    {
        return featureByRecordNumber.get(recordNumber);
    }

    public Collection<Feature> getFeatures()
    {
        return featureByRecordNumber.values();
    }
}

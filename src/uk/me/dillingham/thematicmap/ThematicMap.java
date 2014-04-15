package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.Table;
import uk.me.dillingham.thematicmap.io.ShpFileReader;

/**
 * Class for drawing thematic maps in Processing.
 * @author Iain Dillingham
 */
public class ThematicMap
{
    private List<Feature> features;
    private Table attributeTable;
    private Rectangle2D geoBounds, screenBounds;
    private PApplet p;

    /**
     * Constructs a thematic map within the given parent sketch.
     * @param p The parent sketch.
     */
    public ThematicMap(PApplet p)
    {
        features = new ArrayList<Feature>();

        attributeTable = new Table();

        geoBounds = null;

        screenBounds = null;

        this.p = p;
    }

    /**
     * Reads geometry and attributes from the given shapefile. A shapefile consists of a main file (.shp), an index file
     * (.shx), and dBASE table (.dbf).
     * @param shapefile The name of the shapefile, which should be specified without the extension.
     */
    public void read(String shapefile)
    {
        File shpFile = new File("data" + File.separator + shapefile + ".shp");

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

        File csvFile = new File("data" + File.separator + shapefile + ".csv");

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

        if (screenBounds == null)
        {
            screenBounds = new Rectangle2D.Float(0, 0, p.width, p.height);
        }
    }

    /**
     * Draws the thematic map.
     */
    public void draw()
    {
        for (Feature feature : features)
        {
            feature.draw(this);
        }
    }

    /**
     * Draws the feature with the given record number. Record numbers are zero-indexed.
     * @param recordNumber The record number. Record numbers are zero-indexed.
     */
    public void draw(int recordNumber)
    {
        features.get(recordNumber).draw(this);
    }

    /**
     * Gets the record number of the feature located at the given screen point. This method will return -1 if no feature
     * is located at the given screen point.
     * @param screenPoint The screen point.
     * @return The record number or -1.
     */
    public int getRecordNumber(PVector screenPoint)
    {
        PVector geoPoint = screenToGeo(screenPoint);

        for (Feature feature : features)
        {
            if (feature.getClass() == Polygon.class) // TODO: Add points and lines
            {
                Polygon polygon = (Polygon) feature;

                if (polygon.contains(geoPoint.x, geoPoint.y))
                {
                    return polygon.getRecordNumber();
                }
            }
        }

        return -1;
    }

    /**
     * Gets the screen point that corresponds to the given geographic point. This method will return a screen point even
     * when the geographic point lies outside the geographic bounds of the thematic map.
     * @see getGeoBounds()
     * @param geoPoint The geographic point.
     * @return The screen point.
     */
    public PVector geoToScreen(PVector geoPoint)
    {
        float screenX = PApplet.map(geoPoint.x, (float) geoBounds.getMinX(), (float) geoBounds.getMaxX(),
                (float) screenBounds.getMinX(), (float) screenBounds.getMaxX());

        float screenY = PApplet.map(geoPoint.y, (float) geoBounds.getMinY(), (float) geoBounds.getMaxY(),
                (float) screenBounds.getMaxY(), (float) screenBounds.getMinY());

        return new PVector(screenX, screenY);
    }

    /**
     * Gets the geographic point that corresponds to the given screen point. This method will return a geographic point
     * even when the screen point lies outside the screen bounds of the thematic map.
     * @see getScreenBounds()
     * @param screenPoint The screen point.
     * @return The geographic point.
     */
    public PVector screenToGeo(PVector screenPoint)
    {
        float geoX = PApplet.map(screenPoint.x, (float) screenBounds.getMinX(), (float) screenBounds.getMaxX(),
                (float) geoBounds.getMinX(), (float) geoBounds.getMaxX());

        float geoY = PApplet.map(screenPoint.y, (float) screenBounds.getMinY(), (float) screenBounds.getMaxY(),
                (float) geoBounds.getMaxY(), (float) geoBounds.getMinY());

        return new PVector(geoX, geoY);
    }

    /**
     * Gets the attribute table of the thematic map.
     * @return The attribute table of the thematic map.
     */
    public Table getAttributeTable()
    {
        return attributeTable;
    }

    /**
     * Sets the attribute table of the thematic map to the given attribute table.
     * @param attributeTable The attribute table.
     */
    public void setAttributeTable(Table attributeTable)
    {
        this.attributeTable = attributeTable;
    }

    /**
     * Gets the bounds of the thematic map in geographic coordinates. This method will return null if called before
     * either {@link #read(String)} or {@link #setGeoBounds(Rectangle2D)}.
     * @return The bounds of the thematic map in geographic coordinates or null.
     */
    public Rectangle2D getGeoBounds()
    {
        return geoBounds;
    }

    /**
     * Sets the bounds of the thematic map in geographic coordinates.
     * @param geoBounds The bounds of the thematic map in geographic coordinates.
     */
    public void setGeoBounds(Rectangle2D geoBounds)
    {
        this.geoBounds = geoBounds;
    }

    /**
     * Gets the bounds of the thematic map in screen coordinates. This method will return null if called before either
     * {@link #read(String)} or {@link #setScreenBounds(Rectangle2D)}.
     * @return The bounds of the thematic map in screen coordinates or null.
     */
    public Rectangle2D getScreenBounds()
    {
        return screenBounds;
    }

    /**
     * Sets the bounds of the thematic map in screen coordinates.
     * @param screenBounds The bounds of the thematic map in screen coordinates.
     */
    public void setScreenBounds(Rectangle2D screenBounds)
    {
        this.screenBounds = screenBounds;
    }
}

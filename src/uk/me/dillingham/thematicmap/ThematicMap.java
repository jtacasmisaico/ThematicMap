package uk.me.dillingham.thematicmap;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.Table;
import uk.me.dillingham.thematicmap.io.ShpFile;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Class to draw a thematic map in Processing.
 * @author Iain Dillingham
 */
public class ThematicMap
{
    private GeometryFactory geometryFactory;
    private List<Geometry> geometries;
    private Table attributeTable;
    private Rectangle2D geoBounds, screenBounds;
    private PApplet p;

    /**
     * Constructs a thematic map within the given parent sketch.
     * @param p The parent sketch.
     */
    public ThematicMap(PApplet p)
    {
        geometryFactory = new GeometryFactory();

        geometries = new ArrayList<Geometry>();

        attributeTable = new Table();

        geoBounds = null; // TODO

        screenBounds = null; // TODO

        this.p = p;
    }

    /**
     * Reads geometry and attributes from the given shapefile. A shapefile consists of a main file (.shp), an index file
     * (.shx), and dBASE table (.dbf).
     * @param shapefile The shapefile.
     */
    public void read(String shapefile)
    {
        String[] tokens = PApplet.split(shapefile, '.');

        // Read geometry

        String shpFileName = tokens[0] + ".shp";

        InputStream shpFileInputStream = p.createInput(shpFileName);

        if (shpFileInputStream == null)
        {
            System.err.println("Error: Cannot read " + shpFileName);
        }
        else
        {
            try
            {
                ShpFile shpFile = new ShpFile(geometryFactory);

                shpFile.read(shpFileInputStream);

                geometries = shpFile.getGeometries();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // Read attributes

        String csvFileName = tokens[0] + ".csv";

        InputStream csvFileInputStream = p.createInput(csvFileName);

        if (csvFileInputStream == null)
        {
            System.err.println("Error: Cannot read " + csvFileName);
        }
        else
        {
            try
            {
                attributeTable = new Table(csvFileInputStream, "csv, header");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Draws the thematic map. If the bounds of the thematic map in geographic coordinates and in screen coordinates
     * have not been set by {@link #setGeoBounds(Rectangle2D)} and {@link #setScreenBounds(Rectangle2D)}, the geographic
     * bounds will be those of the features read from the shapefile and the screen bounds will be those of the parent
     * sketch.
     */
    public void draw()
    {
        draw(p.g);
    }

    /**
     * Draws the thematic map using the given graphics context. If the bounds of the thematic map in geographic
     * coordinates and in screen coordinates have not been set by {@link #setGeoBounds(Rectangle2D)} and
     * {@link #setScreenBounds(Rectangle2D)}, the geographic bounds will be those of the features read from the
     * shapefile and the screen bounds will be those of the parent sketch.
     * @param g The graphics context.
     */
    public void draw(PGraphics g)
    {
        for (int i = 0; i < geometries.size(); i++)
        {
            draw(i, g);
        }
    }

    /**
     * Draws the feature with the given index. Features are zero-indexed. If the bounds of the thematic map in
     * geographic coordinates and in screen coordinates have not been set by {@link #setGeoBounds(Rectangle2D)} and
     * {@link #setScreenBounds(Rectangle2D)}, the geographic bounds will be those of the features read from the
     * shapefile and the screen bounds will be those of the parent sketch.
     * @param featureIndex The index of the feature. Features are zero-indexed.
     */
    public void draw(int featureIndex)
    {
        draw(featureIndex, p.g);
    }

    /**
     * Draws the feature with the given index using the given graphics context. Features are zero-indexed. If the bounds
     * of the thematic map in geographic coordinates and in screen coordinates have not been set by
     * {@link #setGeoBounds(Rectangle2D)} and {@link #setScreenBounds(Rectangle2D)}, the geographic bounds will be those
     * of the features read from the shapefile and the screen bounds will be those of the parent sketch.
     * @param featureIndex The index of the feature. Features are zero-indexed.
     * @param g The graphics context.
     */
    public void draw(int featureIndex, PGraphics g)
    {
        Geometry geometry = geometries.get(featureIndex);

        if (geometry.getGeometryType().equals("Point") || geometry.getGeometryType().equals("MultiPoint"))
        {
            drawPoint(geometry, g);
        }

        if (geometry.getGeometryType().equals("LineString") || geometry.getGeometryType().equals("MultiLineString"))
        {
            drawLineString(geometry, g);
        }

        if (geometry.getGeometryType().equals("Polygon") || geometry.getGeometryType().equals("MultiPolygon"))
        {
            drawPolygon(geometry, g);
        }
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

    /**
     * Gets the attribute table.
     * @return The attribute table.
     */
    public Table getAttributeTable()
    {
        return attributeTable;
    }

    /**
     * Gets the index of the feature located at the given screen point. This method will return -1 if no feature is
     * located at the given screen point. If more than one feature is located at the given screen point, this method
     * will return the index of the first feature located at the given screen point.
     * @param x The x coordinate of the point in screen coordinates.
     * @param y The y coordinate of the point in screen coordinates.
     * @return The index of the feature located at the given screen point or -1.
     */
    public int getFeatureIndex(float x, float y)
    {
        PVector geoPoint = screenToGeo(new PVector(x, y));

        Geometry geometry = geometryFactory.createPoint(new Coordinate(geoPoint.x, geoPoint.y));

        for (int i = 0; i < geometries.size(); i++)
        {
            if (geometries.get(i).contains(geometry))
            {
                return i;
            }
        }

        return -1;
    }

    private void drawPoint(Geometry geometry, PGraphics g)
    {
        for (int i = 0; i < geometry.getNumGeometries(); i++)
        {
            Coordinate geoPoint = geometry.getGeometryN(i).getCoordinate();

            PVector screenPoint = geoToScreen(new PVector((float) geoPoint.x, (float) geoPoint.y));

            g.ellipse(screenPoint.x, screenPoint.y, 2, 2);
        }
    }

    private void drawLineString(Geometry geometry, PGraphics g)
    {
        for (int i = 0; i < geometry.getNumGeometries(); i++)
        {
            g.beginShape(PConstants.LINES);

            Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();

            for (Coordinate coordinate : coordinates)
            {
                PVector screen = geoToScreen(new PVector((float) coordinate.x, (float) coordinate.y));

                g.vertex(screen.x, screen.y);
            }

            g.endShape();
        }
    }

    private void drawPolygon(Geometry geometry, PGraphics g)
    {
        for (int i = 0; i < geometry.getNumGeometries(); i++)
        {
            g.beginShape(PConstants.POLYGON);

            Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();

            for (Coordinate coordinate : coordinates)
            {
                PVector screen = geoToScreen(new PVector((float) coordinate.x, (float) coordinate.y));

                g.vertex(screen.x, screen.y);
            }

            g.endShape(PConstants.CLOSE);
        }
    }
}

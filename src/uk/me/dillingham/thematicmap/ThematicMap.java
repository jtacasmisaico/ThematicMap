package uk.me.dillingham.thematicmap;

import java.awt.geom.Point2D;
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

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.geom.util.NoninvertibleTransformationException;

/**
 * Class to draw a thematic map in Processing.
 * @author Iain Dillingham
 */
public class ThematicMap
{
    private GeometryFactory geometryFactory;
    private List<Geometry> geometries;
    private Table attributeTable;
    private Rectangle2D window, geoBounds, screenBounds;
    private AffineTransformation geoToScreen, screenToGeo;
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

        window = new Rectangle2D.Float(0, 0, p.width, p.height);

        geoBounds = new Rectangle2D.Float(-180, -90, 360, 180);

        setScreenBounds();

        setTransformations(geoBounds, screenBounds);

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
     * Loads geometries from the given list of geometries.
     * @param geometries The list of geometries.
     */
    public void load(List<Geometry> geometries)
    {
        this.geometries = geometries;
    }

    /**
     * Draws the thematic map. If the bounds of the thematic map in geographic coordinates have not been set by
     * {@link #setGeoBounds(Rectangle2D)} the geographic bounds will be (-180, -90, 360, 180) (x, y, width, height).
     */
    public void draw()
    {
        draw(p.g);
    }

    /**
     * Draws the thematic map using the given graphics context. If the bounds of the thematic map in geographic
     * coordinates have not been set by {@link #setGeoBounds(Rectangle2D)} the geographic bounds will be (-180, -90,
     * 360, 180) (x, y, width, height).
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
     * geographic coordinates have not been set by {@link #setGeoBounds(Rectangle2D)} the geographic bounds will be
     * (-180, -90, 360, 180) (x, y, width, height).
     * @param featureIndex The index of the feature. Features are zero-indexed.
     */
    public void draw(int featureIndex)
    {
        draw(featureIndex, p.g);
    }

    /**
     * Draws the feature with the given index using the given graphics context. Features are zero-indexed. If the bounds
     * of the thematic map in geographic coordinates have not been set by {@link #setGeoBounds(Rectangle2D)} the
     * geographic bounds will be (-180, -90, 360, 180) (x, y, width, height).
     * @param featureIndex The index of the feature. Features are zero-indexed.
     * @param g The graphics context.
     */
    public void draw(int featureIndex, PGraphics g)
    {
        draw(geometries.get(featureIndex), g);
    }

    private void draw(Geometry geometry, PGraphics g)
    {
        if (geometry.getGeometryType().equals("Point") || geometry.getGeometryType().equals("MultiPoint"))
        {
            for (int i = 0; i < geometry.getNumGeometries(); i++)
            {
                Coordinate screen = geoToScreen.transform(geometry.getGeometryN(i).getCoordinate(), new Coordinate());

                g.ellipse((float) screen.x, (float) screen.y, 2, 2);
            }
        }

        if (geometry.getGeometryType().equals("LineString") || geometry.getGeometryType().equals("MultiLineString"))
        {
            for (int i = 0; i < geometry.getNumGeometries(); i++)
            {
                g.beginShape(PConstants.LINES);

                Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();

                for (Coordinate coordinate : coordinates)
                {
                    Coordinate screen = geoToScreen.transform(coordinate, new Coordinate());

                    g.vertex((float) screen.x, (float) screen.y);
                }

                g.endShape();
            }
        }

        if (geometry.getGeometryType().equals("Polygon") || geometry.getGeometryType().equals("MultiPolygon"))
        {
            for (int i = 0; i < geometry.getNumGeometries(); i++)
            {
                g.beginShape(PConstants.POLYGON);

                Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();

                for (Coordinate coordinate : coordinates)
                {
                    Coordinate screen = geoToScreen.transform(coordinate, new Coordinate());

                    g.vertex((float) screen.x, (float) screen.y);
                }

                g.endShape(PConstants.CLOSE);
            }
        }
    }

    private void setScreenBounds()
    {
        double scaleX = geoBounds.getWidth() / window.getWidth();

        double scaleY = geoBounds.getHeight() / window.getHeight();

        double scale = Math.max(scaleX, scaleY);

        float screenX = (float) window.getX();

        float screenY = (float) window.getY();

        float screenWidth = (float) (geoBounds.getWidth() / scale);

        float screenHeight = (float) (geoBounds.getHeight() / scale);

        screenBounds = new Rectangle2D.Float(screenX, screenY, screenWidth, screenHeight);
    }

    private void setTransformations(Rectangle2D geoBounds, Rectangle2D screenBounds)
    {
        try
        {
            // Unlike AWT AffineTransform, JTS AffineTransformation operations are specified in the correct order.

            geoToScreen = new AffineTransformation();

            geoToScreen.translate(-geoBounds.getX(), -geoBounds.getY());

            double scaleX = screenBounds.getWidth() / geoBounds.getWidth();

            double scaleY = screenBounds.getHeight() / geoBounds.getHeight();

            geoToScreen.scale(scaleX, scaleY);

            geoToScreen.reflect(1, 0);

            geoToScreen.translate(screenBounds.getX(), screenBounds.getY() + screenBounds.getHeight());

            screenToGeo = geoToScreen.getInverse();
        }
        catch (NoninvertibleTransformationException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets the screen point that corresponds to the given geographic point. This method will return a screen point even
     * when the geographic point lies outside the geographic bounds of the thematic map.
     * @see #getGeoBounds()
     * @param geoPoint The geographic point.
     * @return The screen point.
     */
    public PVector geoToScreen(PVector geoPoint)
    {
        Coordinate coordinate = geoToScreen(new Coordinate(geoPoint.x, geoPoint.y));

        return new PVector((float) coordinate.x, (float) coordinate.y);
    }

    /**
     * Gets the screen point that corresponds to the given geographic point. This method will return a screen point even
     * when the geographic point lies outside the geographic bounds of the thematic map.
     * @see #getGeoBounds()
     * @param geoPoint The geographic point.
     * @return The screen point.
     */
    public Point2D geoToScreen(Point2D geoPoint)
    {
        Coordinate coordinate = geoToScreen(new Coordinate(geoPoint.getX(), geoPoint.getY()));

        return new Point2D.Double(coordinate.x, coordinate.y);
    }

    private Coordinate geoToScreen(Coordinate coordinate)
    {
        return geoToScreen.transform(coordinate, new Coordinate());
    }

    /**
     * Gets the geographic point that corresponds to the given screen point. This method will return a geographic point
     * even when the screen point lies outside the screen bounds of the thematic map.
     * @see #getScreenBounds()
     * @param screenPoint The screen point.
     * @return The geographic point.
     */
    public PVector screenToGeo(PVector screenPoint)
    {
        Coordinate coordinate = screenToGeo(new Coordinate(screenPoint.x, screenPoint.y));

        return new PVector((float) coordinate.x, (float) coordinate.y);
    }

    /**
     * Gets the geographic point that corresponds to the given screen point. This method will return a geographic point
     * even when the screen point lies outside the screen bounds of the thematic map.
     * @see #getScreenBounds()
     * @param screenPoint The screen point.
     * @return The geographic point.
     */
    public Point2D screenToGeo(Point2D screenPoint)
    {
        Coordinate coordinate = screenToGeo(new Coordinate(screenPoint.getX(), screenPoint.getY()));

        return new Point2D.Double(coordinate.x, coordinate.y);
    }

    private Coordinate screenToGeo(Coordinate coordinate)
    {
        return screenToGeo.transform(coordinate, new Coordinate());
    }

    /**
     * Gets the bounds of the thematic map in geographic coordinates.
     * @return The bounds of the thematic map in geographic coordinates.
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

        setScreenBounds();

        setTransformations(this.geoBounds, this.screenBounds);
    }

    /**
     * Gets the bounds of the thematic map in screen coordinates.
     * @return The bounds of the thematic map in screen coordinates.
     */
    public Rectangle2D getScreenBounds()
    {
        return screenBounds;
    }

    /**
     * Gets the window of the thematic map.
     * @return The window of the thematic map.
     */
    public Rectangle2D getWindow()
    {
        return window;
    }

    /**
     * Sets the window of the thematic map to the given window.
     * @param window The window of the thematic map.
     */
    public void setWindow(Rectangle2D window)
    {
        this.window = window;

        setScreenBounds();

        setTransformations(this.geoBounds, this.screenBounds);
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

    /**
     * Gets the number of features contained within the thematic map.
     * @return The number of features contained within the thematic map.
     */
    public int getNumFeatures()
    {
        return geometries.size();
    }

    /**
     * Gets the geometry factory associated with the thematic map.
     * @return The geometry factory associated with the thematic map.
     */
    public GeometryFactory getGeometryFactory()
    {
        return geometryFactory;
    }

    /**
     * Gets the <a href="http://en.wikipedia.org/wiki/Minimum_bounding_rectangle" target="_blank">Minimum Bounding
     * Rectangle</a> (MBR) of the features contained within the thematic map.
     * @return The MBR of the features contained within the thematic map.
     */
    public Rectangle2D getMBR()
    {
        Geometry geometry = geometryFactory.buildGeometry(geometries).convexHull();

        ShapeWriter shapeWriter = new ShapeWriter();

        return shapeWriter.toShape(geometry).getBounds2D();
    }

    /**
     * Gets the centroids of the features contained within the thematic map.
     * @return The centroids of the features contained within the thematic map.
     */
    public List<Point2D> getCentroids()
    {
        List<Point2D> centroids = new ArrayList<Point2D>(geometries.size());

        for (Geometry geometry : geometries)
        {
            Point point = geometry.getCentroid();

            centroids.add(new Point2D.Double(point.getX(), point.getY()));
        }

        return centroids;
    }

    /**
     * Gets the features that are adjacent to the given feature.
     * @param featureIndex The feature. Features are zero-indexed.
     * @return The features that are adjacent to the given feature. Features are zero-indexed.
     */
    public int[] getAdjacentFeatures(int featureIndex)
    {
        List<Integer> adjacencies = new ArrayList<Integer>();

        Geometry source = geometries.get(featureIndex);

        for (int i = 0; i < geometries.size(); i++)
        {
            Geometry target = geometries.get(i);

            if (source != target && source.intersects(target))
            {
                adjacencies.add(i);
            }
        }

        return toArray(adjacencies);
    }

    private int[] toArray(List<Integer> list)
    {
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++)
        {
            array[i] = list.get(i);
        }

        return array;
    }

    /**
     * Gets the geometry of the feature with the given index. Features are zero-indexed.
     * @param featureIndex The index of the feature. Features are zero-indexed.
     * @return The geometry of the feature with the given index.
     */
    public Geometry getGeometry(int featureIndex)
    {
        return geometries.get(featureIndex);
    }

    /**
     * Gets the geometries contained within the thematic map.
     * @return The geometries contained within the thematic map.
     */
    public List<Geometry> getGeometries()
    {
        return geometries;
    }

    /**
     * Checks the validity of the geometries contained within the thematic map.
     */
    public void checkValidity()
    {
        for (int i = 0; i < geometries.size(); i++)
        {
            Geometry geometry = geometries.get(i);

            String isValid = geometry.isValid() ? "valid" : "invalid";

            String message = "Feature " + i + " (" + geometry.getGeometryType() + ") is " + isValid;

            System.out.println(message);
        }
    }
}

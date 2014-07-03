package uk.me.dillingham.thematicmap.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class ShpFile
{
    private GeometryFactory geometryFactory;

    private List<Geometry> geometries;

    private final Comparator<LinearRing> AREA_ORDER = new Comparator<LinearRing>()
    {
        public int compare(LinearRing linearRing1, LinearRing linearRing2)
        {
            Polygon polygon1 = geometryFactory.createPolygon(linearRing1);

            Polygon polygon2 = geometryFactory.createPolygon(linearRing2);

            double result = polygon1.getArea() - polygon2.getArea();

            if (result > 0) return +1;

            if (result < 0) return -1;

            return 0;
        }
    };

    public ShpFile()
    {
        geometryFactory = new GeometryFactory();

        geometries = new ArrayList<Geometry>();
    }

    public void read(InputStream inputStream) throws IOException
    {
        // File header

        ByteBuffer fileHeader = ByteBuffer.allocate(100);

        inputStream.read(fileHeader.array());

        fileHeader.order(ByteOrder.BIG_ENDIAN);

        int fileLength = fileHeader.getInt(24); // Length of file (bytes)

        fileHeader.order(ByteOrder.LITTLE_ENDIAN);

        int shapeType = fileHeader.getInt(32);

        checkShapeType(shapeType);

        // Record

        int currentPosition = 100; // Current position in file (bytes)

        while (currentPosition < fileLength)
        {
            Geometry geometry = null;

            // Record header

            ByteBuffer recordHeader = ByteBuffer.allocate(8);

            inputStream.read(recordHeader.array());

            recordHeader.order(ByteOrder.BIG_ENDIAN);

            int contentLength = recordHeader.getInt(4); // Length of content (16-bit words)

            // Record content

            ByteBuffer recordContent = ByteBuffer.allocate(contentLength * 2); // Why?

            inputStream.read(recordContent.array());

            recordContent.order(ByteOrder.LITTLE_ENDIAN);

            // Shapefile Point

            if (shapeType == 1)
            {
                geometry = readShapefilePoint(recordContent);
            }

            // Shapefile PolyLine

            if (shapeType == 3)
            {
                geometry = readShapefilePolyLine(recordContent);
            }

            // Shapefile Polygon

            if (shapeType == 5)
            {
                geometry = readShapefilePolygon(recordContent);
            }

            geometries.add(geometry);

            currentPosition += 4 + contentLength;
        }

        inputStream.close();
    }

    public GeometryFactory getGeometryFactory()
    {
        return geometryFactory;
    }

    public void setGeometryFactory(GeometryFactory geometryFactory)
    {
        this.geometryFactory = geometryFactory;
    }

    public List<Geometry> getGeometries()
    {
        return geometries;
    }

    private void checkShapeType(int shapeType)
    {
        String message = "Warning: Cannot read ";

        switch (shapeType)
        {
            case 8:
                System.err.println(message + "MultiPoint");
                break;

            case 11:
                System.err.println(message + "PointZ");
                break;

            case 13:
                System.err.println(message + "PolyLineZ");
                break;

            case 15:
                System.err.println(message + "PolygonZ");
                break;

            case 18:
                System.err.println(message + "MultiPointZ");
                break;

            case 21:
                System.err.println(message + "PointM");
                break;

            case 23:
                System.err.println(message + "PolyLineM");
                break;

            case 25:
                System.err.println(message + "PolygonM");
                break;

            case 28:
                System.err.println(message + "MultiPointM");
                break;

            case 31:
                System.err.println(message + "MultiPatch");
                break;
        }
    }

    private Geometry readShapefilePoint(ByteBuffer byteBuffer)
    {
        double x = byteBuffer.getDouble(4);

        double y = byteBuffer.getDouble(12);

        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    private Geometry readShapefilePolyLine(ByteBuffer byteBuffer)
    {
        // TODO

        return null;
    }

    private Geometry readShapefilePolygon(ByteBuffer byteBuffer)
    {
        int numParts = byteBuffer.getInt(36);

        int numPoints = byteBuffer.getInt(40);

        byteBuffer.position(44);

        int[] parts = new int[numParts]; // Index to first point in part

        for (int i = 0; i < numParts; i++)
        {
            parts[i] = byteBuffer.getInt();
        }

        Coordinate[] points = new Coordinate[numPoints];

        for (int i = 0; i < numPoints; i++)
        {
            double x = byteBuffer.getDouble();

            double y = byteBuffer.getDouble();

            points[i] = new Coordinate(x, y);
        }

        List<LinearRing> interiorParts = new ArrayList<LinearRing>();

        List<LinearRing> exteriorParts = new ArrayList<LinearRing>();

        for (int i = 0; i < numParts; i++)
        {
            int from = parts[i];

            int to = i + 1 < numParts ? parts[i + 1] : numPoints;

            Coordinate[] partPoints = Arrays.copyOfRange(points, from, to);

            if (CGAlgorithms.isCCW(partPoints))
            {
                interiorParts.add(geometryFactory.createLinearRing(partPoints));
            }
            else
            {
                exteriorParts.add(geometryFactory.createLinearRing(partPoints));
            }
        }

        // Ensures interior parts are paired with the smallest possible exterior parts. Think of a polygon with a hole
        // inside the hole of another polygon with a hole.
        Collections.sort(exteriorParts, AREA_ORDER);

        List<Polygon> polygons = new ArrayList<Polygon>(exteriorParts.size());

        for (LinearRing exteriorPart : exteriorParts)
        {
            List<LinearRing> interior = new ArrayList<LinearRing>();

            for (Iterator<LinearRing> it = interiorParts.iterator(); it.hasNext();)
            {
                LinearRing interiorPart = it.next();

                if (exteriorPart.contains(interiorPart))
                {
                    interior.add(interiorPart);

                    it.remove();
                }
            }

            Polygon polygon;

            if (interior.isEmpty())
            {
                polygon = geometryFactory.createPolygon(exteriorPart);
            }
            else
            {
                polygon = geometryFactory.createPolygon(exteriorPart, (LinearRing[]) interior.toArray());
            }

            polygons.add(polygon);
        }

        return geometryFactory.buildGeometry(polygons);
    }

    public static void main(String[] args)
    {
        PApplet p = new PApplet();

        String filename = "./data/ne_110m_admin_0_countries_lakes/ne_110m_admin_0_countries_lakes.shp";

        InputStream inputStream = p.createInput(filename);

        try
        {
            ShpFile shpFile = new ShpFile();

            shpFile.read(inputStream);

            for (Geometry geometry : shpFile.getGeometries())
            {
                System.out.println(geometry.getGeometryType());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

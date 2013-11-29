package uk.me.dillingham.thematicmap.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.me.dillingham.thematicmap.geometries.Feature;
import uk.me.dillingham.thematicmap.geometries.Polygon;

public class ShpFileReader
{
    private ShpFileReader()
    {
        throw new AssertionError();
    }

    public static List<Feature> read(File shpFile) throws IOException
    {
        List<Feature> features = new ArrayList<Feature>();

        InputStream shp = new FileInputStream(shpFile);

        // File header

        ByteBuffer fileHeader = ByteBuffer.allocate(100);

        shp.read(fileHeader.array());

        int fileLength = fileHeader.getInt(24);

        // Record

        int position = 100;

        while (position < fileLength)
        {
            // Record header

            ByteBuffer recHeader = ByteBuffer.allocate(8);

            shp.read(recHeader.array());

            int recNumber = recHeader.getInt(); // Starts at 1

            int recLength = recHeader.getInt();

            // Record content

            ByteBuffer recContent = ByteBuffer.allocate(recLength * 2);

            shp.read(recContent.array());

            recContent.order(ByteOrder.LITTLE_ENDIAN);

            int shapeType = recContent.getInt();

            if (shapeType == 5)
            {
                // Polygon

                float xMin = (float) recContent.getDouble();
                float yMin = (float) recContent.getDouble();
                float xMax = (float) recContent.getDouble();
                float yMax = (float) recContent.getDouble();

                int numParts = recContent.getInt();

                int numPoints = recContent.getInt();

                int[] parts = new int[numParts];

                float[] x = new float[numPoints];
                float[] y = new float[numPoints];

                for (int i = 0; i < numParts; i++)
                {
                    parts[i] = recContent.getInt();
                }

                for (int i = 0; i < numPoints; i++)
                {
                    x[i] = (float) recContent.getDouble();
                    y[i] = (float) recContent.getDouble();
                }

                Polygon polygon = new Polygon(recNumber - 1); // Starts at 0

                for (int i = 0; i < numParts; i++)
                {
                    int from = parts[i];

                    int to;

                    if (i + 1 < numParts)
                    {
                        to = parts[i + 1];
                    }
                    else
                    {
                        to = numPoints;
                    }

                    float[] x0 = Arrays.copyOfRange(x, from, to);
                    float[] y0 = Arrays.copyOfRange(y, from, to);

                    polygon.addPart(x0, y0);
                }

                features.add(polygon);
            }

            // Increment position

            position += 4 + recLength;
        }

        shp.close();

        return features;
    }
}

package uk.me.dillingham.thematicmap.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import processing.core.PApplet;

public class DbfFile
{
    public DbfFile()
    {

    }

    public void read(InputStream inputStream) throws IOException
    {
        // File header

        ByteBuffer fileHeader = ByteBuffer.allocate(67);

        inputStream.read(fileHeader.array());

        fileHeader.order(ByteOrder.LITTLE_ENDIAN);

        int numRecords = fileHeader.getInt(4);

        short numBytesHeader = fileHeader.getShort(8);

        short numBytesRecord = fileHeader.getShort(10);

        inputStream.close();
    }

    public static void main(String[] args)
    {
        try
        {
            PApplet p = new PApplet();

            DbfFile dbfFile = new DbfFile();

            dbfFile.read(p.createInput("data/ne_110m_admin_0_countries_lakes/ne_110m_admin_0_countries_lakes.dbf"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

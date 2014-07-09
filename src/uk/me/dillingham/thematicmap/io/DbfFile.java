package uk.me.dillingham.thematicmap.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

import processing.core.PApplet;

public class DbfFile
{
    private Charset charset;

    public DbfFile()
    {
        charset = Charset.forName("US-ASCII");
    }

    public void read(InputStream inputStream) throws IOException
    {
        // http://ulisse.elettra.trieste.it/services/doc/dbase/DBFstruct.htm#C1

        // File header

        ByteBuffer fileHeader = ByteBuffer.allocate(32);

        inputStream.read(fileHeader.array());

        fileHeader.order(ByteOrder.LITTLE_ENDIAN);

        int numRecords = fileHeader.getInt(4);

        short numHeaderBytes = fileHeader.getShort(8);

        short numRecordBytes = fileHeader.getShort(10);

        // Field descriptors

        for (int currentHeaderByte = 32; currentHeaderByte < numHeaderBytes - 1; currentHeaderByte += 32)
        {
            ByteBuffer fieldDescriptor = ByteBuffer.allocate(32);

            inputStream.read(fieldDescriptor.array());

            fieldDescriptor.order(ByteOrder.LITTLE_ENDIAN);

            String fieldName = new String(Arrays.copyOfRange(fieldDescriptor.array(), 0, 10), charset);

            char fieldType = fieldDescriptor.getChar(11);

            System.out.println(fieldType + " " + fieldName);
        }

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

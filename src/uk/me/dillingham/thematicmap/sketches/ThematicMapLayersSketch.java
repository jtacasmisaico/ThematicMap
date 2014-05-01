package uk.me.dillingham.thematicmap.sketches;

import java.awt.geom.Rectangle2D;
import java.io.File;

import processing.core.PApplet;
import uk.me.dillingham.thematicmap.ThematicMap;

@SuppressWarnings("serial")
public class ThematicMapLayersSketch extends PApplet
{
    ThematicMap countriesLakes, populatedPlaces;

    public void setup()
    {
        size(740, 380);

        Rectangle2D.Float geoBounds = new Rectangle2D.Float(-180, -90, 360, 180);

        Rectangle2D.Float screenBounds = new Rectangle2D.Float(10, 10, 720, 360);

        // Initialise map

        countriesLakes = new ThematicMap(this);

        countriesLakes.setGeoBounds(geoBounds);

        countriesLakes.setScreenBounds(screenBounds);

        countriesLakes.read("ne_110m_admin_0_countries_lakes" + File.separator + "ne_110m_admin_0_countries_lakes");

        // Initialise map

        populatedPlaces = new ThematicMap(this);

        populatedPlaces.setGeoBounds(geoBounds);

        populatedPlaces.setScreenBounds(screenBounds);

        populatedPlaces.read("ne_110m_populated_places" + File.separator + "ne_110m_populated_places");

        textAlign(LEFT, TOP);

        textFont(createFont("SourceSansPro-Regular", 12));
    }

    public void draw()
    {
        background(255);

        // Draw map

        fill(224);

        stroke(255);

        strokeWeight(0.5f);

        countriesLakes.draw();

        // Draw map

        fill(222, 45, 38);

        noStroke();

        populatedPlaces.draw();

        // Draw border

        noFill();

        stroke(128);

        Rectangle2D screenBounds = countriesLakes.getScreenBounds();

        float minX = (float) screenBounds.getMinX();
        float minY = (float) screenBounds.getMinY();

        rect(minX, minY, (float) screenBounds.getWidth(), (float) screenBounds.getHeight()); // Border

        // Draw text

        if (screenBounds.contains(mouseX, mouseY))
        {
            int recordNumber = countriesLakes.getRecordNumber(mouseX, mouseY);

            if (recordNumber > -1)
            {
                noStroke();

                fill(128);

                text(countriesLakes.getAttributeTable().getString(recordNumber, 3), minX + 2, minY);
            }
        }

        noLoop();
    }

    public void mouseMoved()
    {
        loop();
    }

    public static void main(String[] args)
    {
        PApplet.main("uk.me.dillingham.thematicmap.sketches.ThematicMapLayersSketch");
    }
}

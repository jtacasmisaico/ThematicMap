package uk.me.dillingham.thematicmap.sketches;

import java.awt.geom.Rectangle2D;

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

        countriesLakes.read("ne_110m_admin_0_countries_lakes/ne_110m_admin_0_countries_lakes.shp");

        // Initialise map

        populatedPlaces = new ThematicMap(this);

        populatedPlaces.setGeoBounds(geoBounds);

        populatedPlaces.setScreenBounds(screenBounds);

        populatedPlaces.read("ne_110m_populated_places/ne_110m_populated_places.shp");

        textAlign(LEFT, TOP);

        textFont(createFont("SourceSansPro-Regular", 12));
    }

    public void draw()
    {
        background(255);

        // Draw map

        fill(240);

        stroke(189);

        strokeWeight(0.5f);

        countriesLakes.draw();

        fill(222, 45, 38);

        noStroke();

        populatedPlaces.draw();

        // Draw border

        noFill();

        stroke(99);

        Rectangle2D screenBounds = countriesLakes.getScreenBounds();

        float minX = (float) screenBounds.getMinX();
        float minY = (float) screenBounds.getMinY();

        rect(minX, minY, (float) screenBounds.getWidth(), (float) screenBounds.getHeight()); // Border

        // Draw text

        if (screenBounds.contains(mouseX, mouseY))
        {
            int featureIndex = countriesLakes.getFeatureIndex(mouseX, mouseY);

            if (featureIndex > -1)
            {
                noStroke();

                fill(99);

                text(countriesLakes.getAttributeTable().getString(featureIndex, 3), minX + 2, minY);
            }
        }

        noLoop();
    }

    public void keyPressed()
    {
        if (key == 's')
        {
            save("img/ThematicMapLayersSketch.png");
        }
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

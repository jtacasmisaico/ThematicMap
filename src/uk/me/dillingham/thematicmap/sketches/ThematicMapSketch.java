package uk.me.dillingham.thematicmap.sketches;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PVector;
import uk.me.dillingham.thematicmap.ThematicMap;

@SuppressWarnings("serial")
public class ThematicMapSketch extends PApplet
{
    ThematicMap thematicMap;

    public void setup()
    {
        size(740, 380);

        thematicMap = new ThematicMap(this);

        thematicMap.read("ne_110m_admin_0_countries_lakes/ne_110m_admin_0_countries_lakes.shp");

        thematicMap.setGeoBounds(new Rectangle2D.Float(-180, -90, 360, 180));

        thematicMap.setScreenBounds(new Rectangle2D.Float(10, 10, 720, 360));

        textAlign(LEFT, TOP);

        textFont(createFont("SourceSansPro-Regular", 12));

        println(thematicMap.getAttributeTable().getString(168, 3)); // USA
    }

    public void draw()
    {
        background(255);

        // Draw map

        fill(224);

        stroke(255);

        strokeWeight(0.5f);

        thematicMap.draw();

        // Draw highlighted feature

        fill(128);

        thematicMap.draw(168); // USA

        // Draw border

        noFill();

        stroke(128);

        Rectangle2D screenBounds = thematicMap.getScreenBounds();

        float centerX = (float) screenBounds.getCenterX();
        float centerY = (float) screenBounds.getCenterY();

        float minX = (float) screenBounds.getMinX();
        float minY = (float) screenBounds.getMinY();
        float maxX = (float) screenBounds.getMaxX();
        float maxY = (float) screenBounds.getMaxY();

        line(centerX, minY, centerX, maxY); // Vertical centre line

        line(minX, centerY, maxX, centerY); // Horizontal centre line

        rect(minX, minY, (float) screenBounds.getWidth(), (float) screenBounds.getHeight()); // Border

        // Draw text

        if (screenBounds.contains(mouseX, mouseY))
        {
            PVector geo = thematicMap.screenToGeo(new PVector(mouseX, mouseY));

            fill(128);

            text("[" + nfp(geo.x, 3, 2) + ", " + nfp(geo.y, 2, 2) + "]", minX + 2, minY);
        }

        noLoop();
    }

    public void mouseMoved()
    {
        loop();
    }

    public static void main(String[] args)
    {
        PApplet.main("uk.me.dillingham.thematicmap.sketches.ThematicMapSketch");
    }
}

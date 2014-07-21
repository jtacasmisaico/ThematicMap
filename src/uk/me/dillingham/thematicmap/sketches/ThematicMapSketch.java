package uk.me.dillingham.thematicmap.sketches;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PVector;
import uk.me.dillingham.thematicmap.ThematicMap;

@SuppressWarnings("serial")
public class ThematicMapSketch extends PApplet
{
    boolean isCentreLines;
    ThematicMap thematicMap;

    public void setup()
    {
        size(740, 380);

        isCentreLines = false;

        thematicMap = new ThematicMap(this);

        thematicMap.read("ne_110m_admin_0_countries_lakes/ne_110m_admin_0_countries_lakes.shp");

        thematicMap.setWindow(new Rectangle2D.Float(10, 10, 720, 360));

        textAlign(LEFT, TOP);

        textFont(createFont("SourceSansPro-Regular", 12));

        println(thematicMap.getAttributeTable().getString(168, 3)); // USA
    }

    public void draw()
    {
        background(255);

        // Draw map

        fill(240);

        stroke(189);

        strokeWeight(0.5f);

        thematicMap.draw();

        // Draw highlighted feature

        fill(189);

        thematicMap.draw(168); // USA

        // Draw border

        noFill();

        stroke(99);

        Rectangle2D window = thematicMap.getWindow();

        float centerX = (float) window.getCenterX();
        float centerY = (float) window.getCenterY();

        float minX = (float) window.getMinX();
        float minY = (float) window.getMinY();
        float maxX = (float) window.getMaxX();
        float maxY = (float) window.getMaxY();

        if (isCentreLines)
        {
            line(centerX, minY, centerX, maxY); // Vertical centre line

            line(minX, centerY, maxX, centerY); // Horizontal centre line
        }

        rect(minX, minY, (float) window.getWidth(), (float) window.getHeight()); // Border

        // Draw text

        if (window.contains(mouseX, mouseY))
        {
            PVector geo = thematicMap.screenToGeo(new PVector(mouseX, mouseY));

            fill(99);

            text("[" + nfp(geo.x, 3, 2) + ", " + nfp(geo.y, 2, 2) + "]", minX + 2, minY);
        }

        noLoop();
    }

    public void keyPressed()
    {
        if (key == ' ')
        {
            isCentreLines = !isCentreLines;
        }

        if (key == 's')
        {
            save("img/ThematicMapSketch.png");
        }

        loop();
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

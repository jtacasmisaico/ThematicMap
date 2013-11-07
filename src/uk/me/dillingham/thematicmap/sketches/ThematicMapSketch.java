package uk.me.dillingham.thematicmap.sketches;

import java.io.File;

import processing.core.PApplet;
import processing.core.PVector;
import uk.me.dillingham.thematicmap.ThematicMap;
import uk.me.dillingham.thematicmap.projections.Equirectangular;

@SuppressWarnings("serial")
public class ThematicMapSketch extends PApplet
{
    private ThematicMap map;

    public void setup()
    {
        size(380, 200);

        map = new ThematicMap(this, new Equirectangular());

        String prefix = "ne_110m_admin_0_countries_lakes";

        map.read(prefix + File.separator + prefix + ".shp");

        textAlign(LEFT, TOP);

        textFont(createFont("SourceSansPro-Regular", 12));
    }

    public void draw()
    {
        background(255);

        pushMatrix();

        translate(10, 10);

        // Draw map

        fill(color(224));

        stroke(255);

        strokeWeight((float) 0.5);

        map.draw();

        // Draw border

        noFill();

        stroke(128);

        line(180, 0, 180, 180); // Vertical centre line

        line(0, 90, 360, 90); // Horizontal centre line

        rect(0, 0, 360, 180); // Border

        // Draw text

        PVector geo = map.screenToGeo(new PVector(mouseX - 10, mouseY - 10));

        fill(128);

        text("[" + nfp(geo.x, 3, 2) + ", " + nfp(geo.y, 2, 2) + "]", 2, 0);

        popMatrix();

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

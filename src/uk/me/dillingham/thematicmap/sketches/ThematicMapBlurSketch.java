package uk.me.dillingham.thematicmap.sketches;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PGraphics;
import uk.me.dillingham.thematicmap.ThematicMap;

@SuppressWarnings("serial")
public class ThematicMapBlurSketch extends PApplet
{
    boolean isBlurred;
    ThematicMap notNorthAmericaLakes, northAmerica;
    PGraphics northAmericaGraphics;

    public void setup()
    {
        size(740, 380);

        isBlurred = false;

        Rectangle2D.Float geoBounds = new Rectangle2D.Float(-180, -90, 360, 180);

        Rectangle2D.Float screenBounds = new Rectangle2D.Float(10, 10, 720, 360);

        notNorthAmericaLakes = new ThematicMap(this);

        notNorthAmericaLakes.setGeoBounds(geoBounds);

        notNorthAmericaLakes.setScreenBounds(screenBounds);

        notNorthAmericaLakes.read("ne_110m_admin_0_countries_lakes/Not_North_America.shp");

        northAmerica = new ThematicMap(this);

        northAmerica.setGeoBounds(geoBounds);

        northAmerica.setScreenBounds(screenBounds);

        northAmerica.read("ne_110m_admin_0_countries_lakes/North_America.shp");

        // Graphics

        northAmericaGraphics = createGraphics(width, height);

        northAmericaGraphics();
    }

    public void draw()
    {
        background(255);

        fill(222, 235, 247);

        noStroke();

        rect(10, 10, 720, 360); // 'Sea' rectangle

        style(northAmericaGraphics.getStyle());

        notNorthAmericaLakes.draw();

        image(northAmericaGraphics, 0, 0);

        noFill();

        stroke(99);

        rect(10, 10, 720, 360); // Border rectangle
    }

    public void keyPressed()
    {
        if (key == ' ')
        {
            isBlurred = !isBlurred;

            northAmericaGraphics();
        }

        if (key == 's')
        {
            String suffix = isBlurred ? "Blur" : "NoBlur";

            save("img/ThematicMapBlurSketch" + suffix + ".png");
        }
    }

    private void northAmericaGraphics()
    {
        northAmericaGraphics.beginDraw();

        northAmericaGraphics.background(255, 0); // Alpha should equal zero

        northAmericaGraphics.fill(229, 245, 224);

        northAmericaGraphics.stroke(161, 217, 155);

        northAmericaGraphics.strokeWeight(0.5f);

        northAmerica.draw(northAmericaGraphics);

        if (isBlurred)
        {
            northAmericaGraphics.filter(BLUR, 1);
        }

        northAmericaGraphics.endDraw();
    }

    public static void main(String[] args)
    {
        PApplet.main("uk.me.dillingham.thematicmap.sketches.ThematicMapBlurSketch");
    }
}

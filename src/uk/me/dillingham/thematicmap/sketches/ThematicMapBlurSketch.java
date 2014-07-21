package uk.me.dillingham.thematicmap.sketches;

import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PGraphics;
import uk.me.dillingham.thematicmap.ThematicMap;

@SuppressWarnings("serial")
public class ThematicMapBlurSketch extends PApplet
{
    boolean isBlurred;
    ThematicMap countriesLakes;
    PGraphics countriesLakesGraphics;

    public void setup()
    {
        size(820, 424);

        isBlurred = false;

        // Initialise map

        countriesLakes = new ThematicMap(this);

        countriesLakes.read("ne_110m_admin_0_countries_lakes/ne_110m_admin_0_countries_lakes_robinson.shp");

        countriesLakes.setGeoBounds(countriesLakes.getMBR());

        countriesLakes.setWindow(new Rectangle2D.Float(10, 10, 800, 404));

        // Graphics

        countriesLakesGraphics = createGraphics(width, height);

        countriesLakesGraphics();
    }

    public void draw()
    {
        background(255);

        style(countriesLakesGraphics.getStyle());

        for (int featureIndex = 0; featureIndex < countriesLakes.getAttributeTable().getRowCount(); featureIndex++)
        {
            if (featureIndex != 8)
            {
                countriesLakes.draw(featureIndex);
            }
        }

        image(countriesLakesGraphics, 0, 0);

        // Draw border

        noFill();

        stroke(99);

        rect(10, 10, 800, 404);
    }

    public void keyPressed()
    {
        if (key == ' ')
        {
            isBlurred = !isBlurred;

            countriesLakesGraphics();
        }

        if (key == 's')
        {
            String suffix = isBlurred ? "Blur" : "NoBlur";

            save("img/ThematicMapBlurSketch" + suffix + ".png");
        }
    }

    private void countriesLakesGraphics()
    {
        countriesLakesGraphics.beginDraw();

        countriesLakesGraphics.background(255, 0); // Alpha should equal zero

        countriesLakesGraphics.fill(222, 235, 247);

        countriesLakesGraphics.stroke(49, 130, 189);

        countriesLakesGraphics.strokeWeight(0.5f);

        countriesLakes.draw(8, countriesLakesGraphics);

        if (isBlurred)
        {
            countriesLakesGraphics.filter(BLUR, 2);
        }

        countriesLakesGraphics.endDraw();
    }

    public static void main(String[] args)
    {
        PApplet.main("uk.me.dillingham.thematicmap.sketches.ThematicMapBlurSketch");
    }
}

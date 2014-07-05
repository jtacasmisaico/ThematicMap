ThematicMap
===========
A library for drawing thematic maps in [Processing](http://processing.org/). Undocumented, untested, and unfinished!

Features
--------
There are several libraries for drawing thematic maps in Processing. What sets ThematicMap apart?

ThematicMap reads features from a [shapefile](http://en.wikipedia.org/wiki/Shapefile) into objects from the [JTS Topology Suite](http://en.wikipedia.org/wiki/JTS_Topology_Suite). It then draws these features in Processing. It reads and draws with just two classes: `ShpFile` and `ThematicMap`. With the obvious exceptions of the JTS Topology Suite and Processing, there are no other dependencies.

A simple ThematicMap, which uses the [equirectangular projection](http://en.wikipedia.org/wiki/Equirectangular_projection), might look like this:

![ThematicMapSketch.png](https://raw.githubusercontent.com/iaindillingham/ThematicMap/master/img/ThematicMapSketch.png)

There's a one-to-one relationship between a ThematicMap and a shapefile. However, because it's often necessary to layer shapefiles, ThematicMap makes it easy to ensure that they all share the same geographic and screen extents.

A layered ThematicMap, which uses the [Robinson projection](http://en.wikipedia.org/wiki/Robinson_projection), might look like this:

![ThematicMapLayersSketch.png](https://raw.githubusercontent.com/iaindillingham/ThematicMap/master/img/ThematicMapLayersSketch.png)

ThematicMap makes it easy to draw different features into different graphics contexts. Consequently, it's easy to add a filter, such as a blur, to a feature. (You might wish to use the amount of blur to represent the amount of uncertainty associated with the feature.)

Compare Australia in the following two figures:

![ThematicMapBlurSketchNoBlur.png](https://raw.githubusercontent.com/iaindillingham/ThematicMap/master/img/ThematicMapBlurSketchNoBlur.png)

![ThematicMapBlurSketchBlur.png](https://raw.githubusercontent.com/iaindillingham/ThematicMap/master/img/ThematicMapBlurSketchBlur.png)

package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent a 3D scene with various elements such as background color,
 * ambient lighting, and geometries.
 */
public class Scene {

    /**
     * Name of the scene
     */
    public String name;
    /**
     * Background color of the scene, default is black
     */
    public Color background = Color.BLACK;
    /**
     * Ambient light of the scene, default is none
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * Geometries in the scene, default is an empty model
     */
    public Geometries geometries = new Geometries();

    /**
     * List of light sources in the scene
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructor to initialize the scene with a name.
     *
     * @param name The name of the scene.
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Setter for the background color.
     *
     * @param background The background color to set.
     * @return The current Scene object.
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Setter for the ambient light.
     *
     * @param ambientLight The ambient light to set.
     * @return The current Scene object.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Setter for the geometries.
     *
     * @param geometries The geometries to set.
     * @return The current Scene object.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Setter for the list of light sources.
     *
     * @param lights The list of light sources to set.
     * @return The current Scene object.
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}

package com.mycompany.efectos;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Iluminacion {

    /**
     * Configura la iluminación básica para la escena
     * @param rootNode El nodo raíz de la escena
     */
    public static void configurarIluminacion(Node rootNode) {
        // 1. Luz direccional principal (sol)
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        // 2. Luz direccional secundaria para iluminar desde otro ángulo
        DirectionalLight fill = new DirectionalLight();
        fill.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        fill.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(fill);

        // 3. Luz puntual para destacar áreas específicas
        PointLight pointLight = new PointLight();
        pointLight.setPosition(new Vector3f(0, 5, 0));
        pointLight.setColor(ColorRGBA.White.mult(0.8f));
        pointLight.setRadius(20f);
        rootNode.addLight(pointLight);

        // 4. Luz ambiental para evitar que las áreas sin luz directa queden completamente oscuras
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.2f));
        rootNode.addLight(ambient);
    }

    /**
     * Agrega una luz direccional a la escena
     * @param rootNode El nodo raíz de la escena
     * @param direccion Dirección de la luz
     * @param color Color de la luz
     */
    public static void agregarLuzDireccional(Node rootNode, Vector3f direccion, ColorRGBA color) {
        DirectionalLight luz = new DirectionalLight();
        luz.setDirection(direccion.normalizeLocal());
        luz.setColor(color);
        rootNode.addLight(luz);
    }

    /**
     * Agrega una luz puntual a la escena
     * @param rootNode El nodo raíz de la escena
     * @param posicion Posición de la luz
     * @param color Color de la luz
     * @param radio Radio de influencia de la luz
     */
    public static void agregarLuzPuntual(Node rootNode, Vector3f posicion, ColorRGBA color, float radio) {
        PointLight luz = new PointLight();
        luz.setPosition(posicion);
        luz.setColor(color);
        luz.setRadius(radio);
        rootNode.addLight(luz);
    }

    /**
     * Agrega una luz ambiental a la escena
     * @param rootNode El nodo raíz de la escena
     * @param color Color de la luz ambiental
     */
    public static void agregarLuzAmbiental(Node rootNode, ColorRGBA color) {
        AmbientLight luz = new AmbientLight();
        luz.setColor(color);
        rootNode.addLight(luz);
    }
}
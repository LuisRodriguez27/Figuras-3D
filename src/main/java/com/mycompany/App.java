package com.mycompany;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.mycompany.efectos.Iluminacion;
import com.mycompany.figuras.*;
import com.mycompany.interfaz.GUI;

import java.util.HashMap;
import java.util.Map;

public class App extends SimpleApplication {

    private Figuras figuras; // Gestor de figuras
    private GUI gui; // Interfaz gráfica

    private Map<Geometry, FiguraBase> geometryToFiguraMap = new HashMap<>();

    public static void main(String[] args) {
        App app = new App();
        app.start(); // Inicia la aplicación
    }

    @Override
    public void simpleInitApp() {
        // Configuración inicial
        flyCam.setEnabled(false); // Desactivar la cámara voladora
        cam.setLocation(new com.jme3.math.Vector3f(0, 5, 18)); // Posición inicial de la cámara
        cam.lookAt(new com.jme3.math.Vector3f(0, 0, -5), com.jme3.math.Vector3f.UNIT_Y);

        // Crear nodo raíz para las figuras
        Node rootNodeFiguras = new Node("RootNodeFiguras");
        rootNode.attachChild(rootNodeFiguras);

        // Crear gestor de figuras
        figuras = new Figuras(assetManager, rootNodeFiguras);

        // Crear y colocar las figuras en la escena
        figuras.crearYColocarFiguras();

        // Registrar todas las figuras en la GUI
        gui = new GUI(this, rootNode);
        for (FiguraBase figura : figuras.getFigurasBase()) {
            registrarFigura(figura);
            gui.registrarFigura(figura);
        }

        // Configurar la GUI
//        gui.createSlidersForSelectedFigure(); // Crea los sliders iniciales (vacíos)

        // Configurar iluminación
        Iluminacion.configurarIluminacion(rootNode);
    }

    public void registrarFigura(FiguraBase figura) {
        geometryToFiguraMap.put(figura.getGeometry(), figura);
    }

    private FiguraBase obtenerFiguraBaseDesdeGeometry(Geometry geometry) {
        return geometryToFiguraMap.get(geometry);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Aplicar transformaciones basadas en los valores de los sliders
        if (gui != null) {
            gui.applyTransformations();
        }
    }
}
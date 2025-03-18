package com.mycompany.pruebas;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.light.DirectionalLight;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;

import java.util.HashMap;
import java.util.Map;

public class Pruebas extends SimpleApplication {

    private Node cubeGroup; // Nodo que agrupa cubo y borde
    private Geometry cubeGeometry;
    private Geometry currentWireframe;

    Figura3D cubo;
    Figura3D esfera;
    Figura3D cilindro;
    Figura3D toro;
    Figura3D piramide;

    private Figura3D selectedFigure = null; // Agregar esto después de las declaraciones de las figuras

    public static void main(String[] args) {
        Pruebas app = new Pruebas();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Configurar Lemur para la GUI
        GuiGlobals.initialize(this);

        // Cargar el estilo "glass"
        try {
            BaseStyles.loadGlassStyle();
            GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        } catch (Exception e) {
            System.err.println("Error al cargar el estilo 'glass': " + e.getMessage());
            e.printStackTrace();
            GuiGlobals.getInstance().getStyles().setDefaultStyle("default");
        }

        // Configurar fondo blanco
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        // Crear nodo grupo
        cubeGroup = new Node("CuboConBorde");

        // Crear cubo
        Box box = new Box(1f, 1f, 1f);
        cubeGeometry = new Geometry("Cubo Azul", box);

        // Material azul
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        cubeGeometry.setMaterial(mat);

        // Añadir cubo y borde al grupo
        cubeGroup.attachChild(cubeGeometry);

//        // Colocar todo el grupo en una posición visible
//        cubeGroup.setLocalTranslation(0, 0, -5);
//
//        // Añadir grupo a la escena
//        rootNode.attachChild(cubeGroup);

        // Crear figuras
        cubo = new Figura3D(assetManager, "cubo", ColorRGBA.Blue);
        esfera = new Figura3D(assetManager, "esfera", ColorRGBA.Red);
        cilindro = new Figura3D(assetManager, "cilindro", ColorRGBA.Green);
        toro = new Figura3D(assetManager, "toro", ColorRGBA.Yellow);
        piramide = new Figura3D(assetManager, "piramide", ColorRGBA.Magenta);

        // Posicionar figuras
        cubo.getFiguraGroup().setLocalTranslation(-5, 0, -5);
        esfera.getFiguraGroup().setLocalTranslation(-2, 0, -5);
        cilindro.getFiguraGroup().setLocalTranslation(1, 0, -5);
        toro.getFiguraGroup().setLocalTranslation(3, 0, -5);
        piramide.getFiguraGroup().setLocalTranslation(6, 0, -5);

        // Agregar figuras a la escena
        rootNode.attachChild(cubo.getFiguraGroup());
        rootNode.attachChild(esfera.getFiguraGroup());
        rootNode.attachChild(cilindro.getFiguraGroup());
        rootNode.attachChild(toro.getFiguraGroup());
        rootNode.attachChild(piramide.getFiguraGroup());



        // Luz direccional
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        light.setColor(ColorRGBA.White);
        rootNode.addLight(light);

        // Hacer visible el cursor y configurar cámara
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(analogListener, "pick target");

        cam.setLocation(new Vector3f(0, 2, 15));
        cam.lookAt(cubeGroup.getLocalTranslation(), Vector3f.UNIT_Y);

        // Crear contenedores para la GUI
        String[] titles = {"Rotacion", "Sesgado", "Escalamiento"};
        int initialY = cam.getHeight() - 10;
        int containerHeight = 150;

        for (int i = 0; i < titles.length; i++) {
            Container window = new Container();
            guiNode.attachChild(window);
            createSlidersWithLabels(window, initialY - i * containerHeight, titles[i]);
        }
    }

    // Mapa para almacenar las referencias de los sliders
    private Map<String, VersionedReference<Double>> sliderRefs = new HashMap<>();

    private void createSlidersWithLabels(Container window, int y, String title) {
        if(title != "Escalamiento") {
            // Ajustar la posición del contenedor
            window.setLocalTranslation(10, y, 0);

            // Agregar una etiqueta para el título
            Label titleLabel = new Label(title);
            window.addChild(titleLabel);

            // Slider para el eje X
            Label xAxisLabel = new Label("Eje X:");
            window.addChild(xAxisLabel);

            Slider sliderX = new Slider("glass");
            sliderX.setPreferredSize(new Vector3f(200, 20, 0));
            sliderX.getModel().setValue(49);

            VersionedReference<Double> refX = window.addChild(sliderX).getModel().createReference();
            sliderRefs.put(title + "_X", refX); // Asociar la referencia con una clave única

            // Slider para el eje Y
            Label yAxisLabel = new Label("Eje Y:");
            window.addChild(yAxisLabel);
            Slider sliderY = new Slider();
            sliderY.setPreferredSize(new Vector3f(200, 20, 0));
            sliderY.getModel().setValue(49);

            VersionedReference<Double> refY = window.addChild(sliderY).getModel().createReference();
            sliderRefs.put(title + "_Y", refY); // Asociar la referencia con una clave única

            // Slider para el eje Z
            Label zAxisLabel = new Label("Eje Z:");
            window.addChild(zAxisLabel);
            Slider sliderZ = new Slider();
            sliderZ.setPreferredSize(new Vector3f(200, 20, 0));
            sliderZ.getModel().setValue(49);

            VersionedReference<Double> refZ = window.addChild(sliderZ).getModel().createReference();
            sliderRefs.put(title + "_Z", refZ); // Asociar la referencia con una clave única
        }else{
            // Ajustar la posición del contenedor
            window.setLocalTranslation(10, y, 0);

            // Agregar una etiqueta para el título
            Label titleLabel = new Label(title);
            window.addChild(titleLabel);

            // Slider para el eje X
            Label xAxisLabel = new Label("General:");
            window.addChild(xAxisLabel);

            Slider sliderX = new Slider("glass");
            sliderX.setPreferredSize(new Vector3f(200, 20, 0));
            sliderX.getModel().setValue(49);

            VersionedReference<Double> refX = window.addChild(sliderX).getModel().createReference();
            sliderRefs.put(title + "_General", refX); // Asociar la referencia con una clave única
        }

    }

    @Override
    public void simpleUpdate(float tpf) {
        for (Map.Entry<String, VersionedReference<Double>> entry : sliderRefs.entrySet()) {
            String sliderKey = entry.getKey(); // Clave única del slider
            VersionedReference<Double> ref = entry.getValue();

            if (ref.update()) {
                float value = (float) (ref.get() / 1);
                System.out.println("Nuevo valor para " + sliderKey + ": " + value);
            }
        }
    }

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("pick target")) {
                // Obtener posición del cursor
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

                // Crear un rayo desde la cámara hacia la escena
                Ray ray = new Ray(click3d, dir);
                CollisionResults results = new CollisionResults();
                rootNode.collideWith(ray, results);

                // Verificar si hay colisiones
                if (results.size() > 0) {
                    // Obtener la geometría más cercana
                    Geometry target = results.getClosestCollision().getGeometry();

                    // Buscar qué figura fue clickeada
                    Figura3D clickedFigure = null;
                    for (Figura3D figura : new Figura3D[]{cubo, esfera, cilindro, toro, piramide}) {
                        if (figura.getFiguraGeometry() == target) {
                            clickedFigure = figura;
                            break;
                        }
                    }

                    // Si se clickeó una figura diferente a la seleccionada
                    if (clickedFigure != null && clickedFigure != selectedFigure) {
                        // Si había una figura seleccionada previamente, quitar su borde
                        if (selectedFigure != null) {
                            selectedFigure.quitarWireframe();
                        }

                        // Establecer la nueva figura seleccionada
                        selectedFigure = clickedFigure;

                        // Agregar el borde a la nueva figura seleccionada
                        selectedFigure.agregarWireframe(assetManager);
                    }
                } else {
                    // Si no hay colisiones, limpiar la selección actual
                    if (selectedFigure != null) {
                        selectedFigure.quitarWireframe();
                        selectedFigure = null; // Limpiar la referencia a la figura seleccionada
                    }
                }
            }
        }
    };

    private Material createWireframeMaterial() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);

        // Configuramos el grosor de línea (solo funciona si la tarjeta gráfica lo soporta)
        mat.getAdditionalRenderState().setLineWidth(2.0f);

        return mat;
    }

    private Geometry createCubeEdges(Box box) {
        // Creamos un WireBox que solo muestra las aristas sin diagonales
        WireBox wireBox = new WireBox(box.getXExtent(), box.getYExtent(), box.getZExtent());
        Geometry wireGeo = new Geometry("WireBox", wireBox);
        wireGeo.setMaterial(createWireframeMaterial());
        return wireGeo;
    }
}
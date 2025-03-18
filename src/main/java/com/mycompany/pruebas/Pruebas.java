//package com.mycompany.pruebas;
//
//import com.jme3.input.MouseInput;
//import com.jme3.input.controls.MouseButtonTrigger;
//import com.jme3.input.controls.AnalogListener;
//import com.jme3.material.Material;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Ray;
//import com.jme3.math.Vector2f;
//import com.jme3.math.Vector3f;
//import com.jme3.collision.CollisionResults;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Node;
//import com.jme3.app.SimpleApplication;
//import com.jme3.scene.debug.WireBox;
//import com.jme3.scene.shape.Box;
//import com.jme3.light.DirectionalLight;
//import com.simsilica.lemur.Container;
//import com.simsilica.lemur.GuiGlobals;
//import com.simsilica.lemur.Label;
//import com.simsilica.lemur.Slider;
//import com.simsilica.lemur.core.VersionedReference;
//import com.simsilica.lemur.style.BaseStyles;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Pruebas extends SimpleApplication {
//
//    private Node cubeGroup; // Nodo que agrupa cubo y borde
//    private Geometry cubeGeometry;
//    private Geometry currentWireframe;
//
//    public static void main(String[] args) {
//        Pruebas app = new Pruebas();
//        app.start();
//    }
//
//    @Override
//    public void simpleInitApp() {
//        // Configurar Lemur para la GUI
//        GuiGlobals.initialize(this);
//
//        // Cargar el estilo "glass"
//        try {
//            BaseStyles.loadGlassStyle();
//            GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
//        } catch (Exception e) {
//            System.err.println("Error al cargar el estilo 'glass': " + e.getMessage());
//            e.printStackTrace();
//            GuiGlobals.getInstance().getStyles().setDefaultStyle("default");
//        }
//
//        // Configurar fondo blanco
//        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
//
//        // Crear nodo grupo
//        cubeGroup = new Node("CuboConBorde");
//
//        // Crear cubo
//        Box box = new Box(1f, 1f, 1f);
//        cubeGeometry = new Geometry("Cubo Azul", box);
//
//        // Material azul
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        cubeGeometry.setMaterial(mat);
//
//        // Añadir cubo y borde al grupo
//        cubeGroup.attachChild(cubeGeometry);
//
//        // Colocar todo el grupo en una posición visible
//        cubeGroup.setLocalTranslation(0, 0, -5);
//
//        // Añadir grupo a la escena
//        rootNode.attachChild(cubeGroup);
//
////        Figura3D cubo = new Figura3D(assetManager, "cubo", ColorRGBA.Blue);
////        // Crear figuras
////        Figura3D esfera = new Figura3D(assetManager, "esfera", ColorRGBA.Red);
////        Figura3D cilindro = new Figura3D(assetManager, "cilindro", ColorRGBA.Green);
////        Figura3D toro = new Figura3D(assetManager, "toro", ColorRGBA.Yellow);
////        Figura3D piramide = new Figura3D(assetManager, "piramide", ColorRGBA.Magenta);
////
////        // Posicionar figuras
////        esfera.getFiguraGroup().setLocalTranslation(-6, 0, -5);
////        cilindro.getFiguraGroup().setLocalTranslation(-2, 0, -5);
////        toro.getFiguraGroup().setLocalTranslation(2, 0, -5);
////        piramide.getFiguraGroup().setLocalTranslation(6, 0, -5);
////
////        // Agregar figuras a la escena
////        rootNode.attachChild(esfera.getFiguraGroup());
////        rootNode.attachChild(cilindro.getFiguraGroup());
////        rootNode.attachChild(toro.getFiguraGroup());
////        rootNode.attachChild(piramide.getFiguraGroup());
//
//
//
//        // Luz direccional
//        DirectionalLight light = new DirectionalLight();
//        light.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
//        light.setColor(ColorRGBA.White);
//        rootNode.addLight(light);
//
//        // Hacer visible el cursor y configurar cámara
//        inputManager.setCursorVisible(true);
//        flyCam.setEnabled(false);
//        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
//        inputManager.addListener(analogListener, "pick target");
//
//        cam.setLocation(new Vector3f(0, 2, 5));
//        cam.lookAt(cubeGroup.getLocalTranslation(), Vector3f.UNIT_Y);
//
//        // Crear contenedores para la GUI
//        String[] titles = {"Rotacion", "Sesgado", "Escalamiento"};
//        int initialY = cam.getHeight() - 10;
//        int containerHeight = 150;
//
//        for (int i = 0; i < titles.length; i++) {
//            Container window = new Container();
//            guiNode.attachChild(window);
//            createSlidersWithLabels(window, initialY - i * containerHeight, titles[i]);
//        }
//    }
//
//    // Mapa para almacenar las referencias de los sliders
//    private Map<String, VersionedReference<Double>> sliderRefs = new HashMap<>();
//
//    private void createSlidersWithLabels(Container window, int y, String title) {
//        if(title != "Escalamiento") {
//            // Ajustar la posición del contenedor
//            window.setLocalTranslation(10, y, 0);
//
//            // Agregar una etiqueta para el título
//            Label titleLabel = new Label(title);
//            window.addChild(titleLabel);
//
//            // Slider para el eje X
//            Label xAxisLabel = new Label("Eje X:");
//            window.addChild(xAxisLabel);
//
//            Slider sliderX = new Slider("glass");
//            sliderX.setPreferredSize(new Vector3f(200, 20, 0));
//            sliderX.getModel().setValue(49);
//
//            VersionedReference<Double> refX = window.addChild(sliderX).getModel().createReference();
//            sliderRefs.put(title + "_X", refX); // Asociar la referencia con una clave única
//
//            // Slider para el eje Y
//            Label yAxisLabel = new Label("Eje Y:");
//            window.addChild(yAxisLabel);
//            Slider sliderY = new Slider();
//            sliderY.setPreferredSize(new Vector3f(200, 20, 0));
//            sliderY.getModel().setValue(49);
//
//            VersionedReference<Double> refY = window.addChild(sliderY).getModel().createReference();
//            sliderRefs.put(title + "_Y", refY); // Asociar la referencia con una clave única
//
//            // Slider para el eje Z
//            Label zAxisLabel = new Label("Eje Z:");
//            window.addChild(zAxisLabel);
//            Slider sliderZ = new Slider();
//            sliderZ.setPreferredSize(new Vector3f(200, 20, 0));
//            sliderZ.getModel().setValue(49);
//
//            VersionedReference<Double> refZ = window.addChild(sliderZ).getModel().createReference();
//            sliderRefs.put(title + "_Z", refZ); // Asociar la referencia con una clave única
//        }else{
//            // Ajustar la posición del contenedor
//            window.setLocalTranslation(10, y, 0);
//
//            // Agregar una etiqueta para el título
//            Label titleLabel = new Label(title);
//            window.addChild(titleLabel);
//
//            // Slider para el eje X
//            Label xAxisLabel = new Label("General:");
//            window.addChild(xAxisLabel);
//
//            Slider sliderX = new Slider("glass");
//            sliderX.setPreferredSize(new Vector3f(200, 20, 0));
//            sliderX.getModel().setValue(49);
//
//            VersionedReference<Double> refX = window.addChild(sliderX).getModel().createReference();
//            sliderRefs.put(title + "_General", refX); // Asociar la referencia con una clave única
//        }
//
//    }
//
//    @Override
//    public void simpleUpdate(float tpf) {
//        for (Map.Entry<String, VersionedReference<Double>> entry : sliderRefs.entrySet()) {
//            String sliderKey = entry.getKey(); // Clave única del slider
//            VersionedReference<Double> ref = entry.getValue();
//
//            if (ref.update()) {
//                float value = (float) (ref.get() / 1);
//                System.out.println("Nuevo valor para " + sliderKey + ": " + value);
//            }
//        }
//    }
//
//    private AnalogListener analogListener = new AnalogListener() {
//        public void onAnalog(String name, float intensity, float tpf) {
//            if (name.equals("pick target")) {
//                CollisionResults results = new CollisionResults();
//                Vector2f click2d = inputManager.getCursorPosition();
//                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
//                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
//                Ray ray = new Ray(click3d, dir);
//                rootNode.collideWith(ray, results);
//
//                if (results.size() > 0) {
//                    Geometry target = results.getClosestCollision().getGeometry();
//
//                    if (target.getName().equals("Cubo Azul")) {
//                        if (currentWireframe == null) {
//                            currentWireframe = createCubeEdges((Box) cubeGeometry.getMesh());
//                            currentWireframe.setMaterial(createWireframeMaterial());
//                            cubeGroup.attachChild(currentWireframe); // Ahora están unidos
//                        }
//
//                        // Rotar TODO el grupo (cubo + borde)
//                        cubeGroup.rotate(0, -intensity, 0);
//
//                        // Ejemplo de mover todo el grupo
////                         cubeGroup.move(0.1f, 0, 0); // Descomenta si quieres moverlo a la derecha
//
//                        // Ejemplo de escalar todo el grupo
////                         cubeGroup.setLocalScale(1.5f); // Descomenta si quieres escalar
//                    }
//                }
//            }
//        }
//    };
//
//    private Material createWireframeMaterial() {
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Black);
//
//        // Configuramos el grosor de línea (solo funciona si la tarjeta gráfica lo soporta)
//        mat.getAdditionalRenderState().setLineWidth(2.0f);
//
//        return mat;
//    }
//
//    private Geometry createCubeEdges(Box box) {
//        // Creamos un WireBox que solo muestra las aristas sin diagonales
//        WireBox wireBox = new WireBox(box.getXExtent(), box.getYExtent(), box.getZExtent());
//        Geometry wireGeo = new Geometry("WireBox", wireBox);
//        wireGeo.setMaterial(createWireframeMaterial());
//        return wireGeo;
//    }
//
//}




package com.mycompany.pruebas;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Geometry;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;

import java.util.HashMap;
import java.util.Map;

public class Pruebas extends SimpleApplication {

    private Figura3D selectedFigure = null; // Figura seleccionada actualmente

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

        // Luz direccional
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        light.setColor(ColorRGBA.White);
        rootNode.addLight(light);

        // Crear figuras usando la clase Figura3D
        Figura3D cubo = new Figura3D(assetManager, "cubo", ColorRGBA.Blue);
        Figura3D esfera = new Figura3D(assetManager, "esfera", ColorRGBA.Red);
        Figura3D cilindro = new Figura3D(assetManager, "cilindro", ColorRGBA.Green);
        Figura3D toro = new Figura3D(assetManager, "toro", ColorRGBA.Yellow);
        Figura3D piramide = new Figura3D(assetManager, "piramide", ColorRGBA.Magenta);

        // Posicionar figuras
        float startX = -8.0f; // Posición inicial en X
        float spacing = 4.0f; // Espaciado entre figuras
        float y = 0.0f;       // Altura uniforme
        float z = -5.0f;      // Profundidad uniforme

        cubo.getFiguraGroup().setLocalTranslation(startX, y, z);
        esfera.getFiguraGroup().setLocalTranslation(startX + spacing, y, z);
        cilindro.getFiguraGroup().setLocalTranslation(startX + spacing * 2, y, z);
        toro.getFiguraGroup().setLocalTranslation(startX + spacing * 3, y, z);
        piramide.getFiguraGroup().setLocalTranslation(startX + spacing * 4, y, z);

        // Agregar figuras a la escena
        rootNode.attachChild(cubo.getFiguraGroup());
        rootNode.attachChild(esfera.getFiguraGroup());
        rootNode.attachChild(cilindro.getFiguraGroup());
        rootNode.attachChild(toro.getFiguraGroup());
        rootNode.attachChild(piramide.getFiguraGroup());

        // Hacer visible el cursor y configurar cámara
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);

        // Configurar cámara
        cam.setLocation(new Vector3f(0, 4, 15)); // Posición más lejana
        cam.lookAt(new Vector3f(0, 2, -5), Vector3f.UNIT_Y); // Mirar hacia el centro de las figuras

        // Configurar AnalogListener para selección de figuras
        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(analogListener, "pick target");

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

    /**
     * AnalogListener para detectar clics del mouse y seleccionar figuras
     */
    private AnalogListener analogListener = new AnalogListener() {
        @Override
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

                    // Seleccionar la figura
                    seleccionarFigura(target);
                }
            }
        }
    };

    /**
     * Selecciona una figura y muestra su wireframe
     */
    private void seleccionarFigura(Geometry geometry) {
        // Si hay una figura seleccionada previamente, quitar su wireframe
        if (selectedFigure != null) {
            selectedFigure.quitarWireframe();
        }

        // Buscar la figura correspondiente a la geometría seleccionada
        for (Figura3D figura : new Figura3D[]{cubo, esfera, cilindro, toro, piramide}) {
            if (figura.getFiguraGeometry() == geometry) {
                selectedFigure = figura;
                selectedFigure.agregarWireframe(assetManager);
                break;
            }
        }
    }

    // Mapa para almacenar las referencias de los sliders
    private Map<String, VersionedReference<Double>> sliderRefs = new HashMap<>();

    private void createSlidersWithLabels(Container window, int y, String title) {
        if (!title.equals("Escalamiento")) {
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
        } else {
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
}
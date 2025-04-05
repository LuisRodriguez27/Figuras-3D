package com.mycompany.train;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;

import java.awt.*;

public class Figura extends SimpleApplication {

    private Piezas piezas;

    private Node[] nodes = new Node[10];
    private Geometry[] geoms = new Geometry[40];
    private Node[] pivots = new Node[40];

    // Nodo principal para agrupar todas las figuras
    private Node mainNode;

    DirectionalLight sun;
    Geometry floor;

    private float currentSunHeight = -6;

    public static void main(String[] args) {
        Figura app = new Figura();

        // Configuración de la ventana
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Figuras 3D ");

        // Tamaño de ventana personalizado
        int windowWidth = 1366;
        int windowHeight = 768;
        settings.setWidth(windowWidth);
        settings.setHeight(windowHeight);

        // Obtener la configuración del monitor principal
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();

        // Calcular posición para centrar perfectamente
        int posX = bounds.x + (bounds.width - windowWidth) / 2;
        int posY = bounds.y + (bounds.height - windowHeight) / 2;

        // Establecer posición de la ventana
        settings.setWindowXPosition(posX);
        settings.setWindowYPosition(posY);

        // Otras configuraciones
        settings.setResizable(true);
        settings.setVSync(true);
        settings.setSamples(4); // Anti-aliasing

        app.setSettings(settings);
        app.setShowSettings(false); // No mostrar ventana de configuración

        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setRotationSpeed(3);
        flyCam.setMoveSpeed(10);

        cam.setLocation(new Vector3f(0, 0, 18));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        setDisplayStatView(false);

        // Crear un piso
        Quad floorQuad = new Quad(50, 50);
        floor = new Geometry("Floor", floorQuad);
        Material floorMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMaterial.setColor("Diffuse", ColorRGBA.Gray);
        floor.setMaterial(floorMaterial);
        floor.rotate(-90 * (float) Math.PI / 180, 0, 0); // Rotar para que sea horizontal
        floor.setLocalTranslation(-25, -1.5f, 25); // Posicionar en el suelo
        rootNode.attachChild(floor);

        piezas = new Piezas(assetManager);

        // Crear el nodo principal
        mainNode = new Node("MainNode");

        // Adjuntar todas las figuras al nodo principal
        crearRuedas();
        crearCabina();
        crearCabina2();
        crearEscape();
        crearTapas();
        crearTecho();
        crearHumo();
        crearFrente();
        crearVentana();

        // Adjuntar el nodo principal al rootNode
        rootNode.attachChild(mainNode);

        // Luz ambiental básica
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.2f));
        rootNode.addLight(ambient);

        // Configurar una luz direccional
        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-4, -6, -3).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(sun);

        // Habilitar sombras
        DirectionalLightShadowRenderer shadowRenderer = new DirectionalLightShadowRenderer(assetManager, 8000, 3);
        shadowRenderer.setLight(sun);
        viewPort.addProcessor(shadowRenderer);

        // Asegurarse de que los objetos sean renderizados para las sombras
        floor.setShadowMode(RenderQueue.ShadowMode.Receive);
        mainNode.setShadowMode(RenderQueue.ShadowMode.Cast);

        initKeys();
    }

    private void initKeys() {
        // Mapeo de teclas
        inputManager.addMapping("RotateLeftX", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("RotateRightX", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("RotateSunUpY", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("RotateSunDownY", new KeyTrigger(KeyInput.KEY_F));

        // Asociar ActionListener a las teclas
        inputManager.addListener(actionListener, "RotateLeftX", "RotateRightX", "RotateSunUpY", "RotateSunDownY");

        // Asociar AnalogListener para aplicar cambios continuos mientras las teclas están sostenidas
        inputManager.addListener(analogListener, "RotateLeftX", "RotateRightX", "RotateSunUpY", "RotateSunDownY");
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            // Aquí no necesitamos implementar nada porque el control continuo lo maneja el AnalogListener
        }
    };

    private AnalogListener analogListener = new AnalogListener() {
        private final float rotationSpeed = FastMath.PI / 4f; // Velocidad de rotación (en radianes por segundo)
        private final float sunSpeed = FastMath.PI / 2f; // Velocidad del movimiento del sol (en radianes por segundo)

        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("RotateLeftX")) {
                // Rotar el modelo hacia la izquierda en el eje Y
                mainNode.rotate(0, rotationSpeed * tpf, 0);
            }

            if (name.equals("RotateRightX")) {
                // Rotar el modelo hacia la derecha en el eje Y
                mainNode.rotate(0, -rotationSpeed * tpf, 0);
            }

            if (name.equals("RotateSunUpY")) {
                // Mover el sol en sentido horario (simulando salida del sol)
                updateSunPosition(sunSpeed * tpf);
            }

            if (name.equals("RotateSunDownY")) {
                // Mover el sol en sentido antihorario (simulando puesta del sol)
                updateSunPosition(-sunSpeed * tpf);
            }
        }

        private void updateSunPosition(float deltaY) {
            // Actualizar la altura del sol
            currentSunHeight += deltaY;

            // Limitar la altura máxima y mínima del sol (opcional)
            currentSunHeight = FastMath.clamp(currentSunHeight, -10, 10);

            // Mantener la dirección horizontal constante (por ejemplo, en X y Z)
            float x = -4; // Dirección horizontal fija en X
            float z = -3; // Dirección horizontal fija en Z

            // Actualizar la dirección de la luz direccional
            sun.setDirection(new Vector3f(x, currentSunHeight, z).normalizeLocal());
        }

    };

    public void crearRuedas() {
        float spacingX = 7.0f; // Separación en X (ancho del vehículo)
        float spacingZ = 5.2f; // Separación en Z (distancia entre ejes)

        for (int i = 1; i <= 4; i++) {
            geoms[i] = piezas.createRuedas();
            pivots[i] = new Node("pivot" + i);
            pivots[i].attachChild(geoms[i]);

            // Posicionar las ruedas
            float posX = (i % 2 == 0) ? spacingX / 2 : -spacingX / 2;
            float posZ = (i <= 2) ? spacingZ / 2 : -spacingZ / 2;

            pivots[i].setLocalTranslation(posX, 0, posZ);
            mainNode.attachChild(pivots[i]); // Adjuntar al nodo principal
        }
    }

    public void crearCabina() {
        geoms[5] = piezas.createCabina();
        pivots[5] = new Node("pivot5");
        pivots[5].attachChild(geoms[5]);

        pivots[5].setLocalTranslation(-2, 2F, 0);

        mainNode.attachChild(pivots[5]); // Adjuntar al nodo principal
    }

    public void crearCabina2() {
        geoms[6] = piezas.createCabina2();
        pivots[6] = new Node("pivot6");
        pivots[6].attachChild(geoms[6]);

        pivots[6].setLocalTranslation(2, 3.7F, 0);

        mainNode.attachChild(pivots[6]); // Adjuntar al nodo principal
    }

    public void crearEscape() {
        geoms[7] = piezas.createEscape();
        pivots[7] = new Node("pivot7");
        pivots[7].attachChild(geoms[7]);

        pivots[7].setLocalTranslation(-2.5F, 5F, 0);

        mainNode.attachChild(pivots[7]); // Adjuntar al nodo principal

        geoms[8] = piezas.createEscape2();
        pivots[8] = new Node("pivot8");
        pivots[8].attachChild(geoms[8]);

        pivots[8].setLocalTranslation(-4F, 5F, 0);

        mainNode.attachChild(pivots[8]); // Adjuntar al nodo principal
    }

    public void crearTapas() {
        geoms[9] = piezas.createTapa();
        pivots[9] = new Node("pivot9");
        pivots[9].attachChild(geoms[9]);

        pivots[9].setLocalTranslation(-2.5F, 6.335F, -0.53F);
        mainNode.attachChild(pivots[9]); // Adjuntar al nodo principal

        geoms[10] = piezas.createTapa();
        pivots[10] = new Node("pivot10");
        pivots[10].attachChild(geoms[10]);

        pivots[10].setLocalTranslation(-4F, 5.475F, -0.53F);
        mainNode.attachChild(pivots[10]); // Adjuntar al nodo principal
    }

    public void crearTecho() {
        geoms[11] = piezas.createTecho();
        pivots[11] = new Node("pivot11");
        pivots[11].attachChild(geoms[11]);

        pivots[11].setLocalTranslation(2F, 7.7F, 0);
        mainNode.attachChild(pivots[11]); // Adjuntar al nodo principal
    }

    public void crearHumo() {
        float spacingY = 7f;
        float Yspacing = 1.3f;

        float spacingX = -2.5f;
        float Xspacing = 0.25f;

        for (int i = 12; i < 16; i++) {
            geoms[i] = piezas.createHumo();
            pivots[i] = new Node("pivot" + i);
            pivots[i].attachChild(geoms[i]);
            spacingY = spacingY + Yspacing;
            spacingX = spacingX + Xspacing;
            pivots[i].setLocalTranslation(spacingX, spacingY, 0);
            mainNode.attachChild(pivots[i]); // Adjuntar al nodo principal
        }

        float spacingY2 = 6f;
        float Yspacing2 = 1.3f;

        float spacingX2 = -4f;
        float Xspacing2 = 0.25f;

        for (int i = 16; i < 19; i++) {
            geoms[i] = piezas.createHumo();
            pivots[i] = new Node("pivot" + i);
            pivots[i].attachChild(geoms[i]);
            spacingY2 = spacingY2 + Yspacing2;
            spacingX2 = spacingX2 + Xspacing2;
            pivots[i].setLocalTranslation(spacingX2, spacingY2, 0);
            mainNode.attachChild(pivots[i]); // Adjuntar al nodo principal
        }
    }

    public void crearFrente() {
        geoms[19] = piezas.createTriangularPrism();
        pivots[19] = new Node("pivot19");
        pivots[19].attachChild(geoms[19]);

        pivots[19].setLocalTranslation(-5.56F, -0.2F, 2F);
        mainNode.attachChild(pivots[19]); // Adjuntar al nodo principal
    }

    public void crearVentana() {
        geoms[20] = piezas.createWindow();
        pivots[20] = new Node("pivot20");
        pivots[20].attachChild(geoms[20]);
        pivots[20].setLocalTranslation(2.15F, 5.5F, 2.6F);
        mainNode.attachChild(pivots[20]); // Adjuntar al nodo principal

        geoms[21] = piezas.createWindow();
        pivots[21] = new Node("pivot21");
        pivots[21].attachChild(geoms[21]);
        pivots[21].setLocalTranslation(2.15F, 5.5F, -2.8F);
        mainNode.attachChild(pivots[21]); // Adjuntar al nodo principal

        geoms[22] = piezas.createTexturedWindow();
        pivots[22] = new Node("pivot22");
        pivots[22].attachChild(geoms[22]);
        pivots[22].setLocalTranslation(-1.1F, 5.8F, 0);
        mainNode.attachChild(pivots[22]); // Adjuntar al nodo principal
    }

}
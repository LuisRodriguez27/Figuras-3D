package com.mycompany.train;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import java.awt.*;

public class Figura extends SimpleApplication {

    private Piezas piezas;

    private Node[] nodes = new Node[10];
    private Geometry[] geoms = new Geometry[40];
    private Node[] pivots = new Node[40];


    public static void main(String[] args) {
        Figura app = new Figura();

        // Configuración de la ventana
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Figuras 3D ");

        // Tamaño de ventana personalizado
        int windowWidth = 1366;
        int windowHeight =768;
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
        // Configurar fondo blanco
//        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

//        flyCam.setEnabled(false);
        flyCam.setRotationSpeed(3);
        flyCam.setMoveSpeed(10);

        cam.setLocation(new Vector3f(0, 0, 18));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        setDisplayStatView(false);

        setupLighting();
//        setupCardinalLights();

        piezas = new Piezas(assetManager);

        crearRuedas();
        crearCabina();
        crearCabina2();
        crearEscape();
        crearTapas();
        crearTecho();
        crearHumo();
        crearFrente();
        crearVentana();
    }

    private void setupLighting() {
        // Luz direccional suave
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(0.3f));
        rootNode.addLight(sun);

        // Luz ambiental básica
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.03f));
        rootNode.addLight(ambient);
    }

    private void setupCardinalLights() {
        // Crear una luz puntual para cada punto cardinal
        PointLight northLight = new PointLight();
        northLight.setPosition(new Vector3f(0, 2, 5)); // Norte
        northLight.setColor(ColorRGBA.Red.mult(1.5f)); // Color rojo
        northLight.setRadius(10); // Radio de influencia
        rootNode.addLight(northLight);

        PointLight southLight = new PointLight();
        southLight.setPosition(new Vector3f(0, 2, -5)); // Sur
        southLight.setColor(ColorRGBA.Blue.mult(1.5f)); // Color azul
        southLight.setRadius(10);
        rootNode.addLight(southLight);

        PointLight eastLight = new PointLight();
        eastLight.setPosition(new Vector3f(5, 2, 0)); // Este
        eastLight.setColor(ColorRGBA.Green.mult(1.5f)); // Color verde
        eastLight.setRadius(10);
        rootNode.addLight(eastLight);

        PointLight westLight = new PointLight();
        westLight.setPosition(new Vector3f(-5, 2, 0)); // Oeste
        westLight.setColor(ColorRGBA.Yellow.mult(1.5f)); // Color amarillo
        westLight.setRadius(10);
        rootNode.addLight(westLight);
    }

    public void crearRuedas(){
        float spacingX = 7.0f; // Separación en X (ancho del vehículo)
        float spacingZ = 5.2f; // Separación en Z (distancia entre ejes)

        for (int i = 1; i <= 4; i++) {
            geoms[i] = piezas.createRuedas();
//            geoms[i] = piezas.createRuedas2();
            pivots[i] = new Node("pivot" + i);
            pivots[i].attachChild(geoms[i]);

            // Posicionar las ruedas
            float posX = (i % 2 == 0) ? spacingX / 2 : -spacingX / 2;
            float posZ = (i <= 2) ? spacingZ / 2 : -spacingZ / 2;

            pivots[i].setLocalTranslation(posX, 0, posZ);
            rootNode.attachChild(pivots[i]);
        }
    }

    public void crearCabina(){
        geoms[5] = piezas.createCabina();
        pivots[5] = new Node("pivot5");
        pivots[5].attachChild(geoms[5]);

        pivots[5].setLocalTranslation(-2, 2F, 0);

        rootNode.attachChild(pivots[5]);
    }

    public void crearCabina2(){
        geoms[6] = piezas.createCabina2();
        pivots[6] = new Node("pivot6");
        pivots[6].attachChild(geoms[6]);

        pivots[6].setLocalTranslation(2, 3.7F, 0);

        rootNode.attachChild(pivots[6]);
    }

    public void crearEscape(){
        geoms[7] = piezas.createEscape();
        pivots[7] = new Node("pivot7");
        pivots[7].attachChild(geoms[7]);

        pivots[7].setLocalTranslation(-2.5F, 5F, 0);

        rootNode.attachChild(pivots[7]);


        geoms[8] = piezas.createEscape2();
        pivots[8] = new Node("pivot7");
        pivots[8].attachChild(geoms[8]);

        pivots[8].setLocalTranslation(-4F, 5F, 0);

        rootNode.attachChild(pivots[8]);
    }

    public void crearTapas(){
        geoms[8] = piezas.createTapa();
        pivots[8] = new Node("pivot8");
        pivots[8].attachChild(geoms[8]);

        pivots[8].setLocalTranslation(-2.5F, 6.335F, -0.53F);
        rootNode.attachChild(pivots[8]);


        geoms[9] = piezas.createTapa();
        pivots[9] = new Node("pivot9");
        pivots[9].attachChild(geoms[9]);

        pivots[9].setLocalTranslation(-4F, 5.475F, -0.53F);
        rootNode.attachChild(pivots[9]);
    }

    public void crearTecho(){
        geoms[10] = piezas.createTecho();
        pivots[10] = new Node("pivot10");
        pivots[10].attachChild(geoms[10]);

        pivots[10].setLocalTranslation(2F, 7.7F, 0);
        rootNode.attachChild(pivots[10]);
    }

    public void crearHumo(){
        float spacingY = 7f;
        float Yspacing = 1.3f;

        float spacingX = -2.5f;
        float Xspacing = 0.25f;

        for(int i = 11; i < 15; i++){
            geoms[i] = piezas.createHumo();
            pivots[i] = new Node("pivot11");
            pivots[i].attachChild(geoms[i]);
            spacingY = spacingY + Yspacing;
            spacingX = spacingX + Xspacing;
            pivots[i].setLocalTranslation(spacingX, spacingY, 0);
            rootNode.attachChild(pivots[i]);
        }

        float spacingY2 = 6f;
        float Yspacing2 = 1.3f;

        float spacingX2 = -4f;
        float Xspacing2 = 0.25f;

        for(int i = 15; i < 18; i++){
            geoms[i] = piezas.createHumo();
            pivots[i] = new Node("pivot11");
            pivots[i].attachChild(geoms[i]);
            spacingY2 = spacingY2 + Yspacing2;
            spacingX2 = spacingX2 + Xspacing2;
            pivots[i].setLocalTranslation(spacingX2, spacingY2, 0);
            rootNode.attachChild(pivots[i]);
        }
    }

    public void crearFrente(){
        geoms[18] = piezas.createTriangularPrism();
        pivots[18] = new Node("pivot18");
        pivots[18].attachChild(geoms[18]);

        pivots[18].setLocalTranslation(-5.56F, -0.2F, 2F);
        rootNode.attachChild(pivots[18]);
    }

    public void crearVentana(){
        geoms[19] = piezas.createWindow();
        pivots[19] = new Node("pivot19");
        pivots[19].attachChild(geoms[19]);
        pivots[19].setLocalTranslation(2.15F, 5.5F, 2.6F);
        rootNode.attachChild(pivots[19]);

        geoms[20] = piezas.createWindow();
        pivots[20] = new Node("pivot20");
        pivots[20].attachChild(geoms[20]);
        pivots[20].setLocalTranslation(2.15F, 5.5F, -2.8F);
        rootNode.attachChild(pivots[20]);

//        geoms[21] = piezas.createPrincipalWindow();
//        pivots[21] = new Node("pivot21");
//        pivots[21].attachChild(geoms[21]);
//        pivots[21].setLocalTranslation(-1.1F, 5.8F, 0);
//        rootNode.attachChild(pivots[21]);

        geoms[21] = piezas.createTexturedWindow();
        pivots[21] = new Node("pivot21");
        pivots[21].attachChild(geoms[21]);
        pivots[21].setLocalTranslation(-1.1F, 5.8F, 0);
        rootNode.attachChild(pivots[21]);
    }

}

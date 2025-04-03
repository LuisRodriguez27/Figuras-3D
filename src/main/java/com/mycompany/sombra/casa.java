package com.mycompany.sombra;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;

import java.awt.*;


public class casa extends SimpleApplication {
    @Override
    public void simpleInitApp() {
        flyCam.setRotationSpeed(3);
        flyCam.setMoveSpeed(10);

        // Desactivar estadísticas en pantalla
        setDisplayStatView(false);
        setDisplayFps(false);

        // Crear un piso
        Quad floorQuad = new Quad(10, 10);
        Geometry floor = new Geometry("Floor", floorQuad);
        Material floorMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMaterial.setColor("Diffuse", ColorRGBA.Gray);
        floor.setMaterial(floorMaterial);
        floor.rotate(-90 * (float) Math.PI / 180, 0, 0); // Rotar para que sea horizontal
        floor.setLocalTranslation(-5, 0, 5); // Posicionar en el suelo
        rootNode.attachChild(floor);

        // Cargar el modelo del edificio desde Blender
        Spatial building = assetManager.loadModel("Models/untitled.obj");
        building.setLocalTranslation(0, 0, 0); // Posicionar el edificio
        building.rotate(0, FastMath.PI , 0);
        rootNode.attachChild(building);

        // Luz ambiental básica
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.2f));
        rootNode.addLight(ambient);

        // Configurar una luz direccional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-4, -6, -3).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(sun);

        // Habilitar sombras
        DirectionalLightShadowRenderer shadowRenderer = new DirectionalLightShadowRenderer(assetManager, 5000, 3);
        shadowRenderer.setLight(sun);
        viewPort.addProcessor(shadowRenderer);

        // Asegurarse de que los objetos sean renderizados para las sombras
        floor.setShadowMode(RenderQueue.ShadowMode.Receive);
        building.setShadowMode(RenderQueue.ShadowMode.Cast);
    }

    public static void main(String[] args) {
        casa app = new casa();

        // Configuración de la ventana
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Casa con Sombra");

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
}

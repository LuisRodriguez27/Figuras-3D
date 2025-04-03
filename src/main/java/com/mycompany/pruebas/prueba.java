package com.mycompany.pruebas;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;

public class prueba extends SimpleApplication {

    public static void main(String[] args) {
        prueba app = new prueba();
        app.start();
    }

    public Geometry createLitCylinder(AssetManager assetManager) {
        // Crear el cilindro básico
        Cylinder cylinder = new Cylinder(30, 20, 1.5f, 0.5f, true);
        Geometry geom = new Geometry("LitCylinder", cylinder);

        // Crear material con iluminación
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        // Habilitar el uso de colores del material
        material.setBoolean("UseMaterialColors", true);

        // Configurar colores del material
        material.setColor("Diffuse", ColorRGBA.Gray); // Color difuso (base)
        material.setColor("Ambient", ColorRGBA.Gray); // Color ambiente
        material.setColor("Specular", ColorRGBA.White); // Color especular (reflejos brillantes)

        // Aumentar el brillo
        material.setFloat("Shininess", 128f); // Reflejos más concentrados

        // Cargar una textura (opcional)
        Texture texture = assetManager.loadTexture("assets/textures/metal.png");
        material.setTexture("DiffuseMap", texture);

        // Aplicar el material
        geom.setMaterial(material);

        return geom;
    }

    // Metodo para crear un cubo con color sólido e iluminación
    public Geometry createLitCube(AssetManager assetManager) {
        // Crear el cubo básico
        Box box = new Box(1, 1, 1); // Dimensiones del cubo (ancho, alto, profundidad)
        Geometry geom = new Geometry("LitCube", box);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.Green);
        mat.setColor("Ambient", ColorRGBA.Green);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 128f);

        // Aplicar el material
        geom.setMaterial(mat);

        return geom;
    }

    @Override
    public void simpleInitApp() {
        // Crear el cilindro con iluminación
        Geometry litCylinder = createLitCylinder(assetManager);
        litCylinder.setLocalTranslation(0, 2, 0);
        rootNode.attachChild(litCylinder);

        // Crear el cubo con iluminación
        Geometry litCube = createLitCube(assetManager);
        litCube.setLocalTranslation(2, 2, 0); // Posicionar el cubo
        rootNode.attachChild(litCube);

        // Agregar una luz direccional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(sun);

        // Agregar una luz ambiente
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(0.2f));
        rootNode.addLight(ambientLight);

        flyCam.setRotationSpeed(3);
        flyCam.setMoveSpeed(10);
    }

}

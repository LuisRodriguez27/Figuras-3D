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

public class Pruebas extends SimpleApplication {

    private Node cubeGroup; // Nodo que agrupa cubo y borde
    private Geometry cubeGeometry;
    private Geometry currentWireframe;

    public static void main(String[] args) {
        Pruebas app = new Pruebas();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Configurar fondo blanco
        viewPort.setBackgroundColor(ColorRGBA.White);

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

        // Colocar todo el grupo en una posición visible
        cubeGroup.setLocalTranslation(0, 0, -5);

        // Añadir grupo a la escena
        rootNode.attachChild(cubeGroup);

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

        cam.setLocation(new Vector3f(0, 2, 5));
        cam.lookAt(cubeGroup.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("pick target")) {
                CollisionResults results = new CollisionResults();
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                Ray ray = new Ray(click3d, dir);
                rootNode.collideWith(ray, results);

                if (results.size() > 0) {
                    Geometry target = results.getClosestCollision().getGeometry();

                    if (target.getName().equals("Cubo Azul")) {
                        if (currentWireframe == null) {
                            currentWireframe = createCubeEdges((Box) cubeGeometry.getMesh());
                            currentWireframe.setMaterial(createWireframeMaterial());
                            cubeGroup.attachChild(currentWireframe); // Ahora están unidos
                        }

                        // Rotar TODO el grupo (cubo + borde)
                        cubeGroup.rotate(0, -intensity, 0);

                        // Ejemplo de mover todo el grupo
//                         cubeGroup.move(0.1f, 0, 0); // Descomenta si quieres moverlo a la derecha

                        // Ejemplo de escalar todo el grupo
//                         cubeGroup.setLocalScale(1.5f); // Descomenta si quieres escalar
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

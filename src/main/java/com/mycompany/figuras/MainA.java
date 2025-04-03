package com.mycompany.figuras;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;

import java.awt.*;
import java.awt.Rectangle;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class MainA extends SimpleApplication {

    private Geometry[] geoms = new Geometry[5];
    private boolean[] isSelected = new boolean[5];
    private boolean isDragging = false;
    private Vector2f lastMousePos = new Vector2f();
    private Node[] pivots = new Node[5];

    private FigureFactory figureFactory;

    public static void main(String[] args) {
        MainA app = new MainA();

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

//        BUENOOOO

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

        flyCam.setEnabled(false);

        cam.setLocation(new Vector3f(0, 0, 18));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        setDisplayStatView(false);

        figureFactory = new FigureFactory(assetManager);

        // Crear las figuras usando la nueva clase
        geoms[0] = figureFactory.createSphere();
        geoms[1] = figureFactory.createPyramid();
        geoms[2] = figureFactory.createCylinder();
        geoms[3] = figureFactory.createTorus();
        geoms[4] = figureFactory.createCube();

        // Posicionar las figuras
        float spacing = 5.0f;
        for (int i = 0; i < geoms.length; i++) {
            pivots[i] = new Node("pivot" + i);
            pivots[i].attachChild(geoms[i]);
            pivots[i].setLocalTranslation((i - 2) * spacing, 0, 0);
            rootNode.attachChild(pivots[i]);
        }

        // Configurar iluminación
        setupLighting();

        // Configurar controles de teclado y ratón
        initKeys();
        initMouse();

        // Crear contenedores para la GUI
        String[] titles = {"Rotacion", "Escalamiento"};
        int initialY = cam.getHeight() - 10;
        int containerHeight = 150;

        for (int i = 0; i < titles.length; i++) {
            Container window = new Container();
            guiNode.attachChild(window);
            createSlidersWithLabels(window, initialY - i * containerHeight, titles[i]);
        }
    }


    private void setupLighting() {
//        DirectionalLight sun = new DirectionalLight();
//        sun.setDirection(new Vector3f(1, -1, -2).normalizeLocal());
//        sun.setColor(ColorRGBA.White);
//        rootNode.addLight(sun);
//
//        AmbientLight ambient = new AmbientLight();
//        ambient.setColor(ColorRGBA.White.mult(0.2f));
//        rootNode.addLight(ambient);

        // Luz direccional suave
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(0.15f));
        rootNode.addLight(sun);

        // Luz ambiental básica
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(ambient);
    }

    private void initKeys() {
        inputManager.addMapping("RotateLeftX", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("RotateRightX", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("RotateUpY", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("RotateDownY", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("ScaleUp", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("ScaleDown", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("SkewX", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("SkewY", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("SkewXNeg", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("SkewYNeg", new KeyTrigger(KeyInput.KEY_C));

        inputManager.addListener(actionListener, "RotateLeftX", "RotateRightX", "RotateUpY", "RotateDownY",
                "ScaleUp", "ScaleDown", "SkewX", "SkewY", "SkewXNeg", "SkewYNeg");

        // Agregar mapeo para la tecla "V"
        inputManager.addMapping("RotateCamera", new KeyTrigger(KeyInput.KEY_V));
        inputManager.addListener(actionListener, "RotateCamera");

        inputManager.addMapping("RotateCamera2", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(actionListener, "RotateCamera2");

        // Agregar mapeos para las teclas de flecha arriba y abajo
        inputManager.addMapping("ZoomIn", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("ZoomOut", new KeyTrigger(KeyInput.KEY_DOWN));

        // Agregar los nuevos mapeos al listener
        inputManager.addListener(actionListener, "ZoomIn", "ZoomOut");
    }

    private void initMouse() {
        inputManager.addMapping("Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Drag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Select");
        inputManager.addListener(analogListener, "Drag");
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("RotateCamera") && isPressed) {
                rotateCameraAroundOrigin(45); // Rotar la cámara 45 grados
                return;
            }

            if (name.equals("RotateCamera2") && isPressed) {
                rotateCameraAroundOrigin2(45); // Rotar la cámara 45 grados
                return;
            }

            if (name.equals("ZoomIn") && isPressed) {
                zoomCamera(1.0f); // Acercarse
            } else if (name.equals("ZoomOut") && isPressed) {
                zoomCamera(-1.0f); // Alejarse
            }

            if (name.equals("Select") && isPressed) {
                selectFigure();
                isDragging = isSelected[getSelectedIndex()];
                lastMousePos.set(inputManager.getCursorPosition());
                return;
            }
            if (name.equals("Select") && !isPressed) {
                isDragging = false;
                return;
            }

            int selectedIndex = getSelectedIndex();
            if (!isPressed || selectedIndex == -1) return; // No hacer nada si no hay figura seleccionada

            Geometry geom = geoms[selectedIndex];
            Node pivot = pivots[selectedIndex];

            switch (name) {
                case "RotateLeftX":
                    pivot.rotate(0, -0.5f, 0); // Aumentar velocidad de rotación
                    break;
                case "RotateRightX":
                    pivot.rotate(0, 0.5f, 0); // Aumentar velocidad de rotación
                    break;
                case "RotateUpY":
                    pivot.rotate(0.5f, 0, 0); // Aumentar velocidad de rotación
                    break;
                case "RotateDownY":
                    pivot.rotate(-0.5f, 0, 0); // Aumentar velocidad de rotación
                    break;
                case "ScaleUp":
                    pivot.setLocalScale(pivot.getLocalScale().mult(1.1f));
                    break;
                case "ScaleDown":
                    pivot.setLocalScale(pivot.getLocalScale().mult(0.9f));
                    break;
                case "SkewX":
                    applySkew(geom, 0.2f, 0, false, true);
                    break;
                case "SkewY":
                    applySkew(geom, 0, 0.2f, true, false);
                    break;
                case "SkewXNeg":
                    applySkew(geom, -0.2f, 0, false, true);
                    break;
                case "SkewYNeg":
                    applySkew(geom, 0, -0.2f, true, false);
                    break;
            }
        }
    };

    private void zoomCamera(float direction) {
        // Factor de zoom: cuánto se mueve la cámara
        float zoomFactor = 1.0f;

        // Obtener la dirección en la que está mirando la cámara
        Vector3f camDir = cam.getDirection().normalize();

        // Calcular el nuevo desplazamiento
        Vector3f move = camDir.mult(zoomFactor * direction);

        // Actualizar la posición de la cámara
        cam.setLocation(cam.getLocation().add(move));

        System.out.println("New camera position: " + cam.getLocation());
    }

    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Drag") && isDragging) {
                int selectedIndex = getSelectedIndex();
                if (selectedIndex == -1) return; // No hacer nada si no hay figura seleccionada

                Vector2f cursorPosition = inputManager.getCursorPosition();
                Vector2f diff = cursorPosition.subtract(lastMousePos);
                // Ajustar el factor de movimiento para mayor precisión
                Vector3f worldPos = getWorldPosition(cursorPosition, geoms[selectedIndex]);
                if (worldPos != null) {
                    Vector3f currentPos = pivots[selectedIndex].getWorldTranslation();
                    Vector3f diff3d = new Vector3f(diff.x * 0.05f, diff.y * 0.05f, 0); // Movimiento en X y Y
                    pivots[selectedIndex].setLocalTranslation(currentPos.add(diff3d));
                }
                lastMousePos.set(cursorPosition);
            }
        }
    };

    // Metodo para rotar la cámara alrededor del origen
    private void rotateCameraAroundOrigin(float degrees) {
        System.out.println("Rotating camera by " + degrees + " degrees.");

        // Convertir grados a radianes
        float angle = degrees * FastMath.DEG_TO_RAD;

        // Obtener la posición actual de la cámara
        Vector3f camPos = cam.getLocation().clone();
        System.out.println("Current camera position: " + camPos);

        // Calcular la nueva posición usando una rotación en el plano XZ
        float radius = camPos.length(); // Distancia desde el origen
        float currentAngle = FastMath.atan2(camPos.z, camPos.x); // Ángulo actual en el plano XZ
        float newAngle = currentAngle + angle; // Nuevo ángulo

        // Calcular la nueva posición
        float newX = radius * FastMath.cos(newAngle);
        float newZ = radius * FastMath.sin(newAngle);

        // Mantener la posición en el eje Y constante
        float newY = camPos.y;

        // Actualizar la posición de la cámara
        Vector3f newCamPos = new Vector3f(newX, newY, newZ);
        cam.setLocation(newCamPos);
        System.out.println("New camera position: " + newCamPos);

        // Asegurarse de que la cámara siempre apunte al origen
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y); // La cámara mira hacia abajo (eje Y)
    }

    private void applySkew(Geometry geom, float dx, float dy, boolean fixX, boolean fixY) {
        Mesh mesh = geom.getMesh();
        VertexBuffer posBuffer = mesh.getBuffer(VertexBuffer.Type.Position);
        FloatBuffer fb = (FloatBuffer) posBuffer.getData();
        fb.rewind();

        FloatBuffer newBuffer = BufferUtils.createFloatBuffer(fb.capacity());

        float fixedXValue = -1;
        float fixedYValue = -1;

        while (fb.hasRemaining()) {
            float x = fb.get();
            float y = fb.get();
            float z = fb.get();

            float newX = x;
            if (dx != 0) {
                if (fixY) {
                    newX += dx * (y - fixedYValue);
                } else {
                    newX += dx * y;
                }
            }

            float newY = y;
            if (dy != 0) {
                if (fixX) {
                    newY += dy * (x - fixedXValue);
                } else {
                    newY += dy * x;
                }
            }

            newBuffer.put(newX).put(newY).put(z);
        }

        newBuffer.flip();
        posBuffer.updateData(newBuffer);
        mesh.updateBound();
        geom.updateModelBound();
    }

    private void selectFigure() {
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(click2d, 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        rootNode.collideWith(ray, results);

        // Deseleccionar todas las figuras primero
        for (int i = 0; i < geoms.length; i++) {
            if (isSelected[i]) {
                isSelected[i] = false;
                Geometry wireframe = findWireframe(pivots[i]);
                if (wireframe != null) {
                    figureFactory.removeWireframe(pivots[i], wireframe);
                }
            }
        }

        // Verificar si hay colisiones
        if (results.size() > 0) {
            Spatial target = results.getClosestCollision().getGeometry();
            // Seleccionar la figura clicada
            for (int i = 0; i < geoms.length; i++) {
                if (target.equals(geoms[i])) {
                    isSelected[i] = true;
                    figureFactory.addWireframe(pivots[i], geoms[i]);
                    break;
                }
            }
        }
    }

    // Metodo auxiliar para encontrar el wireframe
    private Geometry findWireframe(Node node) {
        for (Spatial child : node.getChildren()) {
            if (child.getName() != null && child.getName().startsWith("Wireframe")) {
                return (Geometry) child;
            }
        }
        return null;
    }

    private ColorRGBA getDefaultColor(int index) {
        switch (index) {
            case 0:
                return ColorRGBA.Blue;
            case 1:
                return ColorRGBA.Green;
            case 2:
                return ColorRGBA.Yellow;
            case 3:
                return ColorRGBA.Red;
            case 4:
                return ColorRGBA.Pink;
            default:
                return ColorRGBA.White;
        }
    }

    private int getSelectedIndex() {
        for (int i = 0; i < isSelected.length; i++) {
            if (isSelected[i]) return i;
        }
        return -1;
    }

    private Vector3f getWorldPosition(Vector2f cursorPosition, Geometry geom) {
        Vector3f origin = cam.getWorldCoordinates(cursorPosition, 0.0f);
        Vector3f direction = cam.getWorldCoordinates(cursorPosition, 0.3f).subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);

        CollisionResults results = new CollisionResults();
        geom.collideWith(ray, results);

        if (results.size() > 0) {
            return results.getClosestCollision().getContactPoint();
        }
        return null;
    }

    // Mapa para almacenar las referencias de los sliders
    private Map<String, VersionedReference<Double>> sliderRefs = new HashMap<>();

    private void createSlidersWithLabels(Container window, int y, String title) {
        if (title != "Escalamiento") {
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
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) return; // No figura seleccionada

        Geometry geom = geoms[selectedIndex];
        Node pivot = pivots[selectedIndex];

        for (Map.Entry<String, VersionedReference<Double>> entry : sliderRefs.entrySet()) {
            String sliderKey = entry.getKey();
            VersionedReference<Double> ref = entry.getValue();

            if (ref.update()) {
                double value = ref.get();
                float sliderValue = (float) (value / 100.0); // Normalizar valor entre 0 y 1

                switch (sliderKey) {
                    case "Rotacion_X":
                        pivot.setLocalRotation(new Quaternion().fromAngles((sliderValue - 50) * FastMath.PI, 0, 0));
                        break;
                    case "Rotacion_Y":
                        pivot.setLocalRotation(new Quaternion().fromAngles(0, (sliderValue - 50) * FastMath.PI, 0));
                        break;
                    case "Rotacion_Z":
                        pivot.setLocalRotation(new Quaternion().fromAngles(0, 0, (sliderValue - 50) * FastMath.PI));
                        break;

                    case "Escalamiento_General":
                        float scale = 0.5f + sliderValue; // Escala entre 0.5 y 1.5
                        pivot.setLocalScale(scale);
                        break;
                }
            }
        }
    }

    // Metodo para rotar la cámara alrededor del origen
    private void rotateCameraAroundOrigin2(float degrees) {
        // Convertir grados a radianes
        float angle = degrees * FastMath.DEG_TO_RAD;

        // Obtener la posición actual de la cámara
        Vector3f camPos = cam.getLocation().clone();

        // Calcular la nueva posición usando una rotación en el plano XY
        float radius = camPos.length(); // Distancia desde el origen
        float currentAngle = FastMath.atan2(camPos.y, camPos.x); // Ángulo actual
        float newAngle = currentAngle + angle; // Nuevo ángulo

        // Calcular la nueva posición
        float newX = radius * FastMath.cos(newAngle);
        float newY = radius * FastMath.sin(newAngle);

        // Actualizar la posición de la cámara
        cam.setLocation(new Vector3f(newX, newY, camPos.z));

        // Asegurarse de que la cámara siempre apunte al origen
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
    }
}


//casi final

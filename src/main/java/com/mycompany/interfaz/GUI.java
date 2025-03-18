package com.mycompany.interfaz;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.app.SimpleApplication;
import com.mycompany.figuras.FiguraBase;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;

import java.util.HashMap;
import java.util.Map;

public class GUI {
    private SimpleApplication app;
    private Node rootNode;
    private Map<String, VersionedReference<Double>> sliderRefs = new HashMap<>();
    private Geometry selectedGeometry = null; // Figura seleccionada actualmente
    private Container slidersContainer; // Contenedor para los sliders

    // Mapa para asociar Geometry con FiguraBase
    private Map<Geometry, FiguraBase> geometryToFiguraMap = new HashMap<>();

    public GUI(SimpleApplication app, Node rootNode) {
        this.app = app;
        this.rootNode = rootNode;
        setupLemur();
        setupMouseSelection();
    }

    /**
     * Configura Lemur para la GUI
     */
    private void setupLemur() {
        GuiGlobals.initialize(app);
        try {
            BaseStyles.loadGlassStyle();
            GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        } catch (Exception e) {
            System.err.println("Error al cargar el estilo 'glass': " + e.getMessage());
            GuiGlobals.getInstance().getStyles().setDefaultStyle("default");
        }
    }

    /**
     * Configura el listener para seleccionar figuras con clics del mouse
     */
    private void setupMouseSelection() {
        app.getInputManager().addMapping("select figure", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(analogListener, "select figure");
    }

    /**
     * Listener para detectar clics del mouse y seleccionar figuras
     */
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("select figure")) {
                CollisionResults results = new CollisionResults();
                Vector2f click2d = app.getInputManager().getCursorPosition();
                Vector3f click3d = app.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = app.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                Ray ray = new Ray(click3d, dir);

                rootNode.collideWith(ray, results);

                if (results.size() > 0) {
                    Geometry target = results.getClosestCollision().getGeometry();

                    // Si ya hay una figura seleccionada, deseleccionarla
                    if (selectedGeometry != null) {
                        FiguraBase figuraSeleccionada = obtenerFiguraBaseDesdeGeometry(selectedGeometry);
                        if (figuraSeleccionada != null) {
                            figuraSeleccionada.deseleccionar();
                        }
                    }

                    // Seleccionar la nueva figura
                    selectedGeometry = target;
                    System.out.println("Figura seleccionada: " + selectedGeometry.getName());

                    // Obtener la figura base correspondiente al Geometry
                    FiguraBase nuevaFiguraSeleccionada = obtenerFiguraBaseDesdeGeometry(selectedGeometry);
                    if (nuevaFiguraSeleccionada != null) {
                        nuevaFiguraSeleccionada.seleccionar();

                        // Mostrar los sliders para la figura seleccionada
                        createSlidersForSelectedFigure();

                        // Actualizar los sliders con los valores actuales de la figura
                        updateSlidersForSelectedFigure();
                    }
                }
            }
        }
    };

    /**
     * Crea y muestra los sliders para la figura seleccionada
     */
    public void createSlidersForSelectedFigure() {
        if (slidersContainer != null) {
            app.getGuiNode().detachChild(slidersContainer); // Eliminar sliders anteriores
        }

        slidersContainer = new Container();
        app.getGuiNode().attachChild(slidersContainer);

        String[] titles = {"Rotacion", "Sesgado", "Escalamiento"};
        int initialY = app.getCamera().getHeight() - 10;
        int containerHeight = 150;

        for (int i = 0; i < titles.length; i++) {
            Container window = new Container();
            slidersContainer.addChild(window);
            createSlidersWithLabels(window, initialY - i * containerHeight, titles[i]);
        }

        // Actualizar los sliders con los valores actuales de la figura seleccionada
        updateSlidersForSelectedFigure();
    }

    /**
     * Crea sliders con etiquetas dentro de un contenedor
     */
    private void createSlidersWithLabels(Container window, int y, String title) {
        window.setLocalTranslation(10, y, 0);

        Label titleLabel = new Label(title);
        window.addChild(titleLabel);

        if (!title.equals("Escalamiento")) {
            // Sliders para X, Y, Z
            addSliderWithLabel(window, "Eje X:", title + "_X");
            addSliderWithLabel(window, "Eje Y:", title + "_Y");
            addSliderWithLabel(window, "Eje Z:", title + "_Z");
        } else {
            // Slider general para escalamiento
            addSliderWithLabel(window, "General:", title + "_General");
        }
    }

    /**
     * Agrega un slider con una etiqueta al contenedor
     */
    private void addSliderWithLabel(Container window, String labelText, String sliderKey) {
        Label label = new Label(labelText);
        window.addChild(label);

        Slider slider = new Slider("glass");
        slider.setPreferredSize(new Vector3f(200, 20, 0));
        slider.getModel().setValue(50); // Valor inicial del slider

        VersionedReference<Double> ref = window.addChild(slider).getModel().createReference();
        sliderRefs.put(sliderKey, ref);
    }

    /**
     * Actualiza los sliders con los valores de la figura seleccionada
     */
    private void updateSlidersForSelectedFigure() {
        if (selectedGeometry != null) {
            // Aquí debes obtener las propiedades actuales de la figura (rotación, escala, etc.)
            // y actualizar los valores de los sliders correspondientes
            // Ejemplo:
            // sliderRefs.get("Rotacion_X").updateValue(selectedGeometry.getLocalRotation().getX());
            // sliderRefs.get("Escalamiento_General").updateValue(selectedGeometry.getLocalScale().x);
        }
    }

    /**
     * Aplica las transformaciones basadas en los valores de los sliders
     */
    public void applyTransformations() {
        for (Map.Entry<String, VersionedReference<Double>> entry : sliderRefs.entrySet()) {
            String sliderKey = entry.getKey();
            VersionedReference<Double> ref = entry.getValue();

            if (ref.update()) {
                double value = ref.get();
                System.out.println("Nuevo valor para " + sliderKey + ": " + value);

                // Aquí debes aplicar las transformaciones a la figura seleccionada
                // Ejemplo:
                // if (sliderKey.equals("Rotacion_X")) {
                //     selectedGeometry.rotate((float) value, 0, 0);
                // } else if (sliderKey.equals("Escalamiento_General")) {
                //     selectedGeometry.setLocalScale((float) value);
                // }
            }
        }
    }

    /**
     * Registra una figura en el mapa Geometry -> FiguraBase
     */
    public void registrarFigura(FiguraBase figura) {
        geometryToFiguraMap.put(figura.getGeometry(), figura);
    }

    /**
     * Obtiene la instancia de FiguraBase asociada a un Geometry
     */
    private FiguraBase obtenerFiguraBaseDesdeGeometry(Geometry geometry) {
        return geometryToFiguraMap.get(geometry);
    }
}
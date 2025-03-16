package com.mycompany.interfaz;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.mycompany.figuras.Figuras;

/**
 * Clase para gestionar la interfaz gráfica
 * Esta clase será ampliada en el futuro para incluir labels, sliders y botones
 */
public class GUI implements ActionListener {

    private SimpleApplication app;
    private InputManager inputManager;
    private Node rootNode;
    private Figuras figuras;

    // Constante para la acción de selección
    private static final String SELECT = "Select";

    public GUI(SimpleApplication app) {
        this.app = app;
        this.inputManager = app.getInputManager();
        this.rootNode = app.getRootNode();
    }

    /**
     * Configura el entorno visual básico y el manejo de entrada
     */
    public void configurarEntorno() {
        // Configurar color de fondo
        app.getViewPort().setBackgroundColor(ColorRGBA.White);

        // Configurar cámara
        configurarCamara();

        // Desactivar estadísticas
        app.setDisplayStatView(false);
        app.setDisplayFps(false);

        // Configurar entrada del mouse
        configurarEntradaMouse();
    }

    /**
     * Configura la cámara para una vista general de las figuras
     */
    public void configurarCamara() {
        app.getCamera().setLocation(new Vector3f(0, 3, 12));
        app.getCamera().lookAt(new Vector3f(0, 2, 0), Vector3f.UNIT_Y);
    }

    /**
     * Configura la detección de click de mouse
     */
    private void configurarEntradaMouse() {
        // Limpiar mapeos previos
        inputManager.clearMappings();

        // Asignar botón izquierdo del mouse a la acción "Select"
        inputManager.addMapping(SELECT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // Registrar el listener (esta clase)
        inputManager.addListener(this, SELECT);
    }

    /**
     * Establece la referencia a la clase Figuras
     */
    public void setFiguras(Figuras figuras) {
        this.figuras = figuras;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(SELECT) && !isPressed) {
            // Solo procesamos cuando se suelta el botón para evitar múltiples selecciones
            seleccionarObjetoConMouse();
        }
    }

    /**
     * Selecciona un objeto usando ray casting desde la posición del mouse
     */
    private void seleccionarObjetoConMouse() {
        // Verificamos que tengamos la referencia a figuras
        if (figuras == null) {
            return;
        }

        // Obtenemos la posición actual del cursor
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

        // Creamos el rayo desde la cámara en dirección al cursor
        Ray ray = new Ray(click3d, dir);

        // Preparamos para guardar los resultados de colisión
        CollisionResults results = new CollisionResults();

        // Hacemos el ray casting
        rootNode.collideWith(ray, results);

        // Procesamos los resultados
        if (results.size() > 0) {
            // Tomamos el objeto más cercano
            CollisionResult closest = results.getClosestCollision();
            Geometry target = closest.getGeometry();

            // Si el objeto es una figura (no un wireframe u otro objeto)
            if (!target.getName().startsWith("Wireframe")) {
                // Seleccionamos la figura
                figuras.seleccionarFigura(target);
            }
        } else {
            // Si no se hizo clic en ninguna figura, mantenemos la selección actual
            // (Otra opción sería deseleccionar con: figuras.deseleccionarFigura();)
        }
    }
}
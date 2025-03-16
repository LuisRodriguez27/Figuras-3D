package com.mycompany.interfaz;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * Clase para gestionar la interfaz gráfica
 * Esta clase será ampliada en el futuro para incluir labels, sliders y botones
 */
public class GUI {

    private SimpleApplication app;

    public GUI(SimpleApplication app) {
        this.app = app;
    }

    /**
     * Configura el entorno visual básico
     */
    public void configurarEntorno() {
        // Configurar color de fondo
        app.getViewPort().setBackgroundColor(ColorRGBA.White);

        // Configurar cámara
        configurarCamara();

        // Desactivar estadísticas
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
    }

    /**
     * Configura la cámara para una vista general de las figuras
     */
    public void configurarCamara() {
        app.getCamera().setLocation(new Vector3f(0, 3, 12));
        app.getCamera().lookAt(new Vector3f(0, 2, 0), Vector3f.UNIT_Y);
    }

    // Aquí se añadirán métodos para crear y gestionar labels, sliders y botones en el futuro
}
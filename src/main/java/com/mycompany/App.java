package com.mycompany;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.mycompany.figuras.Figuras;
import com.mycompany.efectos.Iluminacion;
import com.mycompany.interfaz.GUI;

public class App extends SimpleApplication {

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Configurar interfaz gráfica
        GUI gui = new GUI(this);
        gui.configurarEntorno();

        // Crear instancia de la clase Figuras y posicionar las figuras
        Figuras figuras = new Figuras(assetManager, rootNode);
        figuras.crearYColocarFiguras();

        // Configurar iluminación mejorada
        Iluminacion.configurarIluminacion(rootNode);

        // Configurar la cámara para ver todas las figuras
        cam.setLocation(new Vector3f(0, 8, 12));  // Aumentar la distancia para ver todo
        cam.lookAt(new Vector3f(0, 2, 0), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Actualizaciones del juego (si es necesario)
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // Renders adicionales (si es necesario)
    }
}
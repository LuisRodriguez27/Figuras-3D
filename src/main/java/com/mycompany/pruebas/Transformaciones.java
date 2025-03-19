package com.mycompany.pruebas;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Transformaciones {
    private Figura3D selectedFigure; // Figura seleccionada
    private boolean isDragging = false; // Indica si se está arrastrando
    private Vector2f initialMousePosition = new Vector2f(); // Posición inicial del mouse
    private Vector3f initialFigurePosition = new Vector3f(); // Posición inicial de la figura
    private boolean ctrlPressed = false; // Indica si Ctrl está presionado

    public void setSelectedFigure(Figura3D figure) {
        this.selectedFigure = figure;
    }

    public void onMouseMove(MouseMotionEvent evt) {
        if (isDragging && selectedFigure != null) {
            // Calcular el desplazamiento del mouse
            float deltaX = evt.getX() - initialMousePosition.x;
            float deltaY = evt.getY() - initialMousePosition.y;

            // Convertir el desplazamiento del mouse a coordenadas del mundo 3D
            float sensitivity = 0.01f; // Factor de sensibilidad
            float dx = deltaX * sensitivity;
            float dy = -deltaY * sensitivity;

            // Mover la figura seleccionada
            Vector3f newPosition = initialFigurePosition.clone();
            if (ctrlPressed) {
                // Movimiento en el eje Z (profundidad)
                newPosition.z += dy;
            } else {
                // Movimiento en los ejes X y Y
                newPosition.x += dx;
                newPosition.y += dy;
            }

            selectedFigure.getFiguraGroup().setLocalTranslation(newPosition);
        }
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if (evt.getButtonIndex() == 0) { // Botón izquierdo del mouse
            if (evt.isPressed()) {
                // Iniciar el arrastre
                if (selectedFigure != null) {
                    isDragging = true;
                    initialMousePosition.set(evt.getX(), evt.getY());
                    initialFigurePosition.set(selectedFigure.getFiguraGroup().getLocalTranslation());
                }
            } else {
                // Finalizar el arrastre
                isDragging = false;
            }
        }
    }

    public void setCtrlPressed(boolean pressed) {
        ctrlPressed = pressed;
    }
}
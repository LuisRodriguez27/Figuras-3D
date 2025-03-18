package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Figuras {
    private AssetManager assetManager;
    private Node rootNode;

    private float startX = -4.0f;
    private float spacing = 4.0f;
    private float y = 2.0f;
    private float z = -2.0f;

    // Añadir una lista para almacenar todas las figuras creadas
    private List<FiguraBase> figurasBase = new ArrayList<>();

    private ColorRGBA[] colors = new ColorRGBA[]{
            ColorRGBA.Red,
            ColorRGBA.Blue,
            ColorRGBA.Green,
            ColorRGBA.Yellow,
            ColorRGBA.Magenta
    };

    public Figuras(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }

    public void crearYColocarFiguras() {
        Esfera esfera = new Esfera(assetManager, rootNode, colors[0]);
        Cubo cubo = new Cubo(assetManager, rootNode, colors[1]);
        Cilindro cilindro = new Cilindro(assetManager, rootNode, colors[2]);
        Toro toro = new Toro(assetManager, rootNode, colors[3]);
        Piramide piramide = new Piramide(assetManager, rootNode, colors[4]);

        figurasBase.add(esfera);
        figurasBase.add(cubo);
        figurasBase.add(cilindro);
        figurasBase.add(toro);
        figurasBase.add(piramide);

        colocarFigura(esfera.getGroupNode(), startX, y, z);
        colocarFigura(cubo.getGroupNode(), startX + spacing, y, z);
        colocarFigura(cilindro.getGroupNode(), startX + spacing * 2, y, z);
        colocarFigura(toro.getGroupNode(), startX + spacing * 3, y, z);
        colocarFigura(piramide.getGroupNode(), startX + spacing * 4, y, z);
    }

    private void colocarFigura(Node groupNode, float x, float y, float z) {
        groupNode.setLocalTranslation(x, y, z);
    }

    public List<FiguraBase> getFigurasBase() {
        return figurasBase;
    }
}
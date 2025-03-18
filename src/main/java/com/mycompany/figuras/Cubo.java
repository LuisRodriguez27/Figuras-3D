package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class Cubo extends FiguraBase {

    public Cubo(AssetManager assetManager, Node rootNode, ColorRGBA color) {
        super(assetManager, rootNode, color);
        crearFigura();
    }

    @Override
    public void crearFigura() {
        Box boxMesh = new Box(1f, 1f, 1f);
        geometry = new Geometry("Cubo", boxMesh);
        geometry.setMaterial(crearMaterial(color));
        groupNode.attachChild(geometry);
    }

    @Override
    protected Geometry crearWireframe() {
        Box boxMesh = new Box(1f, 1f, 1f);
        Mesh wireBox = new com.jme3.scene.debug.WireBox(boxMesh.getXExtent(), boxMesh.getYExtent(), boxMesh.getZExtent());
        Geometry wireGeo = new Geometry("WireBox", wireBox);
        Material wireMat = crearMaterial(ColorRGBA.Black);
        wireMat.getAdditionalRenderState().setLineWidth(2.0f);
        wireGeo.setMaterial(wireMat);
        return wireGeo;
    }
}
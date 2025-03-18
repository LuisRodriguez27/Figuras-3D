package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public abstract class FiguraBase {
    protected AssetManager assetManager;
    protected Node groupNode; // Nodo que agrupa la figura y su wireframe
    protected Geometry geometry;
    protected Geometry wireframeGeometry;
    protected ColorRGBA color;

    public FiguraBase(AssetManager assetManager, Node rootNode, ColorRGBA color) {
        this.assetManager = assetManager;
        this.color = color;
        this.groupNode = new Node("FiguraGroup");
        rootNode.attachChild(groupNode);
    }

    public abstract void crearFigura();

    public Node getGroupNode() {
        return groupNode;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    protected Material crearMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setColor("Ambient", color.mult(0.3f));
        mat.setFloat("Shininess", 64f);
        return mat;
    }

    public void seleccionar() {
        if (wireframeGeometry == null) {
            wireframeGeometry = crearWireframe();
            wireframeGeometry.setLocalTranslation(geometry.getLocalTranslation());
            wireframeGeometry.setLocalRotation(geometry.getLocalRotation());
            wireframeGeometry.setLocalScale(geometry.getLocalScale().mult(1.01f));
            groupNode.attachChild(wireframeGeometry);
        }
    }

    public void deseleccionar() {
        if (wireframeGeometry != null) {
            groupNode.detachChild(wireframeGeometry);
            wireframeGeometry = null;
        }
    }

    protected abstract Geometry crearWireframe();


}
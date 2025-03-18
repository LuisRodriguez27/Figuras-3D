package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Torus;

public class Toro extends FiguraBase {

    public Toro(AssetManager assetManager, Node rootNode, ColorRGBA color) {
        super(assetManager, rootNode, color);
        crearFigura();
    }

    @Override
    public void crearFigura() {
        Torus torusMesh = new Torus(32, 32, 0.4f, 1.5f);
        geometry = new Geometry("Toro", torusMesh);
        geometry.setMaterial(crearMaterial(color));
        geometry.rotate(FastMath.HALF_PI, 0, 0); // Rotar para mejor visualización
        groupNode.attachChild(geometry);
    }

    @Override
    protected Geometry crearWireframe() {
        float innerRadius = 0.4f;
        float outerRadius = 1.5f;
        int segments = 24;

        // Crear vértices para el wireframe
        Vector3f[] vertices = new Vector3f[segments * 2];
        int[] indices = new int[segments * 2];

        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);

            float x1 = (float) ((outerRadius + innerRadius) * Math.cos(angle));
            float y1 = (float) ((outerRadius + innerRadius) * Math.sin(angle));
            float x2 = (float) ((outerRadius + innerRadius) * Math.cos(nextAngle));
            float y2 = (float) ((outerRadius + innerRadius) * Math.sin(nextAngle));

            vertices[i * 2] = new Vector3f(x1, y1, 0);
            vertices[i * 2 + 1] = new Vector3f(x2, y2, 0);

            indices[i * 2] = i * 2;
            indices[i * 2 + 1] = i * 2 + 1;
        }

        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Position, 3, com.jme3.util.BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Index, 1, com.jme3.util.BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry wireGeo = new Geometry("ToroWireframe", lineMesh);
        Material wireMat = crearMaterial(ColorRGBA.Black);
        wireMat.getAdditionalRenderState().setLineWidth(2.0f);
        wireGeo.setMaterial(wireMat);
        return wireGeo;
    }
}
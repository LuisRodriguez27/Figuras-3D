package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class Esfera extends FiguraBase {

    public Esfera(AssetManager assetManager, Node rootNode, ColorRGBA color) {
        super(assetManager, rootNode, color);
        crearFigura();
    }

    @Override
    public void crearFigura() {
        Sphere sphereMesh = new Sphere(32, 32, 1.5f);
        geometry = new Geometry("Esfera", sphereMesh);
        geometry.setMaterial(crearMaterial(color));
        groupNode.attachChild(geometry);
    }

    @Override
    protected Geometry crearWireframe() {
        float radius = 1.5f;
        int segments = 16;
        Vector3f[] vertices = new Vector3f[segments * 6];
        int[] indices = new int[segments * 6];
        int vertexIndex = 0;
        int indexIndex = 0;

        // Círculo en plano XZ
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float x1 = (float) (radius * Math.cos(angle));
            float z1 = (float) (radius * Math.sin(angle));
            float x2 = (float) (radius * Math.cos(nextAngle));
            float z2 = (float) (radius * Math.sin(nextAngle));
            vertices[vertexIndex] = new Vector3f(x1, 0, z1);
            vertices[vertexIndex + 1] = new Vector3f(x2, 0, z2);
            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;
            vertexIndex += 2;
            indexIndex += 2;
        }

        // Círculos en planos XY y YZ (similar al código anterior)

        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Position, 3, com.jme3.util.BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Index, 1, com.jme3.util.BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry wireGeo = new Geometry("SphereWireframe", lineMesh);
        Material wireMat = crearMaterial(ColorRGBA.Black);
        wireMat.getAdditionalRenderState().setLineWidth(2.0f);
        wireGeo.setMaterial(wireMat);
        return wireGeo;
    }
}
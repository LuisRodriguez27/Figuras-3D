package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;

public class Cilindro extends FiguraBase {

    public Cilindro(AssetManager assetManager, Node rootNode, ColorRGBA color) {
        super(assetManager, rootNode, color);
        crearFigura();
    }

    @Override
    public void crearFigura() {
        Cylinder cylinderMesh = new Cylinder(16, 32, 1.0f, 2.0f, true);
        geometry = new Geometry("Cilindro", cylinderMesh);
        geometry.setMaterial(crearMaterial(color));
        geometry.rotate(1.57f, 0, 0); // Rotar para que quede vertical
        groupNode.attachChild(geometry);
    }

    @Override
    protected Geometry crearWireframe() {
        float radius = 1.0f;
        float height = 2.0f;
        int segments = 16;

        // Crear vértices para el wireframe
        Vector3f[] vertices = new Vector3f[segments * 4];
        int[] indices = new int[segments * 4];

        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);

            float x1 = (float) (radius * Math.cos(angle));
            float z1 = (float) (radius * Math.sin(angle));
            float x2 = (float) (radius * Math.cos(nextAngle));
            float z2 = (float) (radius * Math.sin(nextAngle));

            // Base inferior
            vertices[i * 2] = new Vector3f(x1, -height / 2, z1);
            vertices[i * 2 + 1] = new Vector3f(x2, -height / 2, z2);

            // Base superior
            vertices[segments * 2 + i * 2] = new Vector3f(x1, height / 2, z1);
            vertices[segments * 2 + i * 2 + 1] = new Vector3f(x2, height / 2, z2);

            // Índices para líneas
            indices[i * 2] = i * 2;
            indices[i * 2 + 1] = i * 2 + 1;
            indices[segments * 2 + i * 2] = segments * 2 + i * 2;
            indices[segments * 2 + i * 2 + 1] = segments * 2 + i * 2 + 1;
        }

        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Position, 3, com.jme3.util.BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Index, 1, com.jme3.util.BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry wireGeo = new Geometry("CilindroWireframe", lineMesh);
        Material wireMat = crearMaterial(ColorRGBA.Black);
        wireMat.getAdditionalRenderState().setLineWidth(2.0f);
        wireGeo.setMaterial(wireMat);
        return wireGeo;
    }
}
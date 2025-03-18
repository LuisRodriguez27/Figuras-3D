package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.util.BufferUtils;

public class Piramide extends FiguraBase {

    public Piramide(AssetManager assetManager, Node rootNode, ColorRGBA color) {
        super(assetManager, rootNode, color);
        crearFigura();
    }

    @Override
    public void crearFigura() {
        Mesh pyramidMesh = new Mesh();

        // Definir vértices
        Vector3f[] vertices = new Vector3f[5];
        vertices[0] = new Vector3f(-1f, -1f, -1f);
        vertices[1] = new Vector3f(1f, -1f, -1f);
        vertices[2] = new Vector3f(1f, -1f, 1f);
        vertices[3] = new Vector3f(-1f, -1f, 1f);
        vertices[4] = new Vector3f(0f, 1.8f, 0f);

        // Definir índices
        int[] indices = new int[]{
                0, 3, 2, // Base
                0, 2, 1,
                0, 1, 4, // Caras laterales
                1, 2, 4,
                2, 3, 4,
                3, 0, 4
        };

        // Calcular normales
        Vector3f[] normals = new Vector3f[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            normals[i] = new Vector3f(0, 0, 0);
        }
        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i];
            int i2 = indices[i + 1];
            int i3 = indices[i + 2];
            Vector3f v1 = vertices[i1];
            Vector3f v2 = vertices[i2];
            Vector3f v3 = vertices[i3];
            Vector3f edge1 = v2.subtract(v1);
            Vector3f edge2 = v3.subtract(v1);
            Vector3f faceNormal = edge1.cross(edge2).normalizeLocal();
            normals[i1].addLocal(faceNormal);
            normals[i2].addLocal(faceNormal);
            normals[i3].addLocal(faceNormal);
        }
        for (int i = 0; i < normals.length; i++) {
            normals[i].normalizeLocal();
        }

        // Asignar buffers
        pyramidMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        pyramidMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        pyramidMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
        pyramidMesh.updateBound();

        geometry = new Geometry("Pirámide", pyramidMesh);
        geometry.setMaterial(crearMaterial(color));
        groupNode.attachChild(geometry);
    }

    @Override
    protected Geometry crearWireframe() {
        float baseSize = 2.0f;
        float height = 1.8f;

        // Crear vértices para el wireframe
        Vector3f[] vertices = new Vector3f[8];
        vertices[0] = new Vector3f(-baseSize / 2, -height / 2, -baseSize / 2);
        vertices[1] = new Vector3f(baseSize / 2, -height / 2, -baseSize / 2);
        vertices[2] = new Vector3f(baseSize / 2, -height / 2, baseSize / 2);
        vertices[3] = new Vector3f(-baseSize / 2, -height / 2, baseSize / 2);
        vertices[4] = new Vector3f(0, height / 2, 0); // Pico

        int[] indices = new int[]{
                0, 1, 1, 2, 2, 3, 3, 0, // Base
                0, 4, 1, 4, 2, 4, 3, 4  // Lados
        };

        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(com.jme3.scene.VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry wireGeo = new Geometry("PiramideWireframe", lineMesh);
        Material wireMat = crearMaterial(ColorRGBA.Black);
        wireMat.getAdditionalRenderState().setLineWidth(2.0f);
        wireGeo.setMaterial(wireMat);
        return wireGeo;
    }
}
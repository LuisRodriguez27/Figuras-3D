package com.mycompany.pruebas;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.scene.debug.WireBox;
import com.jme3.util.BufferUtils;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.scene.VertexBuffer.Type;

public class Figura3D {
    private Node figuraGroup; // Nodo que agrupa la figura y su borde
    private Geometry figuraGeometry;
    private Geometry wireframe;

    public Figura3D(AssetManager assetManager, String tipoFigura, ColorRGBA color) {
        figuraGroup = new Node(tipoFigura + "ConBorde");

        // Crear la geometría según el tipo de figura
        switch (tipoFigura.toLowerCase()) {
            case "cubo":
                figuraGeometry = crearCubo(assetManager, color);
                break;
            case "esfera":
                figuraGeometry = crearEsfera(assetManager, color);
                break;
            case "cilindro":
                figuraGeometry = crearCilindro(assetManager, color);
                break;
            case "toro":
                figuraGeometry = crearToro(assetManager, color);
                break;
            case "piramide":
                figuraGeometry = crearPiramide(assetManager, color);
                break;
            default:
                throw new IllegalArgumentException("Tipo de figura no soportado: " + tipoFigura);
        }

        // Añadir la figura al grupo
        figuraGroup.attachChild(figuraGeometry);

        // Agregar wireframe
//        agregarWireframe(assetManager);
    }

    public Node getFiguraGroup() {
        return figuraGroup;
    }

    public Geometry getFiguraGeometry() {
        return figuraGeometry;
    }

    private Geometry crearCubo(AssetManager assetManager, ColorRGBA color) {
        Box box = new Box(1f, 1f, 1f);
        Geometry cube = new Geometry("Cubo", box);
        cube.setMaterial(crearMaterial(assetManager, color));
        return cube;
    }

    private Geometry crearEsfera(AssetManager assetManager, ColorRGBA color) {
        Sphere sphere = new Sphere(32, 32, 1.2f);
        Geometry esfera = new Geometry("Esfera", sphere);
        esfera.setMaterial(crearMaterial(assetManager, color));
        return esfera;
    }

    private Geometry crearCilindro(AssetManager assetManager, ColorRGBA color) {
        Cylinder cylinder = new Cylinder(16, 32, 0.8f, 2f, true);
        Geometry cilindro = new Geometry("Cilindro", cylinder);
        cilindro.setMaterial(crearMaterial(assetManager, color));
        return cilindro;
    }

    private Geometry crearToro(AssetManager assetManager, ColorRGBA color) {
        Torus torus = new Torus(32, 32, 0.4f, 0.9f);
        Geometry toro = new Geometry("Toro", torus);
        toro.setMaterial(crearMaterial(assetManager, color));
        return toro;
    }

    private Geometry crearPiramide(AssetManager assetManager, ColorRGBA color) {
        Mesh pyramidMesh = new Mesh();
        Vector3f[] vertices = new Vector3f[5];
        vertices[0] = new Vector3f(-1f, -1f, -1f);
        vertices[1] = new Vector3f(1f, -1f, -1f);
        vertices[2] = new Vector3f(1f, -1f, 1f);
        vertices[3] = new Vector3f(-1f, -1f, 1f);
        vertices[4] = new Vector3f(0f, 1.8f, 0f);
        int[] indices = new int[]{
                0, 3, 2, // Base
                0, 2, 1,
                0, 1, 4, // Side faces
                1, 2, 4,
                2, 3, 4,
                3, 0, 4
        };
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
        float[] texCoord = new float[2 * vertices.length];
        texCoord[0] = 0;
        texCoord[1] = 0;
        texCoord[2] = 1;
        texCoord[3] = 0;
        texCoord[4] = 1;
        texCoord[5] = 1;
        texCoord[6] = 0;
        texCoord[7] = 1;
        texCoord[8] = 0.5f;
        texCoord[9] = 0.5f;
        pyramidMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        pyramidMesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        pyramidMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        pyramidMesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
        pyramidMesh.updateBound();
        pyramidMesh.setStatic();
        TangentBinormalGenerator.generate(pyramidMesh);
        Geometry piramide = new Geometry("Piramide", pyramidMesh);
        piramide.setMaterial(crearMaterial(assetManager, color));
        return piramide;
    }

    private Material crearMaterial(AssetManager assetManager, ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setColor("Ambient", color.mult(0.3f));
        mat.setFloat("Shininess", 64f);
        return mat;
    }

    public void agregarWireframe(AssetManager assetManager) {
        if (figuraGeometry.getMesh() instanceof Box) {
            wireframe = crearBordesCubo((Box) figuraGeometry.getMesh());
        } else if (figuraGeometry.getMesh() instanceof Sphere) {
            wireframe = crearBordesEsfera((Sphere) figuraGeometry.getMesh());
        } else if (figuraGeometry.getMesh() instanceof Cylinder) {
            wireframe = crearBordesCilindro((Cylinder) figuraGeometry.getMesh());
        } else if (figuraGeometry.getMesh() instanceof Torus) {
            wireframe = crearBordesToro((Torus) figuraGeometry.getMesh());
        } else {
            wireframe = crearBordesGenericos(figuraGeometry);
        }
        wireframe.setMaterial(crearMaterialWireframe(assetManager));
        figuraGroup.attachChild(wireframe);
    }

    public void quitarWireframe() {
        if (wireframe != null && figuraGroup.hasChild(wireframe)) {
            figuraGroup.detachChild(wireframe); // Eliminar el borde del grupo
            wireframe = null; // Limpiar la referencia al wireframe
        }
    }

    private Geometry crearBordesCubo(Box box) {
        WireBox wireBox = new WireBox(box.getXExtent(), box.getYExtent(), box.getZExtent());
        return new Geometry("WireBox", wireBox);
    }

    private Geometry crearBordesEsfera(Sphere sphere) {
        float radius = sphere.getRadius();
        int segments = 16;
        Vector3f[] vertices = new Vector3f[segments * 6];
        int[] indices = new int[segments * 6];
        int vertexIndex = 0;
        int indexIndex = 0;
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float x1 = (float) (radius * FastMath.cos(angle));
            float z1 = (float) (radius * FastMath.sin(angle));
            float x2 = (float) (radius * FastMath.cos(nextAngle));
            float z2 = (float) (radius * FastMath.sin(nextAngle));
            vertices[vertexIndex] = new Vector3f(x1, 0, z1);
            vertices[vertexIndex + 1] = new Vector3f(x2, 0, z2);
            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;
            vertexIndex += 2;
            indexIndex += 2;
        }
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float x1 = (float) (radius * FastMath.cos(angle));
            float y1 = (float) (radius * FastMath.sin(angle));
            float x2 = (float) (radius * FastMath.cos(nextAngle));
            float y2 = (float) (radius * FastMath.sin(nextAngle));
            vertices[vertexIndex] = new Vector3f(x1, y1, 0);
            vertices[vertexIndex + 1] = new Vector3f(x2, y2, 0);
            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;
            vertexIndex += 2;
            indexIndex += 2;
        }
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float y1 = (float) (radius * FastMath.cos(angle));
            float z1 = (float) (radius * FastMath.sin(angle));
            float y2 = (float) (radius * FastMath.cos(nextAngle));
            float z2 = (float) (radius * FastMath.sin(nextAngle));
            vertices[vertexIndex] = new Vector3f(0, y1, z1);
            vertices[vertexIndex + 1] = new Vector3f(0, y2, z2);
            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;
            vertexIndex += 2;
            indexIndex += 2;
        }
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();
        return new Geometry("SphereWireframe", lineMesh);
    }

    private Geometry crearBordesCilindro(Cylinder cylinder) {
        float radius = cylinder.getRadius();
        float height = cylinder.getHeight();
        int segments = 16;
        Vector3f[] vertices = new Vector3f[segments * 4];
        int[] indices = new int[segments * 4];
        int vertexIndex = 0;
        int indexIndex = 0;
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float x1 = (float) (radius * FastMath.cos(angle));
            float z1 = (float) (radius * FastMath.sin(angle));
            float x2 = (float) (radius * FastMath.cos(nextAngle));
            float z2 = (float) (radius * FastMath.sin(nextAngle));
            vertices[vertexIndex] = new Vector3f(x1, -height / 2, z1);
            vertices[vertexIndex + 1] = new Vector3f(x2, -height / 2, z2);
            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;
            vertexIndex += 2;
            indexIndex += 2;
        }
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float x1 = (float) (radius * FastMath.cos(angle));
            float z1 = (float) (radius * FastMath.sin(angle));
            float x2 = (float) (radius * FastMath.cos(nextAngle));
            float z2 = (float) (radius * FastMath.sin(nextAngle));
            vertices[vertexIndex] = new Vector3f(x1, height / 2, z1);
            vertices[vertexIndex + 1] = new Vector3f(x2, height / 2, z2);
            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;
            vertexIndex += 2;
            indexIndex += 2;
        }
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();
        return new Geometry("CylinderWireframe", lineMesh);
    }

    private Geometry crearBordesToro(Torus torus) {
        float innerRadius = torus.getInnerRadius();
        float outerRadius = torus.getOuterRadius();
        float centerRadius = (float) (outerRadius + 0.43);
        int segments = 24;
        Vector3f[] vertices = new Vector3f[segments * 2];
        int[] indices = new int[segments * 2];
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);
            float x1 = (float) (centerRadius * FastMath.cos(angle));
            float y1 = (float) (centerRadius * FastMath.sin(angle));
            float x2 = (float) (centerRadius * FastMath.cos(nextAngle));
            float y2 = (float) (centerRadius * FastMath.sin(nextAngle));
            vertices[i * 2] = new Vector3f(x1, y1, 0);
            vertices[i * 2 + 1] = new Vector3f(x2, y2, 0);
            indices[i * 2] = i * 2;
            indices[i * 2 + 1] = i * 2 + 1;
        }
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();
        return new Geometry("TorusWireframe", lineMesh);
    }

    private Geometry crearBordesGenericos(Geometry geometry) {
        Geometry wireGeo = geometry.clone();
        wireGeo.setName("Wireframe-" + geometry.getName());
        Material wireMat = crearMaterialWireframe(geometry.getMaterial().getMaterialDef().getAssetManager());
        wireMat.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
        wireMat.getAdditionalRenderState().setWireframe(true);
        wireMat.getAdditionalRenderState().setLineWidth(2.0f);
        wireGeo.setMaterial(wireMat);
        wireGeo.setLocalScale(1.01f);
        return wireGeo;
    }

    private Material crearMaterialWireframe(AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        mat.getAdditionalRenderState().setLineWidth(2.0f);
        return mat;
    }
}
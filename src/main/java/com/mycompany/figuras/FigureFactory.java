package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;

public class FigureFactory {

    private AssetManager assetManager;

    public FigureFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

//    public Geometry createSphere(ColorRGBA color) {
//        Sphere sphere = new Sphere(30, 30, 1);
//        Geometry geom = new Geometry("Sphere", sphere);
//        geom.setMaterial(createMaterial(color));
//        return geom;
//    }
//
//    public Geometry createPyramid(ColorRGBA color) {
//        Mesh pyramidMesh = new Mesh();
//        Vector3f[] vertices = new Vector3f[5];
//        vertices[0] = new Vector3f(-1f, -1f, -1f);
//        vertices[1] = new Vector3f(1f, -1f, -1f);
//        vertices[2] = new Vector3f(1f, -1f, 1f);
//        vertices[3] = new Vector3f(-1f, -1f, 1f);
//        vertices[4] = new Vector3f(0f, 1.8f, 0f);
//
//        int[] indices = new int[] {
//                0, 3, 2, 0, 2, 1, 0, 1, 4, 1, 2, 4, 2, 3, 4, 3, 0, 4
//        };
//
//        Vector3f[] normals = new Vector3f[vertices.length];
//        for (int i = 0; i < normals.length; i++) {
//            normals[i] = new Vector3f(0, 0, 0);
//        }
//
//        for (int i = 0; i < indices.length; i += 3) {
//            int i1 = indices[i];
//            int i2 = indices[i + 1];
//            int i3 = indices[i + 2];
//
//            Vector3f v1 = vertices[i1];
//            Vector3f v2 = vertices[i2];
//            Vector3f v3 = vertices[i3];
//
//            Vector3f edge1 = v2.subtract(v1);
//            Vector3f edge2 = v3.subtract(v1);
//            Vector3f faceNormal = edge1.cross(edge2).normalizeLocal();
//
//            normals[i1].addLocal(faceNormal);
//            normals[i2].addLocal(faceNormal);
//            normals[i3].addLocal(faceNormal);
//        }
//
//        for (int i = 0; i < normals.length; i++) {
//            normals[i].normalizeLocal();
//        }
//
//        pyramidMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//        pyramidMesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
//        pyramidMesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//
//        pyramidMesh.updateBound();
//        pyramidMesh.updateCounts();
//
//
//        Geometry geom = new Geometry("Pyramid", pyramidMesh);
//        geom.setMaterial(createMaterial(color));
//        return geom;
//    }
//
//    public Geometry createCylinder(ColorRGBA color) {
//        Cylinder cylinder = new Cylinder(30, 30, 1, 2, true);
//        Geometry geom = new Geometry("Cylinder", cylinder);
//        geom.rotate(FastMath.PI / 2, 0, 0);
//        geom.setMaterial(createMaterial(color));
//        return geom;
//    }
//
//    public Geometry createTorus(ColorRGBA color) {
//        Torus torus = new Torus(32, 32, 0.4f, 0.9f);
//        Geometry geom = new Geometry("Torus", torus);
//        geom.setMaterial(createMaterial(color));
//        return geom;
//    }
//
//    public Geometry createCube(ColorRGBA color) {
//        Box box = new Box(1, 1, 1);
//        Geometry geom = new Geometry("Box", box);
//        geom.setMaterial(createMaterial(color));
//        return geom;
//    }

    public Geometry createSphere() {
        Sphere sphere = new Sphere(30, 30, 1);
        Geometry geom = new Geometry("Sphere", sphere);
        applyGradient(geom.getMesh(), ColorRGBA.Yellow, ColorRGBA.Pink);
        geom.setMaterial(createMaterialWithVertexColors());
        return geom;
    }

    public Geometry createPyramid() {
        Mesh pyramidMesh = new Mesh();
        Vector3f[] vertices = new Vector3f[5];
        vertices[0] = new Vector3f(-1f, -1f, -1f);
        vertices[1] = new Vector3f(1f, -1f, -1f);
        vertices[2] = new Vector3f(1f, -1f, 1f);
        vertices[3] = new Vector3f(-1f, -1f, 1f);
        vertices[4] = new Vector3f(0f, 1.8f, 0f);

        int[] indices = new int[] {
                0, 3, 2, 0, 2, 1, 0, 1, 4, 1, 2, 4, 2, 3, 4, 3, 0, 4
        };

        Vector3f[] normals = new Vector3f[vertices.length];
        for (int i = 0; i < normals.length; i++) {
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

        pyramidMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        pyramidMesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
        pyramidMesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));

        pyramidMesh.updateBound();
        pyramidMesh.updateCounts();


        Geometry geom = new Geometry("Pyramid", pyramidMesh);
        applyGradient(geom.getMesh(), ColorRGBA.Red, ColorRGBA.Yellow);
        geom.setMaterial(createMaterialWithVertexColors());
        return geom;
    }

    public Geometry createCylinder() {
        Cylinder cylinder = new Cylinder(30, 30, 1, 2, true);
        Geometry geom = new Geometry("Cylinder", cylinder);
        geom.rotate(FastMath.PI / 2, 0, 0);
        applyGradient(geom.getMesh(), ColorRGBA.Green, ColorRGBA.Brown);
        geom.setMaterial(createMaterialWithVertexColors());
        return geom;
    }

    public Geometry createTorus() {
        Torus torus = new Torus(32, 32, 0.4f, 0.9f);
        Geometry geom = new Geometry("Torus", torus);
        applyGradient(geom.getMesh(), ColorRGBA.Yellow, ColorRGBA.Pink);
        geom.setMaterial(createMaterialWithVertexColors());
        return geom;
    }

    public Geometry createCube() {
        Box box = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", box);
        applyGradient(geom.getMesh(), ColorRGBA.Magenta, ColorRGBA.Blue);
        geom.setMaterial(createMaterialWithVertexColors());
        return geom;
    }





    private Material createMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", new ColorRGBA(color));
        mat.setColor("Ambient", new ColorRGBA(color));
        mat.setColor("Specular", ColorRGBA.Black);
        mat.setFloat("Shininess", 0f);
        return mat;
    }


    public Geometry createWireframe(Geometry geometry) {
        if (geometry.getMesh() instanceof Box) {
            return crearBordesCubo((Box) geometry.getMesh());
        } else if (geometry.getMesh() instanceof Sphere) {
            return crearBordesEsfera((Sphere) geometry.getMesh());
        } else if (geometry.getMesh() instanceof Cylinder) {
            return crearBordesCilindro((Cylinder) geometry.getMesh());
        } else if (geometry.getMesh() instanceof Torus) {
            return crearBordesToro((Torus) geometry.getMesh());
        } else {
            return crearBordesGenericos(geometry);
        }
    }

    public void addWireframe(Node figureGroup, Geometry geometry) {
        Geometry wireframe = createWireframe(geometry);
        wireframe.setMaterial(crearMaterialWireframe(assetManager));
        figureGroup.attachChild(wireframe);
    }

    public void removeWireframe(Node figureGroup, Geometry wireframe) {
        if (wireframe != null && figureGroup.hasChild(wireframe)) {
            figureGroup.detachChild(wireframe);
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
        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
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
        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
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
        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
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




    private void applyGradient(Mesh mesh, ColorRGBA colorTop, ColorRGBA colorBottom) {
        VertexBuffer pb = mesh.getBuffer(VertexBuffer.Type.Position);
        if (pb == null) {
            return;
        }

        FloatBuffer positionBuffer = (FloatBuffer) pb.getData();
        float[] positions = new float[positionBuffer.limit()];
        positionBuffer.rewind(); // Asegurarse de leer desde el principio
        positionBuffer.get(positions);

        int vertexCount = positions.length / 3;
        float[] colors = new float[vertexCount * 4];

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        // Encontrar los valores mínimos y máximos en Y
        for (int i = 1; i < positions.length; i += 3) {
            if (positions[i] < minY) minY = positions[i];
            if (positions[i] > maxY) maxY = positions[i];
        }

        float range = maxY - minY;
        if (range == 0) range = 1; // Evitar divisiones por cero

        // Asignar colores basados en la altura Y
        for (int i = 0, j = 0; i < positions.length; i += 3, j += 4) {
            float t = (positions[i + 1] - minY) / range;
            colors[j] = FastMath.interpolateLinear(t, colorBottom.r, colorTop.r);
            colors[j + 1] = FastMath.interpolateLinear(t, colorBottom.g, colorTop.g);
            colors[j + 2] = FastMath.interpolateLinear(t, colorBottom.b, colorTop.b);
            colors[j + 3] = 1.0f;
        }

        mesh.setBuffer(VertexBuffer.Type.Color, 4, colors);
    }

    private Material createMaterialWithVertexColors() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
        return mat;
    }

}
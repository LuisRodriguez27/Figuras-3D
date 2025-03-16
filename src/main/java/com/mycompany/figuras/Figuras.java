package com.mycompany.figuras;

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
import com.jme3.util.BufferUtils;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.scene.VertexBuffer.Type;

public class Figuras {

    private AssetManager assetManager;
    private Node rootNode;

    // Posiciones base para las figuras
    private float startX = -10.0f;  // Ajustado para dar espacio a más figuras
    private float spacing = 4.0f;   // Espacio entre figuras
    private float y = 2.0f;         // Altura uniforme
    private float z = -2.0f;        // Profundidad uniforme

    // Colores para las figuras
    private ColorRGBA[] colors = new ColorRGBA[] {
            ColorRGBA.Red,
            ColorRGBA.Blue,
            ColorRGBA.Green,
            ColorRGBA.Yellow,
            ColorRGBA.Orange,
            ColorRGBA.Magenta  // Color para la pirámide
    };

    public Figuras(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }

    /**
     * Crea y coloca todas las figuras en la escena
     */
    public void crearYColocarFiguras() {
        // 1. Esfera
        Geometry sphereGeo = crearEsfera(colors[0], 1.5f);
        sphereGeo.setLocalTranslation(startX, y, z);
        rootNode.attachChild(sphereGeo);

        // 2. Cubo
        Geometry boxGeo = crearCubo(colors[1], 1.0f);
        boxGeo.setLocalTranslation(startX + spacing, y, z);
        rootNode.attachChild(boxGeo);

        // 3. Cilindro
        Geometry cylinderGeo = crearCilindro(colors[2], 1.0f, 2.0f);
        cylinderGeo.setLocalTranslation(startX + spacing * 2, y, z);
        cylinderGeo.rotate(1.57f, 0, 0); // Rotar para que quede vertical
        rootNode.attachChild(cylinderGeo);

        // 4. Toro/Dona
        Geometry torusGeo = crearToro(colors[3], 0.4f, 1.5f);
        torusGeo.setLocalTranslation(startX + spacing * 3, y, z);
        torusGeo.rotate(1.57f, 0, 0); // Rotar para mejor visualización
        rootNode.attachChild(torusGeo);

        // 5. Pirámide
        Geometry pyramidGeo = crearPiramide(colors[5]);
        pyramidGeo.setLocalTranslation(startX + spacing * 4, y, z);
        rootNode.attachChild(pyramidGeo);
    }

    /**
     * Crea una esfera con los parámetros especificados
     */
    public Geometry crearEsfera(ColorRGBA color, float radio) {
        Sphere sphereMesh = new Sphere(32, 32, radio);
        Geometry sphereGeo = new Geometry("Sphere", sphereMesh);
        sphereMesh.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphereMesh);

        Material sphereMat = createMaterial(color);
        sphereGeo.setMaterial(sphereMat);
        return sphereGeo;
    }

    /**
     * Crea un cubo con los parámetros especificados
     */
    public Geometry crearCubo(ColorRGBA color, float lado) {
        Box boxMesh = new Box(lado, lado, lado);
        Geometry boxGeo = new Geometry("Box", boxMesh);
        TangentBinormalGenerator.generate(boxMesh);

        Material boxMat = createMaterial(color);
        boxGeo.setMaterial(boxMat);
        return boxGeo;
    }

    /**
     * Crea un cilindro con los parámetros especificados
     */
    public Geometry crearCilindro(ColorRGBA color, float radio, float altura) {
        Cylinder cylinderMesh = new Cylinder(16, 32, radio, altura, true);
        Geometry cylinderGeo = new Geometry("Cylinder", cylinderMesh);
        TangentBinormalGenerator.generate(cylinderMesh);

        Material cylinderMat = createMaterial(color);
        cylinderGeo.setMaterial(cylinderMat);
        return cylinderGeo;
    }

    /**
     * Crea un toro con los parámetros especificados
     */
    public Geometry crearToro(ColorRGBA color, float radioInterno, float radioExterno) {
        Torus torusMesh = new Torus(32, 32, radioInterno, radioExterno);
        Geometry torusGeo = new Geometry("Torus", torusMesh);
        TangentBinormalGenerator.generate(torusMesh);

        Material torusMat = createMaterial(color);
        torusGeo.setMaterial(torusMat);

        torusGeo.rotate(FastMath.HALF_PI, 0, 0);

        return torusGeo;
    }

    /**
     * Crea una pirámide con los parámetros especificados
     */
    public Geometry crearPiramide(ColorRGBA color) {
        // Create empty mesh for pyramid
        Mesh pyramidMesh = new Mesh();

        // Define vertices (5 points)
        Vector3f[] vertices = new Vector3f[5];
        vertices[0] = new Vector3f(-1f, -1f, -1f);
        vertices[1] = new Vector3f(1f, -1f, -1f);
        vertices[2] = new Vector3f(1f, -1f, 1f);
        vertices[3] = new Vector3f(-1f, -1f, 1f);
        vertices[4] = new Vector3f(0f, 1.8f, 0f);

        // Define faces
        int[] indices = new int[] {
                0, 3, 2, // Base
                0, 2, 1,
                0, 1, 4, // Side faces
                1, 2, 4,
                2, 3, 4,
                3, 0, 4
        };

        // Calculate normals manually
        Vector3f[] normals = new Vector3f[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            normals[i] = new Vector3f(0, 0, 0); // Initialize normals to zero
        }

        // Calculate normals for each face and add to vertex normals
        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i];
            int i2 = indices[i+1];
            int i3 = indices[i+2];

            Vector3f v1 = vertices[i1];
            Vector3f v2 = vertices[i2];
            Vector3f v3 = vertices[i3];

            // Calculate face normal using cross product
            Vector3f edge1 = v2.subtract(v1);
            Vector3f edge2 = v3.subtract(v1);
            Vector3f faceNormal = edge1.cross(edge2).normalizeLocal();

            // Add face normal to each vertex normal
            normals[i1].addLocal(faceNormal);
            normals[i2].addLocal(faceNormal);
            normals[i3].addLocal(faceNormal);
        }

        // Normalize all vertex normals
        for (int i = 0; i < normals.length; i++) {
            normals[i].normalizeLocal();
        }

        // Add both texture coordinates
        float[] texCoord = new float[2 * vertices.length];
        // Simple planar mapping (you might want to improve this)
        texCoord[0] = 0; texCoord[1] = 0;
        texCoord[2] = 1; texCoord[3] = 0;
        texCoord[4] = 1; texCoord[5] = 1;
        texCoord[6] = 0; texCoord[7] = 1;
        texCoord[8] = 0.5f; texCoord[9] = 0.5f; // Tip

        // Assign all buffers to mesh
        pyramidMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        pyramidMesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        pyramidMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        pyramidMesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));

        // Update and prepare mesh
        pyramidMesh.updateBound();
        pyramidMesh.setStatic();

        // Now that we have normals, we can generate tangents
        TangentBinormalGenerator.generate(pyramidMesh);

        // Create geometry with the mesh
        Geometry pyramid = new Geometry("Pyramid", pyramidMesh);
        Material mat = createMaterial(color);
        pyramid.setMaterial(mat);

        return pyramid;
    }

    /**
     * Método auxiliar para crear materiales con el mismo estilo pero diferentes colores
     */
    private Material createMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setColor("Ambient", color.mult(0.3f));
        mat.setFloat("Shininess", 64f);
        return mat;
    }
}

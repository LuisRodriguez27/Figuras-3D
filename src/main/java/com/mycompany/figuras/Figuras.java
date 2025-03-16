package com.mycompany.figuras;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import com.jme3.util.BufferUtils;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.scene.VertexBuffer.Type;

public class Figuras {

    private AssetManager assetManager;
    private Node rootNode;

    // Geometry para la figura seleccionada actualmente
    private Geometry selectedGeometry = null;
    private Geometry wireframeGeometry = null;

    // Posiciones base para las figuras
    private float startX = -8.0f;  // Ajustado para dar espacio a más figuras
    private float spacing = 4.0f;   // Espacio entre figuras
    private float y = 2.0f;         // Altura uniforme
    private float z = -2.0f;        // Profundidad uniforme

    // Colores para las figuras
    private ColorRGBA[] colors = new ColorRGBA[]{
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
        int[] indices = new int[]{
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
            int i2 = indices[i + 1];
            int i3 = indices[i + 2];

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
        texCoord[0] = 0;
        texCoord[1] = 0;
        texCoord[2] = 1;
        texCoord[3] = 0;
        texCoord[4] = 1;
        texCoord[5] = 1;
        texCoord[6] = 0;
        texCoord[7] = 1;
        texCoord[8] = 0.5f;
        texCoord[9] = 0.5f; // Tip

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

    /**
     * Crea un material para el wireframe (borde negro)
     */
    private Material createWireframeMaterial() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);

        // Configuramos el grosor de línea (solo funciona si la tarjeta gráfica lo soporta)
        mat.getAdditionalRenderState().setLineWidth(2.0f);

        return mat;
    }

    /**
     * Crea un wireframe para cubo que solo muestra las aristas
     */
    private Geometry createCubeEdges(Box box) {
        // Creamos un WireBox que solo muestra las aristas sin diagonales
        WireBox wireBox = new WireBox(box.getXExtent(), box.getYExtent(), box.getZExtent());
        Geometry wireGeo = new Geometry("WireBox", wireBox);
        wireGeo.setMaterial(createWireframeMaterial());
        return wireGeo;
    }



    /**
     * Crea líneas alrededor de la figura para mostrar los bordes
     */
    private Geometry createEdgeWireframe(Geometry geometry) {
        // Este método crea un wireframe apropiado según el tipo de geometría
        if (geometry.getName().equals("Box")) {
            return createCubeEdges((Box) geometry.getMesh());
        }
        else if (geometry.getName().equals("Sphere")) {
            return createSphereEdges((Sphere) geometry.getMesh());
        }
        else if (geometry.getName().equals("Cylinder")) {
            return createCylinderEdges((Cylinder) geometry.getMesh());
        }
        else if (geometry.getName().equals("Torus")) {
            return createTorusEdges((Torus) geometry.getMesh());
        }
        else {
            // Para otras figuras, creamos un wireframe de líneas
            Geometry wireGeo = geometry.clone();
            wireGeo.setName("Wireframe-" + geometry.getName());

            Material wireMat = createWireframeMaterial();
            wireMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            wireMat.getAdditionalRenderState().setWireframe(true);
            wireMat.getAdditionalRenderState().setLineWidth(2.0f);

            wireGeo.setMaterial(wireMat);
            wireGeo.setLocalScale(1.01f);

            return wireGeo;
        }
    }

    /**
     * Crea un wireframe para cilindro que solo muestra los bordes principales
     */
    private Geometry createCylinderEdges(Cylinder cylinder) {
        // Creamos una malla personalizada para el contorno del cilindro
        Mesh edgeMesh = new Mesh();

        float radius = cylinder.getRadius();
        float height = cylinder.getHeight();
        int segments = 16; // Número de segmentos para los círculos

        // Total de vértices: 2 círculos * segments + 2*segments para líneas verticales
        Vector3f[] vertices = new Vector3f[segments * 4];
        int[] indices = new int[segments * 8]; // 2 puntos por línea * (2 círculos + segmentos líneas verticales)

        // Círculo superior e inferior
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);

            float x1 = (float) (radius * Math.cos(angle));
            float z1 = (float) (radius * Math.sin(angle));
            float x2 = (float) (radius * Math.cos(nextAngle));
            float z2 = (float) (radius * Math.sin(nextAngle));

            // Círculo superior
            vertices[i*2] = new Vector3f(x1, height/2, z1);
            vertices[i*2+1] = new Vector3f(x2, height/2, z2);

            // Círculo inferior
            vertices[segments*2 + i*2] = new Vector3f(x1, -height/2, z1);
            vertices[segments*2 + i*2+1] = new Vector3f(x2, -height/2, z2);

            // Índices para círculo superior
            indices[i*2] = i*2;
            indices[i*2+1] = i*2+1;

            // Índices para círculo inferior
            indices[segments*2 + i*2] = segments*2 + i*2;
            indices[segments*2 + i*2+1] = segments*2 + i*2+1;
        }

        // Crear una geometría de líneas
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry edgesGeo = new Geometry("CylinderWireframe", lineMesh);
        edgesGeo.setMaterial(createWireframeMaterial());

        return edgesGeo;
    }

    /**
     * Crea un wireframe para toro que solo muestra los bordes principales
     */
    private Geometry createTorusEdges(Torus torus) {
        // Parámetros del toro
        float innerRadius = torus.getInnerRadius();
        float outerRadius = torus.getOuterRadius();
        float centerRadius = outerRadius - innerRadius;

        int segments = 24;

        // Creamos las líneas para los círculos principales
        Vector3f[] vertices = new Vector3f[segments * 2];
        int[] indices = new int[segments * 2];

        // Círculo externo principal
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);

            float x1 = (float) (centerRadius * Math.cos(angle));
            float z1 = (float) (centerRadius * Math.sin(angle));
            float x2 = (float) (centerRadius * Math.cos(nextAngle));
            float z2 = (float) (centerRadius * Math.sin(nextAngle));

            vertices[i*2] = new Vector3f(x1, 0, z1);
            vertices[i*2+1] = new Vector3f(x2, 0, z2);

            indices[i*2] = i*2;
            indices[i*2+1] = i*2+1;
        }

        // Crear una geometría de líneas
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry edgesGeo = new Geometry("TorusWireframe", lineMesh);
        edgesGeo.setMaterial(createWireframeMaterial());

        return edgesGeo;
    }

    /**
     * Crea un wireframe para esfera que solo muestra líneas principales
     */
    private Geometry createSphereEdges(Sphere sphere) {
        float radius = sphere.getRadius();
        int segments = 16; // Número de segmentos para los círculos

        // Calculamos el número total de vértices y líneas
        // 3 círculos principales (X, Y, Z) con segments líneas cada uno
        int totalVertices = segments * 6; // 2 vértices por línea * 3 círculos
        int totalIndices = segments * 6;  // 2 índices por línea * 3 círculos

        Vector3f[] vertices = new Vector3f[totalVertices];
        int[] indices = new int[totalIndices];

        int vertexIndex = 0;
        int indexIndex = 0;

        // Círculo en plano XZ (ecuador)
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

        // Círculo en plano XY (meridiano 1)
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);

            float x1 = (float) (radius * Math.cos(angle));
            float y1 = (float) (radius * Math.sin(angle));
            float x2 = (float) (radius * Math.cos(nextAngle));
            float y2 = (float) (radius * Math.sin(nextAngle));

            vertices[vertexIndex] = new Vector3f(x1, y1, 0);
            vertices[vertexIndex + 1] = new Vector3f(x2, y2, 0);

            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;

            vertexIndex += 2;
            indexIndex += 2;
        }

        // Círculo en plano YZ (meridiano 2)
        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            float nextAngle = (float) ((i + 1) % segments * 2 * Math.PI / segments);

            float y1 = (float) (radius * Math.cos(angle));
            float z1 = (float) (radius * Math.sin(angle));
            float y2 = (float) (radius * Math.cos(nextAngle));
            float z2 = (float) (radius * Math.sin(nextAngle));

            vertices[vertexIndex] = new Vector3f(0, y1, z1);
            vertices[vertexIndex + 1] = new Vector3f(0, y2, z2);

            indices[indexIndex] = vertexIndex;
            indices[indexIndex + 1] = vertexIndex + 1;

            vertexIndex += 2;
            indexIndex += 2;
        }

        // Crear una geometría de líneas
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indices));
        lineMesh.updateBound();

        Geometry edgesGeo = new Geometry("SphereWireframe", lineMesh);
        edgesGeo.setMaterial(createWireframeMaterial());

        return edgesGeo;
    }

    /**
     * Selecciona una geometría y le añade un wireframe
     *
     * @param geometry La geometría a seleccionar
     */
    public void seleccionarFigura(Geometry geometry) {
        // Si hay una figura seleccionada previamente, quitamos su wireframe
        if (wireframeGeometry != null) {
            rootNode.detachChild(wireframeGeometry);
            wireframeGeometry = null;
        }

        // Si seleccionamos una nueva figura
        if (geometry != null) {
            // Guardamos la referencia a la geometría seleccionada
            selectedGeometry = geometry;

            // Creamos el wireframe apropiado que solo muestra las aristas
            wireframeGeometry = createEdgeWireframe(geometry);

            // Posicionamos el wireframe en la misma posición que la geometría seleccionada
            wireframeGeometry.setLocalTranslation(geometry.getLocalTranslation());
            wireframeGeometry.setLocalRotation(geometry.getLocalRotation());
            wireframeGeometry.setLocalScale(geometry.getLocalScale().mult(1.01f));  // Ligeramente más grande

// Añadimos el wireframe al nodo raíz
            rootNode.attachChild(wireframeGeometry);

// Configuramos el wireframe para que se dibuje en la capa de líneas (sobre la geometría)
            wireframeGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        }

// Si no hay geometría seleccionada, simplemente quitamos la referencia
        selectedGeometry = geometry;
    }

    /**
     * Deselecciona la figura actual
     */
    public void deseleccionarFigura() {
        if (wireframeGeometry != null) {
            rootNode.detachChild(wireframeGeometry);
            wireframeGeometry = null;
        }
        selectedGeometry = null;
    }

    /**
     * Devuelve la geometría seleccionada actualmente
     *
     * @return La geometría seleccionada o null si no hay ninguna
     */
    public Geometry getSelectedGeometry() {
        return selectedGeometry;
    }

    /**
     * Mover la figura seleccionada a una nueva posición
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param z Coordenada Z
     */
    public void moverFiguraSeleccionada(float x, float y, float z) {
        if (selectedGeometry != null) {
            // Mover la geometría seleccionada
            selectedGeometry.setLocalTranslation(x, y, z);

            // También mover el wireframe si existe
            if (wireframeGeometry != null) {
                wireframeGeometry.setLocalTranslation(x, y, z);
            }
        }
    }

    /**
     * Rotar la figura seleccionada
     *
     * @param x Rotación en el eje X (radianes)
     * @param y Rotación en el eje Y (radianes)
     * @param z Rotación en el eje Z (radianes)
     */
    public void rotarFiguraSeleccionada(float x, float y, float z) {
        if (selectedGeometry != null) {
            // Rotar la geometría seleccionada
            selectedGeometry.rotate(x, y, z);

            // También rotar el wireframe si existe
            if (wireframeGeometry != null) {
                wireframeGeometry.rotate(x, y, z);
            }
        }
    }

    /**
     * Cambiar el color de la figura seleccionada
     *
     * @param color Nuevo color para la figura
     */
    public void cambiarColorFiguraSeleccionada(ColorRGBA color) {
        if (selectedGeometry != null) {
            Material mat = selectedGeometry.getMaterial();
            mat.setColor("Diffuse", color);
            mat.setColor("Ambient", color.mult(0.3f));
        }
    }

    /**
     * Eliminar la figura seleccionada de la escena
     */
    public void eliminarFiguraSeleccionada() {
        if (selectedGeometry != null) {
            // Eliminar la geometría seleccionada
            rootNode.detachChild(selectedGeometry);

            // También eliminar el wireframe si existe
            if (wireframeGeometry != null) {
                rootNode.detachChild(wireframeGeometry);
                wireframeGeometry = null;
            }

            selectedGeometry = null;
        }
    }
}
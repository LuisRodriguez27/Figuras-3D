package com.mycompany.train;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

public class Piezas {
    private AssetManager assetManager;

    public Piezas(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Geometry createRuedas(){
        Cylinder cylinder = new Cylinder(30, 30, 1.5f, 0.5f, true);
        Geometry geom = new Geometry("Cylinder", cylinder);
//        geom.setMaterial(createTexturedMaterial("assets/textures/wheel2.jpg"));
        geom.setMaterial(createMaterial(new ColorRGBA(0.05f, 0.05f, 0.05f, 1.0f)));
        return geom;
    }


    public Geometry createCabina(){
        Cylinder cylinder2 = new Cylinder(30, 30, 2.5f, 7, true);
        Geometry geom = new Geometry("Cylinder2", cylinder2);
        geom.rotate(0, FastMath.PI / 2, 0);
        geom.setMaterial(createTexturedMaterial("assets/textures/metal.png", ColorRGBA.DarkGray));
        return geom;
    }

    public Geometry createEscape(){
        Cylinder cylinder2 = new Cylinder(30, 30, 0.5f, 2.7F, true);
        Geometry geom = new Geometry("Cylinder2", cylinder2);
        geom.rotate(FastMath.PI / 2, 0, 0);
        geom.setMaterial(createTexturedMaterial("assets/textures/metal_gris.png", ColorRGBA.LightGray));
        return geom;

    }

    public Geometry createEscape2(){
        Cylinder cylinder2 = new Cylinder(30, 30, 0.5f, 1, true);
        Geometry geom = new Geometry("Cylinder2", cylinder2);
        geom.rotate(FastMath.PI / 2, 0, 0);
        geom.setMaterial(createTexturedMaterial("assets/textures/metal_gris.png", ColorRGBA.LightGray));
        return geom;
    }

    public Geometry createTapa(){
        float base1 = 1;
        float base2 = 0.5F;
        float height = 0.35F;
        float depth = 1F;

        // Crear la malla para el trapecio
        Mesh mesh = new Mesh();

        // Definir los vértices del trapecio (base1 y base2 son las longitudes de las bases)
        Vector3f[] vertices = new Vector3f[8];
        vertices[0] = new Vector3f(-base1 / 2, 0, 0);       // Vértice inferior izquierdo de la base1
        vertices[1] = new Vector3f(base1 / 2, 0, 0);        // Vértice inferior derecho de la base1
        vertices[2] = new Vector3f(-base2 / 2, height, 0);  // Vértice superior izquierdo de la base2
        vertices[3] = new Vector3f(base2 / 2, height, 0);   // Vértice superior derecho de la base2

        vertices[4] = new Vector3f(-base1 / 2, 0, depth);   // Repetir los mismos vértices pero con profundidad
        vertices[5] = new Vector3f(base1 / 2, 0, depth);
        vertices[6] = new Vector3f(-base2 / 2, height, depth);
        vertices[7] = new Vector3f(base2 / 2, height, depth);

        // Definir los índices para las caras del trapecio
        int[] indices = new int[] {
                // Cara frontal
                0, 1, 3, 0, 3, 2,
                // Cara trasera
                4, 6, 7, 4, 7, 5,
                // Cara izquierda
                0, 2, 6, 0, 6, 4,
                // Cara derecha
                1, 5, 7, 1, 7, 3,
                // Cara inferior
                0, 4, 5, 0, 5, 1,
                // Cara superior
                2, 3, 7, 2, 7, 6
        };

        // Calcular las normales para cada vértice
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

        // Asignar los búferes a la malla
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));

        mesh.updateBound();
        mesh.updateCounts();

        // Crear la geometría y asignarle un material
        Geometry geom = new Geometry("Trapezoid", mesh);
//        geom.setMaterial(createTexturedMaterial("assets/textures/metal_gris.png"));
        geom.setMaterial(createMaterial(ColorRGBA.Gray));

        return geom;
    }

    public Geometry createCabina2(){
        Box box = new Box(3F, 4, 2.5F);
        Geometry geom = new Geometry("Box", box);
//        geom.setMaterial(createMaterial(ColorRGBA.Brown));
        geom.setMaterial(createTexturedMaterial("assets/textures/vagon.png", ColorRGBA.Red));
        return geom;
    }

    public Geometry createTecho(){
        Box box = new Box(3.5F, 0.2F, 3F);
        Geometry geom = new Geometry("Box", box);
        geom.setMaterial(createTexturedMaterial("assets/textures/madera.png", ColorRGBA.DarkGray));
        return geom;
    }

    public Geometry createHumo(){
        Sphere sphere = new Sphere(30, 30, 0.45F);
        Geometry geom = new Geometry("Sphere", sphere);
        geom.setMaterial(createTexturedMaterial2("assets/textures/humo.png"));
        return geom;
    }

    public Geometry createTriangularPrism() {
        float base = 3.2F;
        float height = 3.5F;
        float depth = 4F;

        // Crear la malla para el prisma triangular
        Mesh mesh = new Mesh();

        // Definir los vértices del triángulo rectángulo (base y altura)
        Vector3f[] vertices = new Vector3f[6];
        vertices[0] = new Vector3f(0, 0, 0);          // Vértice inferior izquierdo (ángulo recto)
        vertices[1] = new Vector3f(base, 0, 0);       // Vértice inferior derecho
        vertices[2] = new Vector3f(0, height, 0);     // Vértice superior

        vertices[3] = new Vector3f(0, 0, depth);      // Repetir los mismos vértices pero con profundidad
        vertices[4] = new Vector3f(base, 0, depth);
        vertices[5] = new Vector3f(0, height, depth);

        // Definir los índices para las caras del prisma
        int[] indices = new int[] {
                // Cara frontal (triángulo rectángulo)
                0, 1, 2,
                // Cara trasera (triángulo rectángulo)
                3, 5, 4,
                // Cara izquierda (rectángulo vertical)
                0, 2, 5, 0, 5, 3,
                // Cara derecha (rectángulo inclinado)
                1, 4, 5, 1, 5, 2,
                // Cara inferior (rectángulo horizontal)
                0, 3, 4, 0, 4, 1
        };

        // Calcular las normales para cada vértice
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

        // Asignar los búferes a la malla
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));

        mesh.updateBound();
        mesh.updateCounts();

        // Crear la geometría y asignarle un material
        Geometry geom = new Geometry("TriangularPrism", mesh);
//        geom.setMaterial(createTexturedMaterial("assets/textures/reja.png"));
        geom.setMaterial(createMaterial(new ColorRGBA(0.937f, 0.188f, 0.220f, 1.0f)));

        geom.rotate(0, FastMath.PI, 0);

        return geom;
    }

    public Geometry createWindow() {
        float radius = 1.8f;
        float height = 0.2f;

        // Crear la malla para el prisma hexagonal
        Mesh mesh = new Mesh();

        // Número de lados del hexágono (6 lados)
        int sides = 6;

        // Definir los vértices del prisma hexagonal
        Vector3f[] vertices = new Vector3f[sides * 2];
        for (int i = 0; i < sides; i++) {
            float angle = (float) (i * 2 * Math.PI / sides); // Ángulo para cada vértice
            float x = radius * FastMath.cos(angle);
            float z = radius * FastMath.sin(angle);

            // Vértices de la base inferior
            vertices[i] = new Vector3f(x, 0, z);

            // Vértices de la base superior
            vertices[i + sides] = new Vector3f(x, height, z);
        }

        // Definir los índices para las caras del prisma
        int[] indices = new int[sides * 12]; // 6 caras laterales (2 triángulos cada una) + 2 bases (6 triángulos cada una)
        int index = 0;

        // Caras laterales
        for (int i = 0; i < sides; i++) {
            int next = (i + 1) % sides; // Siguiente vértice en el hexágono

            // Primer triángulo
            indices[index++] = i;
            indices[index++] = next;
            indices[index++] = i + sides;

            // Segundo triángulo
            indices[index++] = next;
            indices[index++] = next + sides;
            indices[index++] = i + sides;
        }

        // Base inferior
        for (int i = 0; i < sides; i++) {
            int next = (i + 1) % sides;

            indices[index++] = 0; // Centro del hexágono
            indices[index++] = next;
            indices[index++] = i;
        }

        // Base superior
        for (int i = 0; i < sides; i++) {
            int next = (i + 1) % sides;

            indices[index++] = sides; // Centro del hexágono superior
            indices[index++] = sides + i;
            indices[index++] = sides + next;
        }

        // Calcular las normales para cada vértice
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

        // Asignar los búferes a la malla
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));

        mesh.updateBound();
        mesh.updateCounts();

        // Crear la geometría y asignarle un material
        Geometry geom = new Geometry("HexagonalPrism", mesh);
        geom.rotate(FastMath.PI / 2, 0, 0);
        geom.setMaterial(createMaterial(new ColorRGBA(51 / 255f, 153 / 255f, 255 / 255f, 0.6f))); // Puedes cambiar el color
//        geom.setMaterial(createTexturedMaterial("assets/textures/glass.png"));
//        geom.setQueueBucket(com.jme3.renderer.queue.RenderQueue.Bucket.Transparent);

        return geom;
    }

    public Geometry createTexturedWindow() {
        Box box = new Box(2F, 1.3f, 0.05f);
        Geometry geom = new Geometry("TexturedWindow", box);
        geom.rotate(0, FastMath.PI / 2, 0);
        // Aplicar el material a la geometría
        geom.setMaterial(createTexturedMaterial("assets/textures/glass.png", ColorRGBA.Cyan));
        geom.setQueueBucket(com.jme3.renderer.queue.RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Material createTexturedMaterial2(String x) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        // Cargar la textura desde los recursos
        Texture texture = assetManager.loadTexture(x);
        material.setTexture("ColorMap", texture); // Asignar la textura al material

        // Opcional: Configurar transparencia si la textura tiene canal alfa
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        return material;
    }

    private Material createTexturedMaterial(String x, ColorRGBA color) {
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        // Habilitar el uso de colores del material
        material.setBoolean("UseMaterialColors", true);

        // Configurar colores del material
        material.setColor("Diffuse", new ColorRGBA(color)); // Color difuso (base)
        material.setColor("Ambient", new ColorRGBA(color)); // Color ambiente
        material.setColor("Specular", ColorRGBA.White); // Color especular (reflejos brillantes)

        // Aumentar el brillo
        material.setFloat("Shininess", 128f); // Reflejos más concentrados

        // Cargar la textura desde los recursos
        Texture texture = assetManager.loadTexture(x);
        material.setTexture("DiffuseMap", texture); // Asignar la textura al material

        // Opcional: Configurar transparencia si la textura tiene canal alfa
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        return material;
    }

    private Material createMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", new ColorRGBA(color));
        mat.setColor("Ambient", new ColorRGBA(color));
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 128f);
        return mat;
    }


}

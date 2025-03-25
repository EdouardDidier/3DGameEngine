package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {
    private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
    private MeshResource resource;
    private String fileName;

    public Mesh(String fileName) {
        this.fileName = fileName;

        MeshResource oldResource = loadedModels.get(fileName);

        if(oldResource != null) {
            resource = oldResource;
            resource.addReference();
        }
        else {
            loadMesh(fileName);
            loadedModels.put(fileName, resource);
        }
    }

    public Mesh(Vertex[] vertices, int[] indices) {
        this(vertices, indices, false);
    }

    public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals) {
        fileName = "";  //TODO Improve
        addVertices(vertices, indices, calcNormals);
    }

    public void destroy() { //TODO: Improve
        if(resource.removeReference() && !fileName.isEmpty()) {
            loadedModels.get(fileName).destroy();
            loadedModels.remove(fileName); //TODO: Improve
        }
    }

    private void addVertices(Vertex[] vertices, int[] indices) {
        addVertices(vertices, indices, false);
    }

    private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {
        if (calcNormals) { //TODO: Useless?
            calcNormals(vertices, indices);
        }
        calcTangents(vertices, indices); //TODO: Add booleans "calcNormals"?

        resource = new MeshResource(indices.length);

        glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
    }

    public void draw() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
        glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
    }

    private void calcNormals(Vertex[] vertices, int[] indices) { //TODO: Remove?
         for(int i = 0; i < indices.length; i += 3) {
             int i0 = indices[i];
             int i1 = indices[i + 1];
             int i2 = indices[i + 2];

             Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
             Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());

             Vector3f normal = v1.cross(v2).normalized();

             vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
             vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
             vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
         }

        for (Vertex vertex : vertices) {
            vertex.setNormal(vertex.getNormal().normalized());
        }
    }

    public void calcTangents(Vertex[] vertices, int[] indices) //TODO: Remove?
    {
        for(int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3f edge1 = vertices[i1].getPos().sub(vertices[i0].getPos());
            Vector3f edge2 = vertices[i2].getPos().sub(vertices[i0].getPos());

            float deltaU1 = vertices[i1].getTextCoord().getX() - vertices[i0].getTextCoord().getX();
            float deltaV1 = vertices[i1].getTextCoord().getY() - vertices[i0].getTextCoord().getY();
            float deltaU2 = vertices[i2].getTextCoord().getX() - vertices[i0].getTextCoord().getX();
            float deltaV2 = vertices[i2].getTextCoord().getY() - vertices[i0].getTextCoord().getY();

            float dividend = (deltaU1*deltaV2 - deltaU2*deltaV1);
            //TODO: The first 0.0f may need to be changed to 1.0f here.
            float f = dividend == 0 ? 0.0f : 1.0f/dividend;

            Vector3f tangent = new Vector3f(0,0,0);
            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));
            //tangent.normalized();

            vertices[i0].setTangent(vertices[i0].getTangent().add(tangent));
            vertices[i1].setTangent(vertices[i1].getTangent().add(tangent));
            vertices[i2].setTangent(vertices[i2].getTangent().add(tangent));
        }

        for (Vertex vertex : vertices)
            vertex.setTangent(vertex.getTangent().normalized());
    }

    //Resource Loader
    private Mesh loadMesh(String fileName) {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];

        if (!ext.equals("obj")) {
            System.err.println("Error: File format not supported for mesh data: " + ext);
            new Exception().printStackTrace();
            System.exit(1);
        }

        OBJModel test = new OBJModel("./res/models/" + fileName); //TODO: Rewrite
        IndexedModel model = test.toIndexedModel();
        //model.calcNormals(); TODO: Useless?

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();

        for(int i = 0; i < model.getPositions().size(); i++) {
            vertices.add(new Vertex(
                    model.getPositions().get(i),
                    model.getTexCoords().get(i),
                    model.getNormals().get(i),
                    model.getTangents().get(i))
            );
        }

        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);

        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);

        addVertices(vertexData, Util.toIntArray(indexData), false);

        return null; //TODO: Improve code
    }
}

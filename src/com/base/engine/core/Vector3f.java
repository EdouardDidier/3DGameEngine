package com.base.engine.core;

public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z) {
        this.setPos(x, y, z);
    }

    //Mathematics
    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float max() {
        return Math.max(x, Math.max(y, z));
    }

    public float dot(Vector3f r) {
        return x * r.getX() + y * r.getY() + z * r.getZ();
    }

    public Vector3f cross(Vector3f r) {
        return new Vector3f(
                y * r.getZ() - z * r.getY(),
                z * r.getX() - x * r.getZ(),
                x * r.getY() - y * r.getX()
        );
    }

    public Vector3f normalized() {
        float length = this.length();

        if(length == 0)
            return new Vector3f(1, 0, 0);

        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f abs() {
        return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vector3f rotate(Vector3f axis, float angle) {
        float sinAngle = (float)Math.sin(-angle);
        float cosAngle = (float)Math.cos(-angle);

        return this.cross(axis.mul(sinAngle))                               //Rotation on local X
                .add((this.mul(cosAngle))                                   //Rotation on local Z
                        .add(axis.mul(this.dot(axis.mul(1 - cosAngle)))));  //Rotation on local Y
    }

    public Vector3f rotate(Quaternion rotation) {
        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.mul(this).mul(conjugate);

        return new Vector3f(w.getX(), w.getY(), w.getZ());
    }

    public Vector3f lerp(Vector3f dest, float lerpFactor) {
        return dest.sub(this).mul(lerpFactor).add(this);
    }

    public Vector3f add(Vector3f r) {
        return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
    }

    public Vector3f add(float r) {
        return new Vector3f(x + r, y + r, z + r);
    }

    public Vector3f sub(Vector3f r) {
        return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
    }

    public Vector3f sub(float r) {
        return new Vector3f(x - r, y - r, z - r);
    }

    public Vector3f mul(Vector3f r) {
        return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
    }

    public Vector3f mul(float r) {
        return new Vector3f(x * r, y * r, z * r);
    }

    public Vector3f div(Vector3f r) {
        return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
    }

    public Vector3f div(float r) {
        return new Vector3f(x / r, y / r, z / r);
    }

    //Display
    public String toString() {
        return "(" + x + " " + y + " " + z + ")";
    }

    //Getter & Setters
    public Vector3f set(float x, float y, float z) {this.x = x; this.y = y; this.z = z; return this;}
    public Vector3f set(Vector3f r) {this.x = r.getX(); this.y = r.getY(); this.z = r.getZ(); return this;}

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector2f getXY() {return new Vector2f(x, y);}
    public Vector2f getYZ() {return new Vector2f(y, z);}
    public Vector2f getZX() {return new Vector2f(z, x);}

    public Vector2f getYX() {return new Vector2f(y, x);}
    public Vector2f getZY() {return new Vector2f(z, y);}
    public Vector2f getXZ() {return new Vector2f(x, z);}

    public boolean equals(Vector3f r) {
        return x == r.getX() && y == r.getY() && z == r.getZ();
    }
}

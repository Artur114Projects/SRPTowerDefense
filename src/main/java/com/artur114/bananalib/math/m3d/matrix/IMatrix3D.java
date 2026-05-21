package com.artur114.bananalib.math.m3d.matrix;

public interface IMatrix3D {
    IMatrix3D scale(int x, int y, int z);
    IMatrix3D scale(float x, float y, float z);
    IMatrix3D scale(double x, double y, double z);
//    IMatrix3D scale(Vec3D vec);
//    IMatrix3D scale(Vec3I vec);
    IMatrix3D translate(int x, int y, int z);
    IMatrix3D translate(float x, float y, float z);
    IMatrix3D translate(double x, double y, double z);
//    IMatrix3D translate(Vec3D vec);
//    IMatrix3D translate(Vec3I vec);
    IMatrix3D rotate(double dredges, int x, int y, int z);
    IMatrix3D rotate(double dredges, float x, float y, float z);
    IMatrix3D rotate(double dredges, double x, double y, double z);
//    IMatrix3D rotate(double dredges, Vec3D vec);
//    IMatrix3D rotate(double dredges, Vec3I vec);
//    IMatrix3D rotateAround(Vec3D point, double dredges, int x, int y, int z);
//    IMatrix3D rotateAround(Vec3D point, double dredges, float x, float y, float z);
//    IMatrix3D rotateAround(Vec3D point, double dredges, double x, double y, double z);
//    IMatrix3D rotateAround(Vec3D point, double dredges, Vec3D vec);
//    IMatrix3D rotateAround(Vec3D point, double dredges, Vec3I vec);
//    IMatrix3D rotateAround(Vec3I point, double dredges, int x, int y, int z);
//    IMatrix3D rotateAround(Vec3I point, double dredges, float x, float y, float z);
//    IMatrix3D rotateAround(Vec3I point, double dredges, double x, double y, double z);
//    IMatrix3D rotateAround(Vec3I point, double dredges, Vec3D vec);
//    IMatrix3D rotateAround(Vec3I point, double dredges, Vec3I vec);
}

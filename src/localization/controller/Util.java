/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import java.awt.geom.Point2D;

/**
 *
 * @author lubuntu
 */
public class Util {

    public static double max(double x, double y, double z) {
        if (x > y) {
            if (x > z) {
                return x;
            } else {
                return z;
            }
        } else if (y > z) {
            return y;

        } else {
            return z;
        }
    }

    public static double min(double x, double y, double z) {
        if (x < y) {
            if (x < z) {
                return x;
            } else {
                return z;
            }
        } else if (y < z) {
            return y;

        } else {
            return z;
        }
    }

    public static double average(double x, double y, double z) {
        return (x + y + z) / 3;
    }

    public static double Determinant2D(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }

    public static double Determinant3D(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][2] * matrix[2][0]
                + matrix[0][2] * matrix[1][0] * matrix[2][1] - matrix[0][2] * matrix[1][1] * matrix[2][0]
                - matrix[0][1] * matrix[1][0] * matrix[2][2] - matrix[0][0] * matrix[1][2] * matrix[2][1];
    }

    public static Point2D.Double equirectangularProjection(double lon, double lat) {
        Point2D.Double newPoint = new Point2D.Double();
        double R = 6378137.;//km
        double lonAnchor = 23;
        double latAnchor = 37;

        newPoint.x = (lon - lonAnchor) * Math.cos(latAnchor) * Math.toRadians(R);
        newPoint.y = (lat - latAnchor) * Math.toRadians(R);
        return newPoint;
    }

    public static double dotProduct(Point2D p1, Point2D p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    public static double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    public static double getAngle(Point2D center, double radius, Point2D point) {
        Point2D.Double v1 = new Point2D.Double(radius,
                0);
        Point2D.Double v2 = new Point2D.Double(point.getX() - center.getX(),
                point.getY() - center.getY());
        double d1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y);
        double d2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y);
        System.out.println("AB *  BC = " + dotProduct(v1, v2));
        System.out.println("|AB| = " + d1 + " |BC| = " + d2);
        System.out.println("|AB| * |BC| = " + (d1 * d2));
        double cosA = (dotProduct(v1, v2)) / (d1 * d2);
        System.out.println("cosA = " + cosA);
        cosA = toDegrees(Math.acos(cosA));
        System.out.println("θ = " + cosA);
        if (point.getY() < 0) {
            return 360 - cosA;
        
        }
        return cosA;
    }
}

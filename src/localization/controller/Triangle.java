/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.controller;

import localization.database.Point5D;

/**
 * A triangle that is formed from 3 Point5D objects
 * @author lubuntu
 */
public class Triangle {

    private Point5D point[], representative;
    private double area;

    /**
     * Create a triangle from the given points
     * @param nodes the points that form the triangle
     */
    public Triangle(Point5D nodes[]) {
        this.point = nodes;
        this.representative = centroidRep();
        this.area = area();
    }

    /**
     * Calculate triangle's representative using the bounding box method
     * @return the representative
     */
    public final Point5D boundingBoxRep() {
        double maxXg = Util.max(point[0].getLon(), point[1].getLon(), point[2].getLon());
        double minXg = Util.min(point[0].getLon(), point[1].getLon(), point[2].getLon());
        double maxYg = Util.max(point[0].getLat(), point[1].getLat(), point[2].getLat());
        double minYg = Util.min(point[0].getLat(), point[1].getLat(), point[2].getLat());
        double lon = (maxXg - minXg) / 2 + minXg;
        double lat = (maxYg - minYg) / 2 + minYg;

        double alt = Util.average(point[0].getAlt(), point[1].getAlt(), point[2].getAlt());
        double x = (Util.max(point[0].getX(), point[1].getX(), point[2].getX()) + Util.min(point[0].getX(), point[1].getX(), point[2].getX())) / 2;
        double y = (Util.max(point[0].getY(), point[1].getY(), point[2].getY()) + Util.min(point[0].getY(), point[1].getY(), point[2].getY())) / 2;
        return new Point5D(x, y, lat, lon, alt, 0);
    }

    /**
     * Calculate triangle's representative using the centroid method
     * @return the representative
     */
    public final Point5D centroidRep() {
        double lon = (point[0].getLon() + point[1].getLon() + point[2].getLon()) / 3;
        double lat = (point[0].getLat() + point[1].getLat() + point[2].getLat()) / 3;
        double alt = Util.average(point[0].getAlt(), point[1].getAlt(), point[2].getAlt());
        return new Point5D(0, 0, lat, lon, alt, 0);

    }

    /**
     * Calculate the area of the triangle
     * @return 
     */
    private double area() {
        double[][] matrix = new double[3][3];
        for (int i = 0; i < 3; i++) {
//            Point2D.Double pp = Util.equirectangularProjection(point[i].getLon(), point[i].getLat());
//            matrix[0][i] = pp.x;
//            matrix[1][i] = pp.y;
            matrix[0][i] = point[i].getLon();
            matrix[1][i] = point[i].getLat();
            matrix[2][i] = 1;
        }
        return (Math.abs(Util.Determinant3D(matrix)) / 2);
    }

    //l1 = ((y2-y3)(x-x3)+(x3-x2)(y-y3)) / ((y2-y3)(x1-x3)+(x3-x2)(y1-y3))
    //l2 = ((y3-y1)(x-x3)+(x1-x3)(y-y3)) / ((y2-y3)(x1-x3)+(x3-x2)(y1-y3))
    //l1 = 1 - l1 + l2
    /**
     * Check if point with coordinates x,y is inside the triangle
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @return true if point is in the triangle. Else return false
     */
    public boolean include(double x, double y) {
        double l1, l2, l3;

        l1 = ((this.point[1].getY() - this.point[2].getY()) * (x - this.point[2].getX())
                + (this.point[2].getX() - this.point[1].getX()) * (y - this.point[2].getY()))
                / ((this.point[1].getY() - this.point[2].getY()) * (this.point[0].getX() - this.point[2].getX())
                + (this.point[2].getX() - this.point[1].getX()) * (this.point[0].getY() - this.point[2].getY()));

        l2 = ((this.point[2].getY() - this.point[0].getY()) * (x - this.point[2].getX())
                + (this.point[0].getX() - this.point[2].getX()) * (y - this.point[2].getY()))
                / ((this.point[1].getY() - this.point[2].getY()) * (this.point[0].getX() - this.point[2].getX())
                + (this.point[2].getX() - this.point[1].getX()) * (this.point[0].getY() - this.point[2].getY()));

        l3 = 1 - l1 - l2;
//        System.out.println("λ1: " + l1);
//        System.out.println("λ2: " + l2);
//        System.out.println("λ3: " + l3);
//        System.out.println("λ1+λ2+λ3 =  " + (l1 + l2 + l3));
        return l1 >= 0 && l1 <= 1 && l2 >= 0 && l2 <= 1 && l3 >= 0 && l3 <= 1;
    }

    /**
     * Check if @point is inside the triangle
     * @param point the point to be checked
     * @return true if point is in the triangle. Else return false
     */
    public boolean include(Point5D point) {
        double l1, l2, l3;

        l1 = ((this.point[1].getY() - this.point[2].getY()) * (point.getX() - this.point[2].getX())
                + (this.point[2].getX() - this.point[1].getX()) * (point.getY() - this.point[2].getY()))
                / ((this.point[1].getY() - this.point[2].getY()) * (this.point[0].getX() - this.point[2].getX())
                + (this.point[2].getX() - this.point[1].getX()) * (this.point[0].getY() - this.point[2].getY()));

        l2 = ((this.point[2].getY() - this.point[0].getY()) * (point.getX() - this.point[2].getX())
                + (this.point[0].getX() - this.point[2].getX()) * (point.getY() - this.point[2].getY()))
                / ((this.point[1].getY() - this.point[2].getY()) * (this.point[0].getX() - this.point[2].getX())
                + (this.point[2].getX() - this.point[1].getX()) * (this.point[0].getY() - this.point[2].getY()));

        l3 = 1 - l1 - l2;
//        System.out.println("λ1: " + l1);
//        System.out.println("λ2: " + l2);
//        System.out.println("λ3: " + l3);
//        System.out.println("λ1+λ2+λ3 =  " + (l1 + l2 + l3));
        return l1 >= 0 && l1 <= 1 && l2 >= 0 && l2 <= 1 && l3 >= 0 && l3 <= 1;
    }

    public boolean include2(Point5D point) {
        double l1, l2, l3;

        l1 = (this.point[0].getX() * (this.point[2].getY() - this.point[0].getY())
                + (point.getY() - this.point[0].getY()) * (this.point[2].getX() - this.point[0].getX())
                - point.getX() * (this.point[2].getY() - this.point[0].getY()))
                / ((this.point[1].getY() - this.point[0].getY()) * (this.point[2].getX() - this.point[0].getX())
                - (this.point[1].getX() - this.point[0].getX()) * (this.point[2].getY() - this.point[0].getY()));

        l2 = (point.getY() - this.point[0].getY()
                - l1 * (this.point[1].getY() - this.point[0].getY()))
                / (this.point[2].getY() - this.point[0].getY());

        l3 = 1 - l1 - l2;

//        System.out.println("λ1: " + l1);
//        System.out.println("λ2: " + l2);
//        System.out.println("λ3: " + l3);
//        System.out.println("λ1+λ2+λ3 =  " + (l1 + l2 + l3));
        return l1 >= 0 && l1 <= 1 && l2 >= 0 && l2 <= 1 && l3 >= 0 && l3 <= 1;
    }

    public Point5D[] getPoint() {
        return point;
    }

    public Point5D getRepresentative() {
        return representative;
    }

    public double getArea() {
        return area;
    }

    public void setPoint(Point5D[] node) {
        this.point = node;
    }

    public void setRepresentative(Point5D representative) {
        this.representative = representative;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return point[0].toString() + "\n\n"
                + point[1].toString() + "\n\n"
                + point[2].toString() + "\n\n"
                + "Area=" + String.format("%.15f", area) + "\n"
                + "Representative=\n"
                + representative.toString();
    }
}

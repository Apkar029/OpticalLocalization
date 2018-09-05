/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author lubuntu
 */
@Entity
public class Point5D implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "X_", nullable = false)
    private double x;
    @Column(name = "Y_", nullable = false)
    private double y;
    @Column(name = "LON_", nullable = false)
    private double lon;
    @Column(name = "LAT_", nullable = false)
    private double lat;
    @Column(name = "ALT_", nullable = false)
    private double alt;
    @Column(name = "VIEW_", nullable = false)
    private int view;

    public Point5D() {

    }

    public Point5D(double x, double y, double xg, double yg, double hg, int view) {
        this.x = x;
        this.y = y;
        this.lon = xg;
        this.lat = yg;
        this.alt = hg;
        this.view = view;
    }

    public Point5D(double s, double t) {

    }

    public Point5D multiply(double value) {
        x = x * value;
        y = y * value;
        lat = lat * value;
        lon = lon * value;
        alt = alt * value;

        return new Point5D(x * value, y * value, lon * value, lat * value, alt * value, 0);
    }

//    public Point5D add(Point5D value) {
//        return new Point5D(x + value.x, +value.y, lon + value.lon, lat + value.lat, alt + value.alt);
//    }
//
//    public Point5D substract(Point5D value) {
//        return new Point5D(x - value.x, y - value.y, lon - value.lon, lat - value.lat, alt - value.alt);
//    }

    @Override
    public String toString() {
        return x + "," + y + "\n" + lon + ", " + lat + ", " + alt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public int getImg() {
        return view;
    }

    public void setImg(int img) {
        this.view = img;
    }

}

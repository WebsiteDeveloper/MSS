/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mss;

/**
 *
 * @author Bernhard Sirlinger
 */
public class Vec2 {
    public double x;
    public double y;
    
    public Vec2() {
        this.x = 0.0;
        this.y = 0.0;
    }
    
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
    
    public Vec2 negate() {
        this.x = -this.x;
        this.y = -this.y;
        
        return this;
    }
    
    public static Vec2 negate(Vec2 vec) {
        return new Vec2(-vec.x, -vec.y);
    }
    
    public static Vec2 add(Vec2 vec1, Vec2 vec2) {
        return new Vec2(vec1.x + vec2.x, vec1.y + vec2.y);
    }
    
    public static Vec2 skalarMultiply(double r, Vec2 vec) {
        return (new Vec2(r * vec.x, r * vec.y));
    }
}

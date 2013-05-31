/*
 * 
 */
package mss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Bernhard Sirlinger
 */
public class Rechner {
    
    public static final double G  = 6.67384e-11;
    public static final boolean debug = true;
    public static final boolean writeToFile = true;
    
    
    public static void main(String[] args) {
        Planet sun = new Planet(0, 0, 1.0e9, 0.5);
        Planet planet = new Planet(1, 0, 100.0, 0.1);
        
        Rechner rechner = new Rechner();
        rechner.berechnen(sun.getCoords(), planet.getCoords(), sun.getMass());
    }
    
    public void berechnen(Vec2 coords1, Vec2 coords2, double m1) {
        Vec2 a, a1, a2, a3;
        Vec2 v = new Vec2(0, 0.295), v1, v2, v3;
        Vec2 coords = coords2, temp_coords;
        Vec2 r, r1, r2, r3;

        double delta_t = 0.1; //delta_t in Sekunden
        double t = 0; // t in Sekunden
        double t_max = 800;
        double[][] results = new double[(int)Math.ceil(t_max/delta_t)][8];
        
        for(int counter = 0; t <= t_max && counter < results.length; counter++) {
            // Berechnung des Ortsvektors
            r = Vec2.add(coords1, Vec2.negate(coords2));
            results[counter][0] = r.x;
            results[counter][1] = r.y;
            if(Rechner.debug) {
                System.out.println("Vec2 r = (" + r.x + " , " + r.y + ")");
            }
            
            /* Runge Kutta Start */
            // Berechnung der Beschleunigung
            a = this.a(m1, r);
            results[counter][2] = a.x;
            results[counter][3] = a.y;
            if(Rechner.debug) {
                System.out.println("Vec2 a = (" + a.x + " , " + a.y + ")");
            }
            
            v1 = Vec2.add(v, Vec2.skalarMultiply(delta_t/2, a));
            temp_coords = Vec2.add(coords, Vec2.skalarMultiply(delta_t, v1));
            r1 = Vec2.add(coords1, Vec2.negate(temp_coords));
            a1 = this.a(m1, r1);
            
            v2 = Vec2.add(v, Vec2.skalarMultiply(delta_t/2, a1));
            temp_coords = Vec2.add(coords, Vec2.skalarMultiply(delta_t, v2));
            r2 = Vec2.add(coords1, Vec2.negate(temp_coords));
            a2 = this.a(m1, r2);
            
            v3 = Vec2.add(v, Vec2.skalarMultiply(delta_t, a2));
            temp_coords = Vec2.add(coords, Vec2.skalarMultiply(delta_t, v3));
            r3 = Vec2.add(coords1, Vec2.negate(temp_coords));
            a3 = this.a(m1, r3);
            
            v = Vec2.add(v, Vec2.skalarMultiply(delta_t * 1.0/6, Vec2.add(Vec2.add(a, Vec2.skalarMultiply(2, Vec2.add(a1, a2))), a3)));
            /* Runge Kutta Ende */
            results[counter][4] = v.x;
            results[counter][5] = v.y;
            if(Rechner.debug) {
                System.out.println("Vec2 v = (" + v.x + " , " + v.y + ")");
            }
            coords = Vec2.add(coords, Vec2.skalarMultiply(delta_t, v));
            results[counter][6] = coords.x;
            results[counter][7] = coords.y;
            if(Rechner.debug) {
                System.out.println("Vec2 coords = (" + coords.x + " , " + coords.y + ")");
            }
            
            if(coords.x == Double.POSITIVE_INFINITY || coords.y == Double.POSITIVE_INFINITY) {
                break;
            }
            
            coords2 = coords;
            t += delta_t;
        }
        
        if(Rechner.writeToFile) {
            this.writeDataToFile("test1.txt", results);
        }
		}
		
		public Vec2 a(double m, Vec2 r_ort) {
        double r = r_ort.magnitude();
        double skalar = Rechner.G * (m/(Math.pow(r, 3)));
        System.out.println("Skalar: " + skalar);
        Vec2 erg = new Vec2(skalar * r_ort.x, skalar * r_ort.y);
        return erg;
		}
    
    private void writeDataToFile(String filename, double[][] values) {
        ArrayList<String> data = new ArrayList<>(5);
        data.add("");
        
        for(double[] value : values) {
            int index = data.size() - 1;
            data.set(index, data.get(index) + value[0] + " " + value[1] + " ");
            data.set(index, data.get(index) + value[2] + " " + value[3] + " ");
            data.set(index, data.get(index) + value[4] + " " + value[5] + " ");
            data.set(index, data.get(index) + value[6] + " " + value[7] + "\n");
            if(data.get(index).length() >= 6000) {
                data.add("");
            }
        }
        
        try {
            FileWriter writer = new FileWriter(filename);
            while(data.size() > 0) {
                writer.append(data.get(0));
                data.remove(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
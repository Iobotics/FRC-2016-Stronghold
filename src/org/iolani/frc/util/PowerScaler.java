package org.iolani.frc.util;

public class PowerScaler {

    private final PowerPoint _scale[];
    public PowerScaler(PowerPoint scale[]) {
        if(scale.length < 2) throw new IllegalArgumentException("Two or more point are required");
        _scale = scale;
    }
    
    public double get(double input){
        double cmd = Math.abs(input);
        if(cmd >= _scale[_scale.length - 1].x) {cmd = _scale[_scale.length - 1].y;}
        else {
            int i;
            for(i = 0; i < _scale.length-2; i++) {
                if(cmd >= _scale[i].x && cmd < _scale[i + 1].x) {
                // cmd = ((Y2 - Y1) / (X2 - X1)) (cmd - X1) + Y1
                cmd = ((_scale[i + 1].y - _scale[i].y) * (cmd - _scale[i].x)
                / (_scale[i + 1].x - _scale[i].x))
                + _scale[i].y;
                break;
                }
            }
        }
        return cmd * Utility.sign(input);
    }
    
    public static class PowerPoint {
        public final double x;
        public final double y;
        
        public PowerPoint(double ix, double iy) {
            this.x = ix;
            this.y = iy;
        }
    }
}

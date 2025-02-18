/**
 * User: Samy Badjoudj
 */
public class Computer {

    public int compute(int symbX, int symbY) {

        int x,y;
        x = symbX + 2;

        if (x >  symbY) {
            return x *  symbY + 4;
        }
        x = x + 4;
        y = symbY + 1;

        if (y ==  x) {
            return x *  y;
        } else {
            return 2*x +  y;
        }
    }
}

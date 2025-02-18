package company;

import java.io.IOException;

public class Main {

    public static final int width = (84*32)/4;
    public static final int height = (32*18);
    public static final int padding_y = (32*4);
    public static final int FRAME_PER_SEC = 60;

    public static void main(String[] args) throws IOException {
	// write your code here
       Window.create(new Game(), width, height,"Scroll it");
    }
}

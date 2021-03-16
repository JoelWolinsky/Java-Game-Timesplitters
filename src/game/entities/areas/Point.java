package game.entities.areas;

public class Point<S, T> {

        private final int x;
        private final int y;
        private int speed;

        public Point(int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed=speed;
        }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}

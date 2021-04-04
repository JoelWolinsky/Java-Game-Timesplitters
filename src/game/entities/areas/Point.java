package game.entities.areas;

public class Point<S, T> {

        private int x;
        private int y;
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

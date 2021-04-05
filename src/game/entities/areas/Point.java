package game.entities.areas;

public class Point<S, T> {

        private int x;
        private int y;
        private float speed;

        public Point(int x, int y, float speed) {
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

    public float getSpeed() {
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

public class Projectile {

    private Square location;


    public Projectile(Square location) {

        this.location = location;
    }

    public Square getLocation() {
        return location;
    }

    public void setLocation(Square location) {
        this.location = location;
    }
}
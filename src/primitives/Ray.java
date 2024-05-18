package primitives;

/**
 * Class representing a ray in 3D space.
 */
public class Ray {
    private final Point head;
    private final Vector direction;

    /**
     * Constructs a new Ray with the given head (starting point) and direction.
     * @param head The starting point of the Ray.
     * @param direction The direction Vector of the Ray.
     */
    public Ray(Point head, Vector direction) {
        if (direction.length() == 0)
            throw new IllegalArgumentException("Ray direction vector cannot be the zero vector.");
        this.head = head;
        this.direction = direction.normalize();
    }

    public Vector getDirection() {
        return direction;
    }

    public Point getHead() {
        return head;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }
}

package advisor.pagination;

public class PageTurner {
    private TurningMethods turningMethods;

    public void setTurningMethods(TurningMethods turningMethods) {
        this.turningMethods = turningMethods;
    }

    public void turnPageForward() {
        this.turningMethods.turnPageForward();
    }

    public void turnPageBackward() {
        this.turningMethods.turnPageBackward();
    }
}

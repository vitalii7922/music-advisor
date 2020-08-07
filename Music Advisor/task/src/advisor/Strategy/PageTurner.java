package advisor.Strategy;

import java.util.List;

public class PageTurner {
    TurningMethods turningMethods;

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

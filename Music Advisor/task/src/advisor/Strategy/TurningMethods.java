package advisor.Strategy;

import java.util.List;

abstract class TurningMethods {
    private int elementsNumber;
    private List<String> output;
    private int currentPage;
    private int pagesNumber;

    public TurningMethods(int elementsNumber, List<String> output) {
        this.elementsNumber = elementsNumber;
        this.output = output;
        if (elementsNumber != 0) {
            pagesNumber = output.size() / elementsNumber;
        }
    }

    public void turnPageForward() {
        if (output.size() > elementsNumber) {
            if ((currentPage + 1) * elementsNumber <= output.size()) {
                currentPage++;
                printPage();
            } else {
                System.out.println("No more pages");
            }
        } else {
            output.forEach(System.out::println);
        }
    }

    public void turnPageBackward() {
        if (output.size() > elementsNumber) {
            if (currentPage > 1) {
                currentPage--;
                printPage();
            } else {
                System.out.println("No more pages");
            }
        } else {
            output.forEach(System.out::println);
        }
    }

    private void printPage() {
        for (int i = (currentPage - 1) * elementsNumber; i < elementsNumber * currentPage; i++) {
            System.out.println(output.get(i));
        }
        System.out.println("---PAGE " + currentPage + " OF " + pagesNumber + "--");
    }
}

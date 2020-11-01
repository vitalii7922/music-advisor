package advisor.pagination;

import java.util.List;

abstract class TurningMethods {
    private int elementsNumber; //amount of elements per page
    private List<String> output;
    private int currentPage;
    private int pagesNumber;

    TurningMethods(int elementsNumber, List<String> output) {
        this.elementsNumber = elementsNumber;
        this.output = output;
        if (elementsNumber != 0) {
            pagesNumber = (output.size() / elementsNumber) + (output.size() % elementsNumber == 0 ? 0 : 1);
        }
    }

    void turnPageForward() {
        int lastElement;
        if (output.size() > elementsNumber) {
            if ((currentPage + 1) <= pagesNumber) {
                currentPage++;
                lastElement = output.size() / (currentPage * elementsNumber) == 0 ?
                        output.size() :
                        elementsNumber * currentPage;
                printPage(lastElement);
            } else {
                System.out.println("No more pages");
            }
        } else {
            output.forEach(System.out::println);
        }
    }

    void turnPageBackward() {
        if (output.size() > elementsNumber) {
            if (currentPage > 1) {
                currentPage--;
                printPage(currentPage * elementsNumber);
            } else {
                System.out.println("No more pages.");
            }
        } else {
            output.forEach(System.out::println);
        }
    }

    private void printPage(int lastElement) {
        for (int i = (currentPage - 1) * elementsNumber; i < lastElement; i++) {
            System.out.println(output.get(i));
        }
        System.out.println("---PAGE " + currentPage + " OF " + pagesNumber + "---");
    }
}

package advisor.Strategy;

import java.util.List;

public class TurningPagesCategories implements TurningMethods {
    private int elementsNumber;
    private List<String> output;
    private int currentPage;
    private int pagesNumber;

    public TurningPagesCategories(int elementsNumber, List<String> output) {
        this.elementsNumber = elementsNumber;
        this.output = output;
        if (elementsNumber != 0) {
            pagesNumber = output.size() / elementsNumber;
        }
    }

    @Override
    public void turnPageForward() {
        if (output.size() > elementsNumber) {
            if ((currentPage + 1) * elementsNumber <= output.size()) {
                currentPage++;
                for (int i = (currentPage - 1) * elementsNumber; i < elementsNumber * currentPage; i++) {
                    System.out.println(output.get(i));
                }
                System.out.println("---PAGE " + currentPage + " OF " + pagesNumber + "--");
            } else {
                System.out.println("No more pages");
            }
        } else {
            output.forEach(System.out::println);
        }
    }

    @Override
    public void turnPageBackward() {
        if (output.size() > elementsNumber) {
            if (currentPage > 1) {
                currentPage--;
                for (int i = (currentPage - 1) * elementsNumber; i < elementsNumber * currentPage; i++) {
                    System.out.println(output.get(i));
                }
                System.out.println("---PAGE " + currentPage + " OF " + pagesNumber + "--");
            } else {
                System.out.println("No more pages");
            }
        } else {
            output.forEach(System.out::println);
        }
    }
}

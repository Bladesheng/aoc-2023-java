import java.util.List;

public class Day_template extends AocSolver {
    protected Day_template(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_template("resources/Day_template.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        return "hi mom";
    }

    @Override
    protected String runPart2(List<String> lines) {
        return "hi mom";
    }
}

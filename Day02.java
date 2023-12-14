import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Day02 extends AocSolver {
    protected Day02(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day02("resources/day_02.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        Map<String, Integer> LIMITS = new HashMap<>();
        LIMITS.put("red", 12);
        LIMITS.put("green", 13);
        LIMITS.put("blue", 14);

        int total = 0;

        for (String line : lines) {
            boolean isValid = true;

            Game game = new Game(line);

            for (int i = 0; i < game.colorsArr.size(); i++) {
                int cubes = game.cubesArr.get(i);
                String color = game.colorsArr.get(i);

                if (cubes > LIMITS.get(color)) {
                    isValid = false;
                    break;
                }

            }

            if (isValid) {
                total += game.lineNumber;
            }
        }

        return Integer.toString(total);
    }

    @Override
    protected String runPart2(List<String> lines) {
        int total = 0;

        for (String line : lines) {
            Map<String, Integer> minimumCubes = new HashMap<>();
            minimumCubes.put("red", 0);
            minimumCubes.put("green", 0);
            minimumCubes.put("blue", 0);

            Game game = new Game(line);

            for (int i = 0; i < game.colorsArr.size(); i++) {
                int cubes = game.cubesArr.get(i);
                String color = game.colorsArr.get(i);

                if (cubes > minimumCubes.get(color)) {
                    minimumCubes.put(color, cubes);
                }
            }

            int power = minimumCubes.values().stream().reduce(1, (a, b) -> a * b);
            total += power;
        }

        return Integer.toString(total);
    }
}

class Game {
    int lineNumber;
    List<Integer> cubesArr = new ArrayList<>();
    List<String> colorsArr = new ArrayList<>();

    Game(String line) {
        lineNumber = getLineNumber(line);

        Pattern pattern = Pattern.compile("(\\d+)\\s+([a-zA-Z]+)");
        Matcher first = pattern.matcher(line);
        while (first.find()) {
            String cubes = first.group(1);
            String color = first.group(2);
            cubesArr.add(Integer.parseInt(cubes));
            colorsArr.add(color);
        }
    }

    private static int getLineNumber(String line) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher first = pattern.matcher(line);
        first.find();
        return Integer.parseInt(first.group());
    }
}

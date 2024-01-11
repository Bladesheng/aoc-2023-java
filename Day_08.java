import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day_08 extends AocSolver {
    protected Day_08(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_08("resources/day_08.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        String directionsInput = "LLRRRLRLLRLRRLRLRLRRRLLRRLRRRLRRRLRRRLRRRLRRRLRRLRLLRRRLRRLLRLRLLLRRLRRLRLRLRLRRRLRLRRRLRRLLLRRRLLRRLLRRLLRRRLLLLRLRLRRRLRLRRRLRLLLRLRRLRRRLRRRLRRRLRRRLLRRLLLLRRLLRRLLRRLRLRRRLRRRLRRRLRRLRRRLRRLRRLRRLRLRRRLRRLRRRLRRRLRRLRLRRRLRRLLRLRRLRRRLRLRRLRRRLRRLRRLRRRLLRRRR";

        List<Integer> directions = directionsInput
                .chars()
                .mapToObj(c -> c == 'L' ? 0 : 1)
                .toList();

        HashMap<String, String[]> destinationsMap = new HashMap<>();

        for (String line : lines) {
            String group1 = line.substring(0, 3);
            String group2 = line.substring(7, 10);
            String group3 = line.substring(12, 15);

            destinationsMap.put(group1, new String[]{group2, group3});
        }

        int steps = 0;
        String position = "AAA";

        while (!position.equals("ZZZ")) {
            int wrappedIndex = steps % directions.size();

            String[] nextMap = destinationsMap.get(position);

            int nextDirection = directions.get(wrappedIndex);

            position = nextMap[nextDirection];

            steps++;
        }

        return Integer.toString(steps);
    }

    @Override
    protected String runPart2(List<String> lines) {
        String directionsInput = "LLRRRLRLLRLRRLRLRLRRRLLRRLRRRLRRRLRRRLRRRLRRRLRRLRLLRRRLRRLLRLRLLLRRLRRLRLRLRLRRRLRLRRRLRRLLLRRRLLRRLLRRLLRRRLLLLRLRLRRRLRLRRRLRLLLRLRRLRRRLRRRLRRRLRRRLLRRLLLLRRLLRRLLRRLRLRRRLRRRLRRRLRRLRRRLRRLRRLRRLRLRRRLRRLRRRLRRRLRRLRLRRRLRRLLRLRRLRRRLRLRRLRRRLRRLRRLRRRLLRRRR";

        List<Integer> directions = directionsInput
                .chars()
                .mapToObj(c -> c == 'L' ? 0 : 1)
                .toList();

        HashMap<String, String[]> destinationsMap = new HashMap<>();

        for (String line : lines) {
            String group1 = line.substring(0, 3);
            String group2 = line.substring(7, 10);
            String group3 = line.substring(12, 15);

            destinationsMap.put(group1, new String[]{group2, group3});
        }

        List<String> ghosts = new ArrayList<>(destinationsMap
                .keySet()
                .stream()
                .filter(s -> s.endsWith("A"))
                .toList());

        // this is some bs but the **Z appears after every N loops and the N is different for every ghost,
        // so we just need to get the N number for each ghost, and then we find LCM = loop where every ghost hits **Z
        List<Integer> zLoopLengths = new ArrayList<>();

        for (String ghost : ghosts) {
            int steps = 0;

            String position = ghost;

            while (!position.endsWith("Z")) {
                int wrappedIndex = steps % directions.size();

                String[] nextMap = destinationsMap.get(position);

                int nextDirection = directions.get(wrappedIndex);

                position = nextMap[nextDirection];

                steps++;
            }

            zLoopLengths.add(steps);
        }

        long lcm = lcmOfList(zLoopLengths);

        return Long.toString(lcm);
    }

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    // https://stackoverflow.com/questions/4201860/how-to-find-gcd-lcm-on-a-set-of-numbers
    private static long lcmOfList(List<Integer> numbers) {
        long result = numbers.getFirst();
        for (long number : numbers) {
            result = lcm(result, number);
        }
        return result;
    }
}

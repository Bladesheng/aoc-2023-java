import java.time.Duration;
import java.time.Instant;
import java.util.List;

// stolen from this Chad https://github.com/ash42/adventofcode
public abstract class AocSolver {
    protected AocSolver(String filename) {
        List<String> lines = ReadFile.read(filename);

        Instant start = Instant.now();
        String output1 = runPart1(lines);
        Instant end = Instant.now();
        System.out.println("Asnwer to part 1: " + output1);
        System.out.println("Runtime: " + Duration.between(start, end).toMillis() + " ms");

        start = Instant.now();
        String output2 = runPart2(lines);
        end = Instant.now();
        System.out.println("Asnwer to part 2: " + output2);
        System.out.println("Runtime: " + Duration.between(start, end).toMillis() + " ms");
    }

    protected abstract String runPart1(List<String> lines);

    protected abstract String runPart2(List<String> lines);
}

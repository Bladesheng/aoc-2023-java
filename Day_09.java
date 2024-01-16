import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day_09 extends AocSolver {
    protected Day_09(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
//        new Day_09("resources/day_09_sample.txt");
        new Day_09("resources/day_09.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        List<List<Sequence>> report = new ArrayList<>();

        // parse input sequences
        for (String line : lines) {
            String[] inputNumbers = line.split(" ");

            List<Integer> sequenceNumbers = Arrays.stream(inputNumbers)
                    .map(Integer::parseInt)
                    .toList();

            List<Sequence> sequences = new ArrayList<>();
            sequences.add(new Sequence(sequenceNumbers));

            report.add(sequences);
        }

        // create generated sequences
        for (List<Sequence> sequences : report) {
            while (!sequences.getLast().isZero()) {
                Sequence nextSequence = sequences.getLast().createNextSequence();
                sequences.add(nextSequence);
            }
        }

        // create predictions
        for (List<Sequence> sequences : report) {
            // -2 because we want to start at 2nd to last sequence (last sequence prediction is always 0)
            for (int i = sequences.size() - 2; i >= 0; i--) {
                Sequence currentSequence = sequences.get(i);
                Sequence lowerSequence = sequences.get(i + 1);

                int currentLastNumber = currentSequence.getHistory().getLast();
                int lowerPrediction = lowerSequence.getPrediction();

                int currentPrediction = currentLastNumber + lowerPrediction;
                currentSequence.setPrediction(currentPrediction);
            }
        }

        long total = 0;
        for (List<Sequence> sequences : report) {
            total += sequences.getFirst().getPrediction();
        }

        return Long.toString(total);
    }

    @Override
    protected String runPart2(List<String> lines) {
        List<List<Sequence>> report = new ArrayList<>();

        // parse input sequences
        for (String line : lines) {
            String[] inputNumbers = line.split(" ");

            List<Integer> sequenceNumbers = Arrays.stream(inputNumbers)
                    .map(Integer::parseInt)
                    .toList();

            List<Sequence> sequences = new ArrayList<>();
            sequences.add(new Sequence(sequenceNumbers));

            report.add(sequences);
        }

        // create generated sequences
        for (List<Sequence> sequences : report) {
            while (!sequences.getLast().isZero()) {
                Sequence nextSequence = sequences.getLast().createNextSequence();
                sequences.add(nextSequence);
            }
        }

        // create predictions
        for (List<Sequence> sequences : report) {
            // -2 because we want to start at 2nd to last sequence (last sequence prediction is always 0)
            for (int i = sequences.size() - 2; i >= 0; i--) {
                Sequence currentSequence = sequences.get(i);
                Sequence lowerSequence = sequences.get(i + 1);

                // same as part 1, just get first number and subtract lowerPrediction instead of adding it
                int currentFirstNumber = currentSequence.getHistory().getFirst();
                int lowerPrediction = lowerSequence.getPrediction();

                int currentPrediction = currentFirstNumber - lowerPrediction;
                currentSequence.setPrediction(currentPrediction);
            }
        }

        long total = 0;
        for (List<Sequence> sequences : report) {
            total += sequences.getFirst().getPrediction();
        }

        return Long.toString(total);
    }
}

class Sequence {
    private final List<Integer> history;
    private Integer prediction;

    public Sequence(List<Integer> history) {
        this.history = history;

        if (isZero()) {
            this.prediction = 0;
        }
    }

    public List<Integer> getHistory() {
        return history;
    }

    public Integer getPrediction() {
        return prediction;
    }

    public void setPrediction(Integer prediction) {
        this.prediction = prediction;
    }

    public boolean isZero() {
        for (int number : history) {
            if (number != 0) {
                return false;
            }
        }

        return true;
    }

    public Sequence createNextSequence() {
        List<Integer> nextSequence = new ArrayList<>();

        for (int i = 1; i < history.size(); i++) {
            int nextNumber = history.get(i) - history.get(i - 1);
            nextSequence.add(nextNumber);
        }

        return new Sequence(nextSequence);
    }
}

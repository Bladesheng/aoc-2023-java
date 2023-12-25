import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day_04 extends AocSolver {
    protected Day_04(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_04("resources/day_04.txt");
    }


    @Override
    protected String runPart1(List<String> lines) {
        int total = 0;

        for (String line : lines) {
            int matches = getMatches(line);

            if (matches > 0) {
                double points = Math.pow(2, matches - 1);
                total += (int) points;
            }
        }

        return Integer.toString(total);
    }


    @Override
    protected String runPart2(List<String> lines) {
        List<Line> linesWithCount = new ArrayList<>();
        for (String line : lines) {
            linesWithCount.add(new Line(line));
        }

        int total = 0;

        for (int lineNumber = 0; lineNumber < linesWithCount.size(); lineNumber++) {
            Line line = linesWithCount.get(lineNumber);
            int matches = getMatches(line.getLine());

            for (int count = 0; count < line.getCount(); count++) {
                for (int i = 1; i <= matches; i++) {
                    linesWithCount.get(lineNumber + i).incrementCount();
                }

                total++;
            }
        }

        return Integer.toString(total);
    }


    private int getMatches(String line) {
        String[] parts = line.split("\\|");
        String winningPart = parts[0];
        String pickedPart = parts[1];

        Ticket ticket = new Ticket();

        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(winningPart);

        // parse winning numbers
        matcher.find(); // skip card number
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            ticket.addWinningNumber(number);
        }

        // parse picked numbers
        matcher = pattern.matcher(pickedPart);
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            ticket.addPickedNumber(number);
        }

        for (int winningNumber : ticket.getWinningNumbers()) {
            for (int pickedNumber : ticket.getPickedNumbers()) {
                if (pickedNumber == winningNumber) {
                    ticket.incrementWinningNumbersCount();
                    break;
                }
            }
        }

        return ticket.getWinningNumbersCount();
    }
}

class Ticket {
    private final List<Integer> winningNumbers = new ArrayList<>();
    private final List<Integer> pickedNumbers = new ArrayList<>();
    private int winningNumbersCount = 0;


    public List<Integer> getWinningNumbers() {
        return winningNumbers;
    }


    public List<Integer> getPickedNumbers() {
        return pickedNumbers;
    }


    public int getWinningNumbersCount() {
        return winningNumbersCount;
    }


    public void incrementWinningNumbersCount() {
        winningNumbersCount++;
    }


    public void addWinningNumber(int number) {
        winningNumbers.add(number);
    }


    public void addPickedNumber(int number) {
        pickedNumbers.add(number);
    }
}

class Line {
    private final String line;
    private int count = 1; // how many times should the line be processed


    public Line(String startLine) {
        line = startLine;
    }


    public String getLine() {
        return line;
    }


    public int getCount() {
        return count;
    }


    public void incrementCount() {
        count++;
    }
}
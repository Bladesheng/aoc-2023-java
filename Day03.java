import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends AocSolver {
    protected Day03(String filename) {
        super(filename);
    }


    public static void main(String[] args) {
        new Day03("resources/day_03.txt");
    }


    @Override
    protected String runPart1(List<String> lines) {
        int total = 0;

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);

            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                String number = matcher.group(1);
                int startIndex = matcher.start();
                int endIndex = matcher.end();

                boolean isPartNumber = false;

                // check left
                if (startIndex > 0) { // check for out of bounds
                    char charBefore = line.charAt(startIndex - 1);

                    if (isSymbol(charBefore)) {
                        isPartNumber = true;
                    }
                }

                // check right
                if (endIndex < line.length()) { // check for out of bounds
                    char charAfter = line.charAt(endIndex);

                    if (isSymbol(charAfter)) {
                        isPartNumber = true;
                    }
                }

                // check above
                if (lineNumber > 0) { // not on first line
                    // clamp from beginning and end
                    int aboveStart = startIndex - 1;
                    if (aboveStart < 0) {
                        aboveStart = 0;
                    }

                    int aboveEnd = endIndex + 1;
                    if (aboveEnd >= line.length()) {
                        aboveEnd = line.length() - 1;
                    }

                    String substring = lines.get(lineNumber - 1).substring(aboveStart, aboveEnd);

                    if (containsSymbol(substring)) {
                        isPartNumber = true;
                    }
                }


                // check bellow
                if (lineNumber != lines.size() - 1) { // not on last line
                    // clamp from beginning and end
                    int aboveStart = startIndex - 1;
                    if (aboveStart < 0) {
                        aboveStart = 0;
                    }

                    int aboveEnd = endIndex + 1;
                    if (aboveEnd >= line.length()) {
                        aboveEnd = line.length() - 1;
                    }

                    String substring = lines.get(lineNumber + 1).substring(aboveStart, aboveEnd);

                    if (containsSymbol(substring)) {
                        isPartNumber = true;
                    }
                }


                if (isPartNumber) {
                    total += Integer.parseInt(number);
                }
            }
        }

        return Integer.toString(total);
    }


    @Override
    protected String runPart2(List<String> lines) {
        int total = 0;

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);

            Pattern pattern = Pattern.compile("\\*");
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                int gearIndex = matcher.start();

                List<Integer> gearNumbers = new ArrayList<>();


                // check left
                if (gearIndex > 0) { // check for out of bounds
                    int numberBefore = findNumberBefore(gearIndex, line);

                    if (numberBefore > 0) {
                        gearNumbers.add(numberBefore);
                    }
                }

                // check right
                if (gearIndex < line.length()) { // check for out of bounds
                    int numberAfter = findNumberAfter(gearIndex, line);

                    if (numberAfter > 0) {
                        gearNumbers.add(numberAfter);
                    }
                }

                // check above
                if (lineNumber > 0) { // not on first line
                    List<Integer> numbersAbove = findNumbersBeforeAfter(gearIndex, lines.get(lineNumber - 1));

                    if (!numbersAbove.isEmpty()) {
                        gearNumbers.addAll(numbersAbove);
                    }
                }


                // check bellow
                if (lineNumber != lines.size() - 1) { // not on last line
                    List<Integer> numbersBellow = findNumbersBeforeAfter(gearIndex, lines.get(lineNumber + 1));

                    if (!numbersBellow.isEmpty()) {
                        gearNumbers.addAll(numbersBellow);
                    }
                }

                if (gearNumbers.size() == 2) {
                    total += gearNumbers.get(0) * gearNumbers.get(1);
                }
            }
        }

        return Integer.toString(total);
    }


    private boolean isSymbol(char symbol) {
        return symbol != '.';
    }


    private boolean containsSymbol(String str) {
        for (char c : str.toCharArray()) {
            if (isSymbol(c)) {
                return true;
            }
        }

        return false;
    }

    private List<Integer> findNumbersBeforeAfter(int charIndex, String line) {
        List<Integer> numbers = new ArrayList<>();

        if (Character.isDigit(line.charAt(charIndex))) {
            // there is only 1 number above - find it

            int numberAbove = findNumberAtIndex(charIndex, line);

            numbers.add(numberAbove);
        } else {
            // there are 0-2 numbers above - check before and after

            int numberBefore = findNumberBefore(charIndex, line);
            int numberAfter = findNumberAfter(charIndex, line);

            if (numberBefore != 0) {
                numbers.add(numberBefore);
            }

            if (numberAfter != 0) {
                numbers.add(numberAfter);
            }
        }

        return numbers;
    }


    private int findNumberAtIndex(int charIndex, String line) {
        // we find the beginning of the number
        while (Character.isDigit(line.charAt(charIndex - 1))) {
            charIndex--;
        }

        String numberStr = "";

        while (Character.isDigit(line.charAt(charIndex))) {
            numberStr += line.charAt(charIndex);

            charIndex++;
        }

        return Integer.parseInt(numberStr);
    }


    private int findNumberBefore(int charIndex, String line) {
        String substring = line.substring(0, charIndex);

        Pattern pattern = Pattern.compile(".*?(\\d+)$");
        Matcher matcher = pattern.matcher(substring);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }


    private int findNumberAfter(int charIndex, String line) {
        String substring = line.substring(charIndex + 1);

        Pattern patternLeft = Pattern.compile("^(\\d+).*");
        Matcher matcherLeft = patternLeft.matcher(substring);

        if (matcherLeft.find()) {
            return Integer.parseInt(matcherLeft.group(1));
        }

        return 0;
    }
}

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day01 {
    public static void main(String[] args) {
        List<String> lines = ReadFile.read("resources/day_01.txt");

        Map<String, Integer> NUMBERS = new HashMap<>();
        NUMBERS.put("one", 1);
        NUMBERS.put("two", 2);
        NUMBERS.put("three", 3);
        NUMBERS.put("four", 4);
        NUMBERS.put("five", 5);
        NUMBERS.put("six", 6);
        NUMBERS.put("seven", 7);
        NUMBERS.put("eight", 8);
        NUMBERS.put("nine", 9);

        int total = 0;

        for (String line : lines) {
            int firstNumber = 0;
            int secondNumber = 0;

            // left to right
            for (int i = 0; i < line.length(); i++) {
                String substr = line.substring(0, i);

                for (Map.Entry<String, Integer> day : NUMBERS.entrySet()) {
                    if (substr.endsWith(day.getKey())) {
                        firstNumber = day.getValue();
                        break;
                    }
                }

                if (firstNumber != 0) {
                    break;
                }

                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    firstNumber = Character.getNumericValue(c);
                    break;
                }
            }


            // right to left
            for (int i = line.length() - 1; i >= 0; i--) {
                String substr = line.substring(i);

                for (Map.Entry<String, Integer> day : NUMBERS.entrySet()) {
                    if (substr.startsWith(day.getKey())) {
                        secondNumber = day.getValue();
                        break;
                    }
                }

                if (secondNumber != 0) {
                    break;
                }

                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    secondNumber = Character.getNumericValue(c);
                    break;
                }
            }

            int result = Integer.parseInt(Integer.toString(firstNumber) + Integer.toString(secondNumber));
            total += result;
        }

        System.out.format("Total is: %d", total);
    }
}

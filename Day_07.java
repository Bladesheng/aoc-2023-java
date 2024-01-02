import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day_07 extends AocSolver {
    protected Day_07(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_07("resources/day_07.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        List<Hand> hands = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = Pattern.compile("^(\\w+) (\\d+)$").matcher(line);

            if (matcher.find()) {
                String cards = matcher.group(1);
                String bid = matcher.group(2);

                hands.add(new Hand(cards, Integer.parseInt(bid)));
            }
        }


        hands.sort((hand1, hand2) -> {
            int hand1Score;
            int hand2Score;

            hand1Score = hand1.getCombination();
            hand2Score = hand2.getCombination();

            if (hand1Score == hand2Score) {
                if (hand1.equals(hand2)) {
                    hand1Score = 0;
                    hand2Score = 0;
                } else if (Hand.compareCardsStrength(hand1, hand2)) {
                    hand1Score = 1;
                    hand2Score = 0;
                } else {
                    hand1Score = 0;
                    hand2Score = 1;
                }
            }

            return Integer.compare(hand1Score, hand2Score);
        });

        long result = hands.stream().reduce(0L, (acc, hand) -> acc + hand.getBid() * (hands.indexOf(hand) + 1), Long::sum);

        return Long.toString(result);
    }

    @Override
    protected String runPart2(List<String> lines) {
        return "hi mom";
    }


}

class Hand {
    private final List<Character> cards = new ArrayList<>();
    final private int bid;

    final private int combination;


    public Hand(String cardsStr, int bid) {
        this.bid = bid;

        for (char ch : cardsStr.toCharArray()) {
            cards.add(ch);
        }

        combination = calculateCombination();
    }

    public List<Character> getCards() {
        return cards;
    }

    public int getBid() {
        return bid;
    }

    public int getCombination() {
        return combination;
    }

    private int calculateCombination() {
        // 5 of kind
        for (char card : cards) {
            if (containsNCards(5, card)) {
                return 7;
            }
        }

        // 4 of kind
        for (char card : cards) {
            if (containsNCards(4, card)) {
                return 6;
            }
        }

        for (char card : cards) {
            if (containsNCards(3, card)) {
                // remove the found card
                List<Character> filteredCards = cards.stream()
                        .filter(c -> c != card)
                        .toList();

                if (filteredCards.get(0) == filteredCards.get(1)) {
                    // full house (3+2)
                    return 5;
                } else {
                    // 3 of kind
                    return 4;
                }
            }
        }

        for (char card : cards) {
            if (containsNCards(2, card)) {
                // remove the found card
                List<Character> filteredCards = cards.stream()
                        .filter(c -> c != card)
                        .toList();

                if (filteredCards.get(0) == filteredCards.get(1) || filteredCards.get(0) == filteredCards.get(2) || filteredCards.get(1) == filteredCards.get(2)) {
                    // 2 pair (2+2)
                    return 3;
                } else {
                    // 1 pair
                    return 2;
                }
            }
        }

        return 1;
    }

    /**
     * @return boolean if card is present n times
     */
    private boolean containsNCards(int n, char cardToCheck) {
        long count = cards.stream().filter(card -> card == cardToCheck).count();

        return n == count;
    }

    public static boolean compareCardsStrength(Hand hand1, Hand hand2) {
        List<Character> cards1 = hand1.getCards();
        List<Character> cards2 = hand2.getCards();

        HashMap<Character, Integer> CARD_VALUES = new HashMap<>();
        CARD_VALUES.put('A', 14);
        CARD_VALUES.put('K', 13);
        CARD_VALUES.put('Q', 12);
        CARD_VALUES.put('J', 11);
        CARD_VALUES.put('T', 10);
        CARD_VALUES.put('9', 9);
        CARD_VALUES.put('8', 8);
        CARD_VALUES.put('7', 7);
        CARD_VALUES.put('6', 6);
        CARD_VALUES.put('5', 5);
        CARD_VALUES.put('4', 4);
        CARD_VALUES.put('3', 3);
        CARD_VALUES.put('2', 2);
        CARD_VALUES.put('1', 1);

        for (int i = 0; i < 5; i++) {
            int card1Val = CARD_VALUES.get(cards1.get(i));
            int card2Val = CARD_VALUES.get(cards2.get(i));

            if (card1Val == card2Val) {
                continue;
            }

            return card1Val > card2Val;
        }

        System.out.println("FUCK");
        System.exit(1);
        return false;
    }
}


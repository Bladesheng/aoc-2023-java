import java.util.*;
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
        List<Hand> hands = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = Pattern.compile("^(\\w+) (\\d+)$").matcher(line);

            if (matcher.find()) {
                String cards = matcher.group(1);
                String bid = matcher.group(2);

                // create hand with original cards
                Hand originalHand = new Hand(cards, Integer.parseInt(bid));

                // if hand has at least joker
                if (cards.contains(String.valueOf('J'))) {
                    // create set of all unique cards - cards that are not already in set won't increase the combination value
                    Set<Character> uniqueCards = new HashSet<>(originalHand.getCards());

                    // iterate over the unique cards and try them all in place of the joker(s).
                    // (hand will always have the highest value when all jokers are the same card, so no need to try all the possible combinations)
                    List<Integer> possibleCombinations = new ArrayList<>();
                    for (char replacementCard : uniqueCards) {
                        String potentialCards = cards.chars()
                                .mapToObj(c -> (char) c == 'J' ? replacementCard : (char) c)
                                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                                .toString();

                        // add the resulting combination value to list
                        Hand potentialHand = new Hand(potentialCards, Integer.parseInt(bid));
                        possibleCombinations.add(potentialHand.getCombination());
                    }

                    // find the highest value in the list
                    int highestCombination = possibleCombinations.stream()
                            .max(Integer::compareTo)
                            .orElseThrow();

                    // set combination value of the original hand to the highest value - it will always be bigger or same as before
                    originalHand.setCombination(highestCombination);
                }

                hands.add(originalHand);
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


}

class Hand {
    private final List<Character> cards = new ArrayList<>();
    final private int bid;

    private int combination;


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

    public void setCombination(int combination) {
        this.combination = combination;
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

        // part 1
//        Character[] CARDS = {
//                '2',
//                '3',
//                '4',
//                '5',
//                '6',
//                '7',
//                '8',
//                '9',
//                'T',
//                'J',
//                'Q',
//                'K',
//                'A',
//        };
        // part 2
        Character[] CARDS = {
                'J',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'T',
                'Q',
                'K',
                'A',
        };
        HashMap<Character, Integer> CARD_VALUES = new HashMap<>();
        for (int i = 0; i < CARDS.length; i++) {
            CARD_VALUES.put(CARDS[i], i + 1);
        }

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


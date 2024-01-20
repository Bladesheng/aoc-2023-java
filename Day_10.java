import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Day_10 extends AocSolver {
    protected Day_10(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_10("resources/day_10.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        final HashMap<Point, Character> pipesMap = new HashMap<>();

        Point start = new Point();

        for (int y = 0; y < lines.size(); y++) {
            char[] characters = lines.get(y).toCharArray();

            for (int x = 0; x < characters.length; x++) {
                char character = characters[x];
                Point point = new Point(x, -y);
                pipesMap.put(point, character);

                if (character == 'S') {
                    start.setLocation(point);
                }
            }

        }

        int moves = 1;
//        PositionTracker tracker1 = new PositionTracker(1, -1, start, pipesMap);
//        PositionTracker tracker2 = new PositionTracker(3, -1, start, pipesMap);
        PositionTracker tracker1 = new PositionTracker(42, -92, start, pipesMap);
        PositionTracker tracker2 = new PositionTracker(44, -92, start, pipesMap);

        while (!tracker1.getCurrentPosition().equals(tracker2.getCurrentPosition())) {
            moves++;

            tracker1.moveToNext();
            tracker2.moveToNext();
        }


        return Integer.toString(moves);
    }

    @Override
    protected String runPart2(List<String> lines) {
        // expand everything to 3x3
        // flood fill
        // count only the original tiles that are flooded
        // (how to expand S?)

        return "hi mom";
    }
}


class PositionTracker {
    private final Point currentPosition;
    private final Point previousPosition;
    final private HashMap<Point, Character> pipesMap;

    public PositionTracker(int x, int y, Point previousPosition, HashMap<Point, Character> pipesMap) {
        currentPosition = new Point(x, y);
        this.previousPosition = previousPosition.getLocation();
        this.pipesMap = pipesMap;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public Point moveToNext() {
        char currentPipe = pipesMap.get(currentPosition);


        int startOffsetX = previousPosition.x - currentPosition.x;
        int startOffsetY = previousPosition.y - currentPosition.y;
        Point startOffset = new Point(startOffsetX, startOffsetY);

        final HashMap<Character, Pair> pipeConversions = new HashMap<>();
        pipeConversions.put('|', new Pair(new Point(0, -1), new Point(0, 1)));
        pipeConversions.put('-', new Pair(new Point(-1, 0), new Point(1, 0)));
        pipeConversions.put('L', new Pair(new Point(0, 1), new Point(1, 0)));
        pipeConversions.put('J', new Pair(new Point(-1, 0), new Point(0, 1)));
        pipeConversions.put('7', new Pair(new Point(-1, 0), new Point(0, -1)));
        pipeConversions.put('F', new Pair(new Point(0, -1), new Point(1, 0)));

        Pair pipeConversion = pipeConversions.get(currentPipe);

        Point endOffset = pipeConversion.getOther(startOffset);

        previousPosition.setLocation(currentPosition);
        currentPosition.setLocation(currentPosition.x + endOffset.x, currentPosition.y + endOffset.y);

        return currentPosition;
    }

}

class Pair {
    final private Point val1;
    final private Point val2;

    public Pair(Point val1, Point val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public Point getOther(Point val) {
        return val.equals(val1) ? val2 : val1;
    }
}

import java.util.ArrayList;
import java.util.List;

public class Day_06 extends AocSolver {
    protected Day_06(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_06("resources/day_06.txt");
    }

    @Override
    protected String runPart1(List<String> lines) {
        // fuck regex
        // all my homies hate regex
        List<Race> raceRecords = new ArrayList<>();

        // sample
//        raceRecords.add(new Race(7, 9));
//        raceRecords.add(new Race(15, 40));
//        raceRecords.add(new Race(30, 200));

        // real
        raceRecords.add(new Race(44, 208));
        raceRecords.add(new Race(80, 1581));
        raceRecords.add(new Race(65, 1050));
        raceRecords.add(new Race(72, 1102));


        long total = 1;

        for (Race race : raceRecords) {
            List<RaceResult> results = new ArrayList<>();

            for (long timeHeld = 1; timeHeld < race.getTime(); timeHeld++) {
                long distanceTravelled = (race.getTime() - timeHeld) * timeHeld;
                boolean beatRecord = distanceTravelled > race.getDistance();

                results.add(new RaceResult(timeHeld, distanceTravelled, beatRecord));
            }

            long winningCount = results.stream().filter(RaceResult::isBeatRecord).count();
            total *= winningCount;
        }

        return Long.toString(total);
    }

    @Override
    protected String runPart2(List<String> lines) {
        // sample
//        Race race = new Race(71530, 940200);
        // real
        Race race = new Race(44806572L, 208158110501102L);

        List<RaceResult> results = new ArrayList<>();

        for (long timeHeld = 1; timeHeld < race.getTime(); timeHeld++) {
            long distanceTravelled = (race.getTime() - timeHeld) * timeHeld;
            boolean beatRecord = distanceTravelled > race.getDistance();

            results.add(new RaceResult(timeHeld, distanceTravelled, beatRecord));
        }

        long total = results.stream().filter(RaceResult::isBeatRecord).count();

        return Long.toString(total);
    }
}

class Race {
    final private long time;
    final private long distance;

    public Race(long time, long distance) {
        this.time = time;
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public long getDistance() {
        return distance;
    }
}

class RaceResult extends Race {
    final private boolean beatRecord;

    public RaceResult(long time, long distance, boolean beatRecord) {
        super(time, distance);
        this.beatRecord = beatRecord;
    }

    public boolean isBeatRecord() {
        return beatRecord;
    }
}

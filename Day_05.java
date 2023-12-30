import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day_05 extends AocSolver {
    protected Day_05(String filename) {
        super(filename);
    }

    public static void main(String[] args) {
        new Day_05("resources/day_05.txt");
    }


    @Override
    protected String runPart1(List<String> lines) {
        List<Seed> seeds = new ArrayList<>();
        List<Map> maps = new ArrayList<>();

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);

            // first line - parse seeds
            if (lineNumber == 0) {
                Matcher matcher = Pattern.compile("\\d+").matcher(line);

                while (matcher.find()) {
                    String matchedNumber = matcher.group();


                    seeds.add(new Seed(Long.parseLong(matchedNumber)));
                }

                continue;
            }

            // empty line
            if (line.isEmpty()) {
                continue;
            }

            // start of new map
            if (Character.isLetter(line.charAt(0))) {
                maps.add(new Map());
                continue;
            }

            // range inside a map
            if (Character.isDigit(line.charAt(0))) {
                Matcher matcher = Pattern.compile("\\d+").matcher(line);
                Long[] numbers = new Long[3];

                for (int i = 0; i < 3 && matcher.find(); i++) {
                    numbers[i] = Long.parseLong(matcher.group());
                }

                Range range = new Range(numbers[0], numbers[1], numbers[2]);

                maps.getLast().addRange(range);
                continue;
            }
        }


        for (Seed seed : seeds) {
            for (Map map : maps) {
                long newSeed = map.convert(seed.getSeedNumber());
                seed.setSeedNumber(newSeed);
            }

        }

        long minValue = seeds.stream().mapToLong(Seed::getSeedNumber).min().orElseThrow();

        return Long.toString(minValue);
    }


    // storing all the seeds would take around 20 GB of memory, so we will process them one by one
    @Override
    protected String runPart2(List<String> lines) {
        List<SeedRange> seedRanges = new ArrayList<>();
        List<Map> maps = new ArrayList<>();

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);

            // first line - parse seeds
            if (lineNumber == 0) {
                Matcher matcher = Pattern.compile("(\\d+) (\\d+)").matcher(line);

                while (matcher.find()) {
                    long firstNumber = Long.parseLong(matcher.group(1));
                    long secondNumber = Long.parseLong(matcher.group(2));

                    seedRanges.add(new SeedRange(firstNumber, secondNumber));
                }

                continue;
            }

            // empty line
            if (line.isEmpty()) {
                continue;
            }

            // start of new map
            if (Character.isLetter(line.charAt(0))) {
                maps.add(new Map());
                continue;
            }

            // range inside a map
            if (Character.isDigit(line.charAt(0))) {
                Matcher matcher = Pattern.compile("\\d+").matcher(line);
                Long[] numbers = new Long[3];

                for (int i = 0; i < 3 && matcher.find(); i++) {
                    numbers[i] = Long.parseLong(matcher.group());
                }

                Range range = new Range(numbers[0], numbers[1], numbers[2]);

                maps.getLast().addRange(range);
                continue;
            }
        }

        long minSeed = Long.MAX_VALUE;

        long seedsDone = 0;
        long totalSeeds = seedRanges.stream().mapToLong(SeedRange::getLength).sum();
        System.out.println("Total number of seeds: " + totalSeeds);

        for (SeedRange seedRange : seedRanges) {
            for (long seedNumber = seedRange.getStart(); seedNumber < seedRange.getStart() + seedRange.getLength(); seedNumber++) {
                Seed seed = new Seed(seedNumber);

                for (Map map : maps) {
                    long newSeed = map.convert(seed.getSeedNumber());
                    seed.setSeedNumber(newSeed);
                }

                if (seed.getSeedNumber() < minSeed) {
                    minSeed = seed.getSeedNumber();
                }

                seedsDone++;
                if (seedsDone % 10000000 == 0) {
                    float progress = (float) seedsDone / totalSeeds;
                    System.out.println(progress * 100 + " % (" + seedsDone + " seeds done)");
                }
            }

            System.out.println("range done");
        }

        return Long.toString(minSeed);
    }
}

class Seed {
    private long seedNumber;


    public Seed(long seed) {
        this.seedNumber = seed;
    }


    public long getSeedNumber() {
        return seedNumber;
    }


    public void setSeedNumber(long seedNumber) {
        this.seedNumber = seedNumber;
    }
}

class SeedRange {
    long start;
    long length;

    public SeedRange(long start, long length) {
        this.start = start;
        this.length = length;
    }

    public long getStart() {
        return start;
    }

    public long getLength() {
        return length;
    }
}

class Map {
    final List<Range> ranges = new ArrayList<>();


    public long convert(long initialNumber) {
        for (Range range : ranges) {
            if (range.checkIfInRange(initialNumber)) {
                return range.convert(initialNumber);
            }
        }

        return initialNumber;
    }

    public void addRange(Range range) {
        ranges.add(range);
    }
}

class Range {
    long destination;
    long source;
    long length;


    public Range(long destination, long source, long length) {
        this.destination = destination;
        this.source = source;
        this.length = length;
    }


    public boolean checkIfInRange(long number) {
        if (number >= source && number < source + length) {
            return true;
        }

        return false;
    }


    public long convert(long number) {
        long offset = destination - source;
        return number + offset;
    }
}
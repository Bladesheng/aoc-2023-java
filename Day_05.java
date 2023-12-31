import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
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

        final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<Long>> futures = new ArrayList<>();

        long minSeed = Long.MAX_VALUE;

        long totalSeeds = seedRanges.stream().mapToLong(SeedRange::getLength).sum();
        System.out.printf("Total number of seeds: %,d%n", totalSeeds);
        AtomicInteger rangesDone = new AtomicInteger(0);

        // run all the seed ranges in parallel - speedup from 6 minutes to 90 seconds
        for (SeedRange seedRange : seedRanges) {
            Callable<Long> seedRangeTask = () -> {
                long localMinSeed = Long.MAX_VALUE;

                for (long seedNumber = seedRange.getStart(); seedNumber < seedRange.getStart() + seedRange.getLength(); seedNumber++) {
                    Seed seed = new Seed(seedNumber);

                    for (Map map : maps) {
                        long newSeed = map.convert(seed.getSeedNumber());
                        seed.setSeedNumber(newSeed);
                    }

                    if (seed.getSeedNumber() < localMinSeed) {
                        localMinSeed = seed.getSeedNumber();
                    }
                }

                System.out.println("range done (" + rangesDone.incrementAndGet() + "/" + seedRanges.size() + ")");
                return localMinSeed;
            };

            futures.add(executorService.submit(seedRangeTask));
        }

        executorService.shutdown();

        try {
            for (Future<Long> future : futures) {
                long rangeMinSeed = future.get();
                if (rangeMinSeed < minSeed) {
                    minSeed = rangeMinSeed;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
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
    final long destination;
    final long source;
    final long length;
    final long offset;


    public Range(long destination, long source, long length) {
        this.destination = destination;
        this.source = source;
        this.length = length;
        this.offset = destination - source;
    }


    public boolean checkIfInRange(long number) {
        return number >= source && number < source + length;
    }


    public long convert(long number) {
        return number + offset;
    }
}
package net.datafaker.service;

import net.datafaker.AbstractFakerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static net.datafaker.matchers.MatchesRegularExpression.matchesRegularExpression;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.CombinableMatcher.both;

/**
 * @author pmiklos
 */
@RunWith(Parameterized.class)
public class RandomServiceTest extends AbstractFakerTest {

    private final RandomService randomService;

    public RandomServiceTest(String ignoredTitle, RandomService service) {
        this.randomService = service;
    }

    @Parameterized.Parameters(name = "Created via {0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"RandomService(Random)", new RandomService(new Random())},
                {"RandomService()", new RandomService()}
        };
        return Arrays.asList(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPositiveBoundariesOnly() {
        randomService.nextLong(0L);
    }

    @Test
    public void testLongWithinBoundary() {
        assertThat(randomService.nextLong(1), is(0L));

        for (int i = 1; i < 10; i++) {
            assertThat(randomService.nextLong(2), lessThan(2L));
        }
    }

    @Test
    public void testLongMaxBoundary() {
        assertThat(randomService.nextLong(Long.MAX_VALUE), greaterThan(0L));
        assertThat(randomService.nextLong(Long.MAX_VALUE), lessThan(Long.MAX_VALUE));
    }

    @Test
    public void testIntInRange() {
        for (int i = 1; i < 100; i++) {
            assertThat(randomService.nextInt(-5, 5), both(lessThanOrEqualTo(5)).and(greaterThanOrEqualTo(-5)));
        }
    }

    @Test
    public void testDoubleInRange() {
        for (int i = 1; i < 100; i++) {
            assertThat(randomService.nextDouble(-5, 5), both(lessThanOrEqualTo(5.0)).and(greaterThanOrEqualTo(-5.0)));
        }
    }

    @Test
    public void testLongInRange() {
        for (int i = 1; i < 1_000; i++) {
            assertThat(randomService.nextLong(-5_000_000_000L, 5_000_000_000L), both(lessThanOrEqualTo(5_000_000_000L)).and(greaterThanOrEqualTo(-5_000_000_000L)));
        }
    }

    @Test
    public void testHex() {
        assertThat(randomService.hex(8), matchesRegularExpression("^[0-9A-F]{8}$"));
    }

    @Test
    public void testDefaultHex() {
        assertThat(randomService.hex(), matchesRegularExpression("^[0-9A-F]{8}$"));
    }
}

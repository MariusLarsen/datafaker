package net.datafaker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author CharlotteE67
 */
public class PassportTest extends AbstractFakerTest {

    @Test
    public void testChValid() {
        String passport = faker.passport().chValid();
        assertTrue(passport.charAt(0) == 'E' || passport.charAt(0) == 'G');
        if (passport.charAt(0) == 'G') {
            for (int i = 1; i < passport.length(); i++) {
                assertTrue(Character.isDigit(passport.charAt(i)));
            }
        } else {
            assertFalse(passport.charAt(1) == 'I' || passport.charAt(1) == 'O');
            assertTrue(Character.isLetter(passport.charAt(1)) || Character.isDigit(passport.charAt(1)));
            for (int i = 2; i < passport.length(); i++) {
                assertTrue(Character.isDigit(passport.charAt(i)));
            }
        }
    }

    @Test
    public void testChValidLength() {
        assertEquals(faker.passport().chValid().length(), 9);
    }

    @Test
    public void testChInValid() {
        assertFalse(faker.passport().chInvalid().matches("E[0-9A-HJ-NP-Z][0-9]{7}")
                && faker.passport().chInvalid().matches("G[0-9]{8}"));
    }

    @Test
    public void testChInValidNotNull() {
        assertNotNull(faker.passport().chInvalid());
    }

    @Test
    public void testAmValid() {
        assertTrue(faker.passport().amValid().matches("[0-9]{8}"));
    }

    @Test
    public void testAmValidLength() {
        assertEquals(faker.passport().amValid().length(), 8);
    }

    @Test
    public void testAmInValid() {
        assertFalse(faker.passport().amInvalid().matches("[0-9]{8}"));
    }

    @Test
    public void testAmInValidNotNull() {
        assertNotNull(faker.passport().amInvalid());
    }

    @Test
    public void testChValidFrequently() {
        for (int i = 0; i < 100; i++) {
            testChValid();
        }
    }

    @Test
    public void testChInValidFrequently() {
        for (int i = 0; i < 100; i++) {
            testChInValid();
        }
    }
}

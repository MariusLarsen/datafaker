package net.datafaker;

import org.junit.Test;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

import static net.datafaker.matchers.IsANumber.isANumber;
import static net.datafaker.matchers.IsStringWithContents.isStringWithContents;
import static net.datafaker.matchers.MatchesRegularExpression.matchesRegularExpression;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;

public class AddressTest extends AbstractFakerTest {

    private static final char decimalSeparator = new DecimalFormatSymbols().getDecimalSeparator();

    @Test
    public void testStreetAddressStartsWithNumber() {
        final String streetAddressNumber = faker.address().streetAddress();
        assertThat(streetAddressNumber, matchesRegularExpression("[0-9]+ .+"));
    }

    @Test
    public void testStreetAddressIsANumber() {
        final String streetAddressNumber = faker.address().streetAddressNumber();
        assertThat(streetAddressNumber, matchesRegularExpression("[0-9]+"));
    }

    @Test
    public void testLatitude() {
        String latStr;
        Double lat;
        for (int i = 0; i < 100; i++) {
            latStr = faker.address().latitude().replace(decimalSeparator, '.');
            assertThat(latStr, isANumber());
            lat = Double.valueOf(latStr);
            assertThat("Latitude is less then -90", lat, greaterThanOrEqualTo(-90.0));
            assertThat("Latitude is greater than 90", lat, lessThanOrEqualTo(90.0));
        }
    }

    @Test
    public void testLongitude() {
        String longStr;
        Double lon;
        for (int i = 0; i < 100; i++) {
            longStr = faker.address().longitude().replace(decimalSeparator, '.');
            assertThat(longStr, isANumber());
            lon = Double.valueOf(longStr);
            assertThat("Longitude is less then -180", lon, greaterThanOrEqualTo(-180.0));
            assertThat("Longitude is greater than 180", lon, lessThanOrEqualTo(180.0));
        }
    }

    @Test
    public void testLocaleLatitude() {
        Faker engFaker = new Faker(Locale.ENGLISH);
        String engLatStr = engFaker.address().latitude();
        assertThat(engLatStr, matchesRegularExpression("-?\\d{1,3}\\.\\d+"));

        Faker ruFaker = new Faker(new Locale("ru"));
        String rusLatStr = ruFaker.address().latitude();
        assertThat(rusLatStr, matchesRegularExpression("-?\\d{1,3},\\d+"));
    }

    @Test
    public void testLocaleLongitude() {
        Faker engFaker = new Faker(Locale.ENGLISH);
        String engLatStr = engFaker.address().longitude();
        assertThat(engLatStr, matchesRegularExpression("-?\\d{1,3}\\.\\d+"));

        Faker ruFaker = new Faker(new Locale("ru"));
        String rusLatStr = ruFaker.address().longitude();
        assertThat(rusLatStr, matchesRegularExpression("-?\\d{1,3},\\d+"));
    }

    @Test
    public void testTimeZone() {
        assertThat(faker.address().timeZone(), matchesRegularExpression("[A-Za-z_]+/[A-Za-z_]+[/A-Za-z_]*"));
    }

    @Test
    public void testState() {
        assertThat(faker.address().state(), matchesRegularExpression("[A-Za-z ]+"));
    }

    @Test
    public void testCity() {
        assertThat(faker.address().city(), matchesRegularExpression("[A-Za-z'() ]+"));
    }

    @Test
    public void testCityName() {
        assertThat(faker.address().cityName(), matchesRegularExpression("[A-Za-z'() ]+"));
    }

    @Test
    public void testCountry() {
        assertThat(faker.address().country(), matchesRegularExpression("[A-Za-z\\- &.,'()\\d]+"));
    }

    @Test
    public void testCountryCode() {
        assertThat(faker.address().countryCode(), matchesRegularExpression("[A-Za-z ]+"));
    }

    @Test
    public void testStreetAddressIncludeSecondary() {
        assertThat(faker.address().streetAddress(true), not(is(emptyString())));
    }

    @Test
    public void testCityWithLocaleFranceAndSeed() {
        long seed = 1L;
        Faker firstFaker = new Faker(Locale.FRANCE, new Random(seed));
        Faker secondFaker = new Faker(Locale.FRANCE, new Random(seed));
        assertThat(firstFaker.address().city(), is(secondFaker.address().city()));
    }

    @Test
    public void testFullAddress() {
        assertThat(faker.address().fullAddress(), not(is(emptyOrNullString())));
    }

    @Test
    public void testZipCodeByState() {
        faker = new Faker(new Locale("en-US"));
        assertThat(faker.address().zipCodeByState(faker.address().stateAbbr()), matchesRegularExpression("[0-9]{5}"));
    }

    @Test
    public void testHungarianZipCodeByState() {
        faker = new Faker(new Locale("hu"));
        assertThat(faker.address().zipCodeByState(faker.address().stateAbbr()), matchesRegularExpression("[0-9]{4}"));
    }

    @Test
    public void testCountyByZipCode() {
        faker = new Faker(new Locale("en-US"));
        assertThat(faker.address().countyByZipCode("47732"), not(is(emptyOrNullString())));
    }

    @Test
    public void testStreetPrefix() {
        assertThat(faker.address().streetPrefix(), isStringWithContents());
    }

    @Test
    public void testStreetSuffix() {
        assertThat(faker.address().streetSuffix(), isStringWithContents());
    }

    @Test
    public void testCityPrefix() {
        assertThat(faker.address().cityPrefix(), isStringWithContents());
    }
    @Test
    public void testCitySuffix() {
        assertThat(faker.address().citySuffix(), isStringWithContents());
    }

    @Test
    public void testZipIsFiveChars() {
        faker = new Faker(new Locale("en-us"));
        assertThat(faker.address().zipCode().length(), is(5));
    }

    @Test
    public void testZipPlus4IsTenChars() {
        faker = new Faker(new Locale("en-us"));
        assertThat(faker.address().zipCodePlus4().length(), is(10));  // includes dash
    }

    @Test
    public void testZipPlus4IsNineDigits() {
        faker = new Faker(new Locale("en-us"));
        final String[] zipCodeParts = faker.address().zipCodePlus4().split("-");
        assertThat(zipCodeParts[0], matchesRegularExpression("[0-9]{5}"));
        assertThat(zipCodeParts[1], matchesRegularExpression("[0-9]{4}"));
    }

}

package net.datafaker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.datafaker.repeating.Repeat;
import org.apache.commons.validator.routines.EmailValidator;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static net.datafaker.matchers.CountOfCharactersMatcher.countOf;
import static net.datafaker.matchers.MatchesRegularExpression.matchesRegularExpression;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertTrue;

public class InternetTest extends AbstractFakerTest {

    @Test
    public void testEmailAddress() {
        String emailAddress = faker.internet().emailAddress();
        assertThat(EmailValidator.getInstance().isValid(emailAddress), is(true));
    }

    @Test
    public void testEmailAddressWithLocalPartParameter() {
        String emailAddress = faker.internet().emailAddress("john");
        assertThat(emailAddress, startsWith("john@"));
        assertThat(EmailValidator.getInstance().isValid(emailAddress), is(true));
    }

    @Test
    public void testSafeEmailAddress() {
        List<String> emails = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            String emailAddress = faker.internet().safeEmailAddress();
            assertThat(EmailValidator.getInstance().isValid(emailAddress), is(true));
            emails.add(emailAddress);
        }
        final String safeDomain = faker.resolve("internet.safe_email");

        assertThat("Should find at least one email from " + safeDomain, emails,
                Matchers.hasItem(Matchers.endsWith("@" + safeDomain)));
    }

    @Test
    public void testSafeEmailAddressWithLocalPartParameter() {
        List<String> emails = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            String emailAddress = faker.internet().safeEmailAddress("john");
            assertThat(emailAddress, startsWith("john@"));
            assertThat(EmailValidator.getInstance().isValid(emailAddress), is(true));
            emails.add(emailAddress);
        }
        final String safeDomain = faker.resolve("internet.safe_email");

        assertThat("Should find at least one email from " + safeDomain, emails,
                Matchers.hasItem(Matchers.endsWith("@" + safeDomain)));
    }

    @Test
    public void testEmailAddressDoesNotIncludeAccentsInTheLocalPart() {
        String emailAddress = faker.internet().emailAddress("áéíóú");
        assertThat(emailAddress, startsWith("aeiou@"));
    }

    @Test
    public void testSafeEmailAddressDoesNotIncludeAccentsInTheLocalPart() {
        String emailAddress = faker.internet().safeEmailAddress("áéíóú");
        assertThat(emailAddress, startsWith("aeiou@"));
    }

    @Test
    public void testUrl() {
        assertThat(faker.internet().url(), matchesRegularExpression("www\\.(\\w|-)+\\.\\w+"));
    }

    @Test
    public void testAvatar() {
        assertThat(faker.internet().avatar(), matchesRegularExpression("http.*/[^/]+/128.jpg$"));
    }

    @Test
    public void testImage() {
        String imageUrl = faker.internet().image();
        assertThat(imageUrl, matchesRegularExpression("^https:\\/\\/lorempixel\\.com(/g)?/\\d{1,4}/\\d{1,4}/\\w+/$"));
    }

    @Test
    public void testDomainName() {
        assertThat(faker.internet().domainName(), matchesRegularExpression("[a-z]+\\.\\w{2,4}"));
    }

    @Test
    public void testDomainWord() {
        assertThat(faker.internet().domainWord(), matchesRegularExpression("[a-z]+"));
    }

    @Test
    public void testDomainSuffix() {
        assertThat(faker.internet().domainSuffix(), matchesRegularExpression("\\w{2,4}"));
    }

    @Test
    public void testImageWithExplicitParams() {
        String imageUrl = faker.internet().image(800, 600, false, "bugs");
        assertThat(imageUrl, matchesRegularExpression("^https:\\/\\/lorempixel\\.com/800/600/\\w+/bugs$"));
    }

    @Test
    public void testPassword() {
        assertThat(faker.internet().password(), matchesRegularExpression("[a-z\\d]{8,16}"));
    }

    @Test
    public void testPasswordWithFixedLength() {
        String password = Faker.instance().internet().password(32, 32, true, true, true);
        assertThat(password.length(), is(32));
    }

    @Test
    public void testPasswordIncludeDigit() {
        assertThat(faker.internet().password(), matchesRegularExpression("[a-z\\d]{8,16}"));
        assertThat(faker.internet().password(false), matchesRegularExpression("[a-z]{8,16}"));
    }

    @Test
    public void testPasswordMinLengthMaxLength() {
        assertThat(faker.internet().password(10, 25), matchesRegularExpression("[a-z\\d]{10,25}"));
    }

    @Test
    public void testPasswordMinLengthMaxLengthIncludeUpperCase() {
        assertThat(faker.internet().password(1, 2, false), matchesRegularExpression("[a-z\\d]{1,2}"));
        assertThat(faker.internet().password(10, 25, true), matchesRegularExpression("[a-zA-Z\\d]{10,25}"));
    }

    @Test
    public void testPasswordMinLengthMaxLengthIncludeUpperCaseIncludeSpecial() {
        assertThat(faker.internet().password(10, 25, false, false), matchesRegularExpression("[a-z\\d]{10,25}"));
        assertThat(faker.internet().password(10, 25, false, true), matchesRegularExpression("[a-z\\d!@#$%^&*]{10,25}"));
        assertThat(faker.internet().password(10, 25, true, true), matchesRegularExpression("[a-zA-Z\\d!@#$%^&*]{10,25}"));
    }

    @Test
    public void shouldGenerateAPasswordWithMinAndMaxLength() {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            results.add(faker.internet().password(1, 10));
        }

        final List<String> min = results.stream().filter(x -> x.length() == 1).collect(Collectors.toList());
        final List<String> max = results.stream().filter(x -> x.length() == 10).collect(Collectors.toList());

        assertTrue(min.size() > 0);
        assertTrue(max.size() > 0);
    }

    @Test
    public void testPasswordMinLengthMaxLengthIncludeUpperCaseIncludeSpecialIncludeDigit() {
        assertThat(faker.internet().password(10, 25, false, false, false), matchesRegularExpression("[a-z]{10,25}"));
        assertThat(faker.internet().password(10, 25, false, true, true), matchesRegularExpression("[a-z\\d!@#$%^&*]{10,25}"));
        assertThat(faker.internet().password(10, 25, true, true, false), matchesRegularExpression("[a-zA-Z!@#$%^&*]{10,25}"));
        assertThat(faker.internet().password(10, 25, true, true, true), matchesRegularExpression("[a-zA-Z\\d!@#$%^&*]{10,25}"));
    }

    @Test
    public void testMacAddress() {
        assertThat(faker.internet().macAddress(), countOf(':', is(5)));
        assertThat(faker.internet().macAddress(""), countOf(':', is(5)));

        assertThat(faker.internet().macAddress("fa:fa:fa"), startsWith("fa:fa:fa"));
        assertThat(faker.internet().macAddress("fa:fa:fa"), countOf(':', is(5)));

        assertThat(faker.internet().macAddress("01:02"), startsWith("01:02"));
        assertThat(faker.internet().macAddress("01:02"), countOf(':', is(5)));

        // loop through 1000 times just to 'run it through the wringer'
        for (int i = 0; i < 1000; i++) {
            assertThat(
                    "Is valid mac format",
                    faker.internet().macAddress(),
                    matchesRegularExpression("[0-9a-fA-F]{2}(\\:([0-9a-fA-F]{1,4})){5}"));
        }
    }

    @Test
    public void testIpV4Address() {
        assertThat(faker.internet().ipV4Address(), countOf('.', is(3)));
        for (int i = 0; i < 100; i++) {
            final String[] octets = faker.internet().ipV4Address().split("\\.");
            assertThat("first octet is 1-255", parseInt(octets[0]),
                    both(greaterThan(0)).and(lessThanOrEqualTo(255)));
            assertThat("second octet is 0-255", parseInt(octets[1]),
                    both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(255)));
            assertThat("second octet is 0-255", parseInt(octets[2]),
                    both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(255)));
            assertThat("second octet is 0-255", parseInt(octets[3]),
                    both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(255)));
        }
    }

    @Test
    public void testIpV4Cidr() {
        assertThat(faker.internet().ipV4Cidr(), countOf('.', is(3)));
        assertThat(faker.internet().ipV4Cidr(), countOf('/', is(1)));

        for (int i = 0; i < 1000; i++) {
            assertThat(parseInt(faker.internet().ipV4Cidr().split("/")[1]),
                    both(greaterThanOrEqualTo(1)).and(lessThan(32)));
        }
    }

    @Test
    public void testPrivateIpV4Address() {
        String tenDot = "^10\\..+";
        String oneTwoSeven = "^127\\..+";
        String oneSixNine = "^169\\.254\\..+";
        String oneNineTwo = "^192\\.168\\..+";
        String oneSevenTwo = "^172\\.(16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\\..+";


        for (int i = 0; i < 1000; i++) {
            String addr = faker.internet().privateIpV4Address();
            assertThat(addr, anyOf(matchesRegularExpression(tenDot),
                    matchesRegularExpression(oneTwoSeven),
                    matchesRegularExpression(oneSixNine),
                    matchesRegularExpression(oneNineTwo),
                    matchesRegularExpression(oneSevenTwo)));
        }
    }

    @Test
    public void testPublicIpV4Address() {
        String tenDot = "^10\\.";
        String oneTwoSeven = "^127\\.";
        String oneSixNine = "^169\\.254";
        String oneNineTwo = "^192\\.168\\.";
        String oneSevenTwo = "^172\\.(16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\\.";
        for (int i = 0; i < 1000; i++) {
            String addr = faker.internet().publicIpV4Address();
            assertThat(addr.matches(tenDot), is(false));
            assertThat(addr.matches(oneTwoSeven), is(false));
            assertThat(addr.matches(oneSixNine), is(false));
            assertThat(addr.matches(oneNineTwo), is(false));
            assertThat(addr.matches(oneSevenTwo), is(false));
        }
    }

    @Test
    public void testIpV6() {
        assertThat(faker.internet().ipV6Address(), countOf(':', is(7)));

        for (int i = 0; i < 1000; i++) {
            assertThat(
                    "Is valid ipv6 format",
                    faker.internet().ipV6Address(),
                    matchesRegularExpression("[0-9a-fA-F]{1,4}(\\:([0-9a-fA-F]{1,4})){1,7}"));
        }
    }

    @Test
    public void testIpV6Cidr() {
        assertThat(faker.internet().ipV6Cidr(), countOf(':', is(7)));
        assertThat(faker.internet().ipV6Cidr(), countOf('/', is(1)));

        for (int i = 0; i < 1000; i++) {
            assertThat(parseInt(faker.internet().ipV6Cidr().split("/")[1]),
                    both(greaterThanOrEqualTo(1)).and(lessThan(128)));
        }
    }

    @Test
    @Repeat(times = 10)
    public void testSlugWithParams() {
        assertThat(faker.internet().slug(ImmutableList.of("a", "b"), "-"), matchesRegularExpression("[a-zA-Z]+\\-[a-zA-Z]+"));
    }

    @Test
    @Repeat(times = 10)
    public void testSlug() {
        assertThat(faker.internet().slug(), matchesRegularExpression("[a-zA-Z]+\\_[a-zA-Z]+"));
    }

    @Test
    @Repeat(times = 10)
    public void testUuid() {
        assertThat(faker.internet().uuid(), matchesRegularExpression("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
    }

    @Test
    @Repeat(times = 100)
    public void testFarsiIDNs() {
        // in this case, we're just making sure Farsi doesn't blow up.
        // there have been issues with Farsi not being produced.
        final Faker f = new Faker(new Locale("fa"));
        assertThat(f.internet().domainName(), not(is(emptyOrNullString())));
        assertThat(f.internet().emailAddress(), not(is(emptyOrNullString())));
        assertThat(f.internet().safeEmailAddress(), not(is(emptyOrNullString())));
        assertThat(f.internet().url(), not(is(emptyOrNullString())));
    }

    @Test
    public void testUserAgent() {
        Internet.UserAgent[] agents = Internet.UserAgent.values();
        for (Internet.UserAgent agent : agents) {
            assertThat(faker.internet().userAgent(agent), not(is(emptyOrNullString())));
        }

        //Test faker.internet().userAgentAny() for random user_agent retrieval.
        assertThat(faker.internet().userAgentAny(), not(is(emptyOrNullString())));
    }

    @Test
    public void testSlugWithNull() {
        assertThat(faker.internet().slug(null, "_"), notNullValue());
    }
}

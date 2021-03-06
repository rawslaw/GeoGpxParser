package io.github.zeroone3010.geogpxparser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This is the unit test class for the GeoGPXParser class.
 */
public class GeoXMLReaderTest {
    Geocache cache1;
    Geocache cache2;
    Geocache cache3;

    @Before
    public void loadCaches() {
        final GeoXMLReader reader = new GeoXMLReader("src/test/java/io/github/zeroone3010/geogpxparser/test.gpx");
        final List<Geocache> caches = reader.parse();
        assertNotNull(caches);
        assertEquals(3, caches.size());
        cache1 = caches.get(0);
        cache2 = caches.get(1);
        cache3 = caches.get(2);
        assertNotNull(cache1);
        assertNotNull(cache2);
        assertNotNull(cache3);
    }

    @Test
    public void test_gcCode() {
        assertEquals("GC123ABC", cache1.getGcCode());
        assertEquals("GC456DEF", cache2.getGcCode());
    }

    @Test
    public void test_latitude() {
        assertEquals(51.5123, cache1.getLatitude(), 0.00001);
        assertEquals(59.8765, cache2.getLatitude(), 0.00001);
    }

    @Test
    public void test_longitude() {
        assertEquals(-0.0789, cache1.getLongitude(), 0.00001);
        assertEquals(10.6543, cache2.getLongitude(), 0.00001);
    }

    @Test
    public void test_country() {
        assertEquals("United Kingdom", cache1.getCountry());
        assertEquals("Norway", cache2.getCountry());
    }

    @Test
    public void test_state() {
        assertEquals("London", cache1.getState());
        assertEquals("Oslo", cache2.getState());
    }

    @Test
    public void test_hidden() {
        assertEquals(2001, cache1.getHidden().getYear());
        assertEquals(10, cache1.getHidden().getMonthValue());
        assertEquals(27, cache1.getHidden().getDayOfMonth());

        assertEquals(2010, cache2.getHidden().getYear());
        assertEquals(2, cache2.getHidden().getMonthValue());
        assertEquals(28, cache2.getHidden().getDayOfMonth());
    }

    @Test
    public void test_type() {
        assertEquals(CacheType.Multi, cache1.getType());
        assertEquals(CacheType.Traditional, cache2.getType());
    }

    @Test
    public void test_size() {
        assertEquals(CacheSize.Not_chosen, cache1.getSize());
        assertEquals(CacheSize.Micro, cache2.getSize());
    }

    @Test
    public void test_difficulty() {
        assertEquals(2, cache1.getDifficulty(), 0.0001);
        assertEquals(3.5, cache2.getDifficulty(), 0.0001);
    }

    @Test
    public void test_terrain() {
        assertEquals(1.5, cache1.getTerrain(), 0.0001);
        assertEquals(4, cache2.getTerrain(), 0.0001);
    }

    @Test
    public void test_attributes() {
        final Map<String, Boolean> attributes1 = cache1.getAttributes();
        final Map<String, Boolean> attributes2 = cache2.getAttributes();
        final Map<String, Boolean> attributes3 = cache3.getAttributes();

        assertEquals(2, attributes1.size());
        assertEquals(true, attributes1.get("Public transportation"));
        assertEquals(true, attributes1.get("Wheelchair accessible"));

        assertEquals(2, attributes2.size());
        assertEquals(false, attributes2.get("Available at all times"));
        assertEquals(true, attributes2.get("Stealth required"));

        assertNotNull(attributes3);
        assertEquals(0, attributes3.size());
    }

    @Test
    public void test_name() {
        assertEquals("Cache I name", cache1.getName());
        assertEquals("Cache II name", cache2.getName());
    }

    @Test
    public void test_owner() {
        assertEquals("owner 1", cache1.getOwner());
        assertEquals("owner 2", cache2.getOwner());
    }

    @Test
    public void test_shortDescription() {
        assertEquals("Short description 1.", cache1.getShortDescription());
        assertEquals("Short description 2.", cache2.getShortDescription());
    }

    @Test
    public void test_longDescription() {
        assertEquals("Long description 1.", cache1.getLongDescription());
        assertEquals("Long description 2.", cache2.getLongDescription());
    }

    @Test
    public void test_hint() {
        assertEquals("Hint 1.", cache1.getHint(true));
        assertEquals("Hint 2.", cache2.getHint(true));
    }

    @Test
    public void test_available() {
        assertEquals(true, cache1.isAvailable());
        assertEquals(false, cache2.isAvailable());
    }

    @Test
    public void test_archived() {
        assertEquals(false, cache1.isArchived());
        assertEquals(true, cache2.isArchived());
    }

    @Test
    public void test_logs1() {
        final List<Log> logs1 = cache1.getLogs();
        assertNotNull(logs1);
        assertEquals(1, logs1.size());
        assertEquals(2010, logs1.get(0).getDate().getYear());
        assertEquals(123, logs1.get(0).getId());
        assertEquals(7, logs1.get(0).getDate().getMonthValue());
        assertEquals(8, logs1.get(0).getDate().getDayOfMonth());
        assertEquals(LogType.FOUND, logs1.get(0).getType());
        assertEquals("Example user", logs1.get(0).getUser());
        assertEquals("TFTC!", logs1.get(0).getText());
    }

    @Test
    public void test_logs2() {
        final List<Log> logs2 = cache2.getLogs();
        assertNotNull(logs2);
        assertEquals(2, logs2.size());
        assertEquals(450, logs2.get(0).getId());
        assertEquals(456, logs2.get(1).getId());
        assertEquals(10, logs2.get(0).getDate().getDayOfMonth());
        assertEquals(15, logs2.get(1).getDate().getDayOfMonth());
        assertEquals(LogType.DNF, logs2.get(0).getType());
        assertEquals(LogType.FOUND, logs2.get(1).getType());
        assertEquals("Example user", logs2.get(0).getUser());
        assertEquals("No luck.", logs2.get(0).getText());
        assertEquals("Example user", logs2.get(1).getUser());
        assertEquals("TNLN.", logs2.get(1).getText());
    }

    @Test
    public void test_empty_logs3() {
        assertNotNull(cache3.getLogs());
        assertEquals(0, cache3.getLogs().size());
    }
}

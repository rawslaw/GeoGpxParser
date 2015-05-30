/*
 * The MIT License - Copyright (c) 2011-2015 Ville Saalo (http://coord.info/PR32K8V)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package zeroone3010.geogpxparser;

import zeroone3010.geogpxparser.cachelistparsers.CacheListParser;
import zeroone3010.geogpxparser.cachelistparsers.CountryStatsParser;
import zeroone3010.geogpxparser.cachelistparsers.DateStatsParser;
import zeroone3010.geogpxparser.cachelistparsers.OwnerStatsParser;
import zeroone3010.geogpxparser.cachelistparsers.StarChallengeParser;
import zeroone3010.geogpxparser.comparison.GeoGPXComparer;
import zeroone3010.geogpxparser.coordinateformatters.CoordinateFormatter;
import zeroone3010.geogpxparser.coordinateformatters.DefaultCoordinateFormatter;
import zeroone3010.geogpxparser.coordinateformatters.DegreesAndMinutesFormatter;
import zeroone3010.geogpxparser.outputformatters.AbstractTabularDataFormatter;
import zeroone3010.geogpxparser.outputformatters.FormatterFactory;
import zeroone3010.geogpxparser.tabular.TableData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class can be used to parse geocaches from a Groundspeak .gpx file into plain old Java objects (POJO). The cache
 * list can then be processed further and various text files can be created out of it.
 *
 * @author Ville Saalo (http://coord.info/PR32K8V)
 */
public final class GeoGPXParser {

    public static void main(final String[] args) throws IOException {
        if (args == null) {
            showInstructions();
        } else if (args.length == 1) {
            createBasicTables(args[0]);
        } else if (args.length == 2) {
            createComparisonTable(args[0], args[1]);
        } else {
            showInstructions();
        }
    }

    private static void createBasicTables(final String file) throws IOException {
        final GeoXMLReader reader = new GeoXMLReader(file);
        final List<Geocache> caches = reader.parse();
        final TableData tabularRepresentation = new CacheListParser(buildCoordinateFormatter()).getTabularInfo(caches);
        final TableData ownerStats = new OwnerStatsParser().getTabularInfo(caches);
        final TableData countryStats = new CountryStatsParser().getTabularInfo(caches);
        final TableData starStats = new StarChallengeParser().getTabularInfo(caches);
        final TableData dateStats = new DateStatsParser().getTabularInfo(caches);

        final String outputType = System.getProperty("output", "html").toLowerCase();

        Stream.of(tabularRepresentation, ownerStats, countryStats, starStats, dateStats)
                .map(td -> FormatterFactory.createFormatter(td, outputType))
                .forEach(GeoGPXParser::writeDataToFile);

        writeHtmlResources();

        info("Done!");
    }

    private static void createComparisonTable(final String file1, final String file2) {
        final GeoGPXComparer comparer = new GeoGPXComparer(file1, file2);
        final TableData compare = comparer.compare();

        writeDataToFile(FormatterFactory.createFormatter(compare, "html"));

        info("Done!");
    }

    private static void showInstructions() {
        System.out.println("Usage:");
        System.out.println("1) java [-DcoordinateFormat=(dd|ddmm)] [-Doutput=(xml|html|txt)] -jar GeoGPXParser.jar caches.gpx");
        System.out.println("2) java [-DcoordinateFormat=(dd|ddmm)] [-Doutput=(xml|html|txt)] -jar GeoGPXParser.jar some/directory/with/gpx/files");
        System.out.println("3) java -jar GeoGPXParser.jar cachesOfUser1.gpx cachesOfUser2.gpx");
        System.out.println("...where \"[...]\" denotes an optional parameter and \"(A|B|C)\" denotes alternatives: either A or B or C.");
        System.exit(1);
    }

    private static CoordinateFormatter buildCoordinateFormatter() {
        final CoordinateFormatter coordinateFormatter;
        final String coordinateFormat = System.getProperty("coordinateFormat", "ddmm").toLowerCase();
        switch (coordinateFormat) {
            case "dd":
                coordinateFormatter = new DefaultCoordinateFormatter();
                break;
            default:
                coordinateFormatter = new DegreesAndMinutesFormatter();
                break;
        }
        return coordinateFormatter;
    }

    private static void info(final String text) {
        System.out.println(text);
    }

    private static <T extends AbstractTabularDataFormatter> void writeDataToFile(final T formatter) {
        final String fileName = formatter.getFileName();
        try {
            info("Writing " + fileName + "...");
            Files.write(Paths.get(fileName), formatter.toString().getBytes(Charset.forName("UTF-8")));
        } catch (IOException ex) {
            System.out.println("Saving the file " + fileName + " failed!");
            ex.printStackTrace();
        }
    }

    private static void writeHtmlResources() throws IOException {
        final ClassLoader classLoader = GeoGPXParser.class.getClassLoader();
        for (final String filename : new String[]{"jquery-1.9.1.min.js", "jquery.tablesorter.min.js", "jquery-ui.min.js"}) {
            try (final InputStream inputStream = classLoader.getResourceAsStream(filename)) {
                Files.copy(inputStream, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}

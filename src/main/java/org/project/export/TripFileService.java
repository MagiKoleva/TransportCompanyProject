package org.project.export;

import org.project.dto.TripExportDto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class TripFileService {

    private static final DateTimeFormatter date_format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void writeTrips(List<TripExportDto> trips, String filename) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filename))) {
            writer.write(
                    "id, type, route, final price, departure date, arrival date, driver, vehicle, paid"
            );
            writer.newLine();

            for (TripExportDto t : trips) {
                writer.write(String.format(
                        "%d, %s, %s-%s, %.2f, %s, %s, %s, %s, %s",
                        t.getId(),
                        t.getTripType(),
                        t.getStartLoc(),
                        t.getEndLoc(),
                        t.getFinalPrice(),
                        t.getDeparture().format(date_format),
                        t.getArrival().format(date_format),
                        t.getDriverName(),
                        t.getVehicleType(),
                        t.isPaid()
                ));
                writer.newLine();
            }
            System.out.println("Trip data written to: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error writing trip file!", e);
        }
    }

    public static void readAndPrintTripsFile(String filename) {
        System.out.println("\n----- FILE CONTENT -----");

        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            lines.forEach((System.out::println));
        } catch (IOException e) {
            System.out.println("Could not read file: " + filename);
        }

        System.out.println("\n----- FILE END -----\n");
    }
}

package configuration;

import lombok.NonNull;
import models.GridConfiguration;
import models.Instruction;
import models.MowerConfiguration;
import models.Orientation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GridConfigurationFactory {

    public GridConfiguration get(@NonNull String filePath) throws Exception {

        List<String> lines = getLinesFromPath(filePath);

        GridConfiguration configuration = new GridConfiguration()
                .setGridMaxCols(intFromChar(lines.get(0), 0))
                .setGridMaxRows(intFromChar(lines.get(0), 1))
                .setMowerConfigurations(new ArrayList<>());

        for (int i = 1; i < lines.size(); i = i + 2) {

            String line1 = lines.get(i);
            String line2 = lines.get(i + 1);

            MowerConfiguration mowerConfiguration = new MowerConfiguration();
            mowerConfiguration.setX(intFromChar(line1, 0));
            mowerConfiguration.setY(intFromChar(line1, 1));
            mowerConfiguration.setOrientation(Orientation.find(stringFromChar(line1, 2)));

            List<Instruction> chars = new ArrayList<>();
            for (Character character : line2.toCharArray()) {
                chars.add(Instruction.find(String.valueOf(character)));
            }

            mowerConfiguration.setInstructions(chars);

            configuration.getMowerConfigurations().add(mowerConfiguration);
        }

        return configuration;
    }

    private List<String> getLinesFromPath(@NonNull String filePath) throws IOException {
        File file = new File(filePath);

        List<String> lines = Files.readAllLines(Paths.get(file.toURI()), Charset.defaultCharset());

        lines = lines.stream()
                .filter(s -> !s.isEmpty())
                .map(line -> line.replace(" ", ""))
                .collect(Collectors.toList());
        return lines;
    }

    private int intFromChar(String s, int i) {
        return Integer.parseInt(stringFromChar(s, i));
    }

    private String stringFromChar(String s, int i) {
        return String.valueOf(s.charAt(i));
    }

}

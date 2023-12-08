import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для обработки CSV файлов и создания коллекции команд.
 */
public class CSVHandler {

    /**
     * Метод для чтения CSV файла и создания коллекции команд.
     *
     * @param path Путь к CSV файлу.
     * @return Коллекция команд.
     */
    public static Map<String, Team> readFile(String path) {
        Map<String, Team> teams = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines = lines.subList(1, lines.size()); // Пропускаем первую строку с заголовками

            for (String line : lines) {
                String[] data = serializeLine(line);
                String teamName = data[1];
                Team team = teams.computeIfAbsent(teamName, Team::new);
                team.addPlayer(new PlayerIndicators(data[0], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return teams;
    }

    /**
     * Метод для обработки строки CSV и разделения на поля.
     *
     * @param line Строка CSV.
     * @return Массив строк с данными.
     */
    private static String[] serializeLine(String line) {
        String[] splited = line.split(",");

        for (int i = 0; i < splited.length; i++) {
            splited[i] = splited[i].replaceAll("^\"|\"$", "");
        }

        return splited;
    }
}
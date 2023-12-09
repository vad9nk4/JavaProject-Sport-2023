# JavaProject-Sport-2023

### 1. Создадим два файла для хранения данных извлечённых из csv - PlayerIndicators и Team

Файлы PlayerMetrics и SportsTeam представляют собой классы, используемые в приложении для представления структуры данных. Они служат для моделирования сущностей, с которыми приложение работает, а именно, игроков и команд.
* SportsTeam - Представляет команду, включающую в себя список игроков и другие характеристики команды
* PlayerMetrics - Описывает показатели игрока, такие как имя, позиция, рост, вес и возраст.

##### SportsTeam

```
import java.util.List;
import java.util.ArrayList;

/**
 * Класс, представляющий спортивную команду.
 */
public class SportsTeam  {
    private final String teamName; // Название команды
    private final List<PlayerMetrics> playersList = new ArrayList<>(); // Список игроков в команде

    /**
     * Конструктор класса Team.
     *
     * @param teamName Название команды.
     */
    public SportsTeam(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Добавляет игрока в команду.
     *
     * @param player Игрок для добавления.
     */
    public void addPlayer(PlayerMetrics player) {
        playersList.add(player);
    }

    /**
     * Получает массив игроков в команде.
     *
     * @return Массив игроков в команде.
     */
    public PlayerMetrics[] getPlayers() {
        PlayerMetrics[] playersArray = new PlayerMetrics[playersList.size()];
        playersList.toArray(playersArray);
        return playersArray;
    }

    /**
     * Получает название команды.
     *
     * @return Название команды.
     */
    public String getName() {
        return teamName;
    }
```

##### PlayerMetrics

```
/**
 * Класс, представляющий показатели игрока.
 */
public class PlayerMetrics  {
    private final String name;       // Имя игрока
    private final String position;   // Позиция игрока
    private final int height;        // Рост игрока в дюймах
    private final int weight;        // Вес игрока в фунтах
    private final double age;        // Возраст игрока

    /**
     * Конструктор класса PlayerIndicators.
     *
     * @param name     Имя игрока.
     * @param position Позиция игрока.
     * @param height   Рост игрока в дюймах.
     * @param weight   Вес игрока в фунтах.
     * @param age      Возраст игрока.
     */
    public PlayerMetrics(String name, String position, int height, int weight, double age) {
        this.name = name;
        this.position = position;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    /**
     * Получение имени игрока.
     *
     * @return Имя игрока.
     */
    public String getName() {
        return name;
    }

    /**
     * Получение позиции игрока.
     *
     * @return Позиция игрока.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Получение роста игрока.
     *
     * @return Рост игрока в дюймах.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Получение веса игрока.
     *
     * @return Вес игрока в фунтах.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Получение возраста игрока.
     *
     * @return Возраст игрока.
     */
    public double getAge() {
        return age;
    }
}
```
<br /><br /><br />

### 2. Создаём класс TeamCSVReader, который будет служить для обработки данных в формате CSV (Comma-Separated Values), который часто используется для представления табличных данных. В этом приложении, класс TeamCSVReader выполняет роль чтения данных из CSV файла и преобразования их в формат, который можно использовать в приложении для дальнейшей обработки.
```
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для обработки CSV файлов и создания коллекции команд.
 */
public class TeamCSVReader  {

    /**
     * Метод для чтения CSV файла и создания коллекции команд.
     *
     * @param path Путь к CSV файлу.
     * @return Коллекция команд.
     */
    public static Map<String, SportsTeam> readFile(String path) {
        Map<String, SportsTeam> teams = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines = lines.subList(1, lines.size()); // Пропускаем первую строку с заголовками

            for (String line : lines) {
                String[] data = serializeLine(line);
                String teamName = data[1];
                SportsTeam team = teams.computeIfAbsent(teamName, SportsTeam::new);
                team.addPlayer(new PlayerMetrics(data[0], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
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
```
<br /><br /><br />

### 3. Создаём класс DatabaseHandler, который будет выполнять роль взаимодействия с базой данных SQLite и предоставляет методы для выполнения различных операций с данными. Он используется для создания, чтения, обновления и удаления данных в базе данных, а также для выполнения запросов и получения статистических данных для анализа спортивных данных.

```
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных SQLite.
 */
public class DatabaseHandler {

    private final Connection connection;

    /**
     * Конструктор класса.
     *
     * @throws SQLException            Исключение, связанное с работой с базой данных.
     * @throws ClassNotFoundException Исключение, если не удается найти класс JDBC драйвера.
     */
    public DatabaseHandler() throws SQLException, ClassNotFoundException {
        // Указываем драйвер JDBC для SQLite
        Class.forName("org.sqlite.JDBC");

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);

        // Устанавливаем соединение с базой данных
        connection = DriverManager.getConnection("jdbc:sqlite:D:\\JDK projects\\MyJavaProject-Sport\\DataBase.db", config.toProperties());

        // Проверка и создание таблиц
        createTables();
    }
```
<br /><br /><br />

### 4. Читаем данные ( столбцы ), и создаём их в таблице базы данных SQLite, после чего передаём туда данных из csv-файла.

```
    /**
     * Метод для создания таблиц в базе данных, если они не существуют.
     *
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    private void createTables() {
        try {
            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS SportsTeam" +
                            "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE);");

            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS PlayerMetrics ("
                            + "playerId INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "name TEXT NOT NULL,"
                            + "position TEXT NOT NULL,"
                            + "height INTEGER NOT NULL,"
                            + "weight INTEGER NOT NULL,"
                            + "age REAL NOT NULL,"
                            + "teamId INTEGER,"
                            + "FOREIGN KEY (teamId) REFERENCES teams(id));");
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
    }
```
<br /><br /><br />

### 5. Создаём метод getAllData в классе DatabaseHandler, который перебирает всю БД и возвращает все данные из неё и в последствии выводим эти данные на экран пользователя в консоли.

```
    /**
     * Метод для получения всех данных из базы.
     *
     * @return Список строк данных.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public List<String> getAllData() throws SQLException {
        List<String> allData = new ArrayList<>();

        String sql = """
                SELECT
                	PlayerMetrics.name, PlayerMetrics.position, PlayerMetrics.height,
                	PlayerMetrics.weight, PlayerMetrics.age, teams.name AS teamName
                FROM `PlayerMetrics`
                	LEFT JOIN `teams`
                		ON teams.id = PlayerMetrics.teamId
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet data = preparedStatement.executeQuery()) {
            while (data.next()) {
                String dbLine = data.getString("name") + ", " +
                        data.getString("position") + ", " +
                        data.getInt("height") + ", " +
                        data.getInt("weight") + ", " +
                        data.getDouble("age") + ", " +
                        data.getString("teamName");

                allData.add(dbLine);
            }
        }

        return allData;
    }
```

* Вывод данных в консоль:<br />
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/63503bb4-fbfe-43e7-8fcc-e61801da2dcd)

<br /><br /><br />

### 6. Создаём метод который выполняет первое задание, а именно строит график среднего возраста команд.

* Достаём данные из базы данных:

```
    /**
     * Метод для получения среднего возраста команды.
     *
     * @param teamName Имя команды.
     * @return Средний возраст команды.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public double getAverageAge(String teamName) throws SQLException {
        int teamId = getTeamNumber(teamName).getInt(1);
        String sql = "SELECT AVG(age) FROM `PlayerMetrics` WHERE `teamId` = '" + teamId + "'";
        try (ResultSet averageHeight = connection.createStatement().executeQuery(sql)) {
            return averageHeight.getDouble(1);
        }
    }
```

* Создаём график: <br />

```
    /**
     * Создание и сохранение графика среднего возраста команд.
     *
     * @throws SQLException Если возникает ошибка SQL.
     * @throws IOException  Если возникает ошибка ввода/вывода.
     */
    private static void createChart() throws SQLException, IOException {
        // Отключение логирования SLF4J
        System.setProperty("org.jfree.chart.util.LogTarget", "org.jfree.chart.util.NullLogTarget");

        // Задание данных для графика
        var dataset = new DefaultCategoryDataset();

        // Перехватываем SQLException из метода getAverageAge
        try {
            dataBase.getTeamsNames().forEach(name -> {
                try {
                    dataset.addValue(dataBase.getAverageAge(name), "Возраст", name);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Создание графика
        var chart = ChartFactory.createLineChart(
                "Задание 1 - Средний возраст всех команд",
                "Команды",
                "Возраст",

                dataset
        );

        // Настройка внешнего вида графика
        var plot = (CategoryPlot) chart.getPlot();
        var renderer = new LineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        var yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setTickLabelPaint(Color.BLACK);

        var xAxis = (CategoryAxis) plot.getDomainAxis();
        xAxis.setTickLabelPaint(Color.BLACK);

        // Настройка заголовка
        var title = chart.getTitle();
        title.setPaint(Color.DARK_GRAY);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Настройка легенды
        var legend = chart.getLegend();
        legend.setItemPaint(Color.BLACK);

        // Сохранение графика
        try {
            ChartUtils.saveChartAsPNG(
                    new File("D:\\JDK projects\\MyJavaProject-Sport\\chart.png"),
                    chart,
                    1920,
                    1080
            );
            System.out.println("График сохранен в файл chart.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
<br /><br /><br />



### 7. Полученный график:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/8df31994-74b4-415f-bf10-756c9ae85213)

<br /><br /><br />

### 8. Создаём метод для второго задания, а именно: "Найдите команду с самым высоким средним ростом. Выведите в консоль 5 самых высоких игроков команды."

* Делаем нужные запросы из БД:

```
    /**
     * Метод для получения имен самых высоких игроков команды.
     *
     * @param teamName Имя команды.
     * @return Массив имен самых высоких игроков.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public String[] getMaxHeightPlayers(String teamName) throws SQLException {
        int teamId = getTeamNumber(teamName).getInt(1);
        String sql = "SELECT DISTINCT `name` FROM `PlayerMetrics` WHERE `teamId` = " + teamId + " ORDER BY `height` DESC LIMIT 5";
        ResultSet data = connection.createStatement().executeQuery(sql);

        List<String> playerNames = new ArrayList<>();
        while (data.next()) {
            playerNames.add(data.getString(1));
        }

        return playerNames.toArray(new String[0]);
    }

    /**
     * Метод для получения среднего роста команды.
     *
     * @param teamName Имя команды.
     * @return Средний рост команды.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public double getAverageHeight(String teamName) throws SQLException {
        int teamId = getTeamNumber(teamName).getInt(1);
        String sql = "SELECT AVG(height) FROM PlayerMetrics WHERE `teamId` = '" + teamId + "'";
        try (ResultSet averageHeight = connection.createStatement().executeQuery(sql)) {
            return averageHeight.getDouble(1);
        }
    }
```

* Создаём методы для обработки и вывода в консоль:

```
    /**
     * Расчет и отображение информации о команде с самыми высокими игроками.
     *
     * @throws SQLException Если возникает ошибка SQL.
     */
    private static void calculateAndDisplayTallestTeamInfo() throws SQLException {
        var teamsList = dataBase.getTeamsNames();
        var maxHeightTeam = findTeamWithMaxHeight(teamsList);

        // Отображение команды с самыми высокими игроками
        System.out.println("Команда с самым большим средним ростом - " + maxHeightTeam);
        System.out.println("Самые высокие игроки: " +
                String.join(", ", dataBase.getMaxHeightPlayers(maxHeightTeam)));
    }

    /**
     * Поиск команды с самыми высокими игроками.
     *
     * @param teams Список команд.
     * @return Имя команды с самыми высокими игроками.
     * @throws SQLException Если возникает ошибка SQL.
     */
    private static String findTeamWithMaxHeight(List<String> teams) throws SQLException {
        String maxHeightTeam = "";
        double maxHeight = -1;

        // Поиск команды с самыми высокими игроками
        for (String name : teams) {
            double height = dataBase.getAverageHeight(name);
            if (height > maxHeight) {
                maxHeight = height;
                maxHeightTeam = name;
            }
        }

        return maxHeightTeam;
    }
```
<br /><br /><br />

### 9. Создаём метод для третьего задания, а именно "Найдите команду, с средним ростом равным от 74 до 78 inches и средним весом от 190 до 210 lbs, с самым высоким средним возрастом."
* Делаем нужные запросы из БД:
  
```
    /**
     * Метод для получения среднего роста команды.
     *
     * @param teamName Имя команды.
     * @return Средний рост команды.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public double getAverageHeight(String teamName) throws SQLException {
        int teamId = getTeamNumber(teamName).getInt(1);
        String sql = "SELECT AVG(height) FROM PlayerMetrics WHERE `teamId` = '" + teamId + "'";
        try (ResultSet averageHeight = connection.createStatement().executeQuery(sql)) {
            return averageHeight.getDouble(1);
        }
    }

    /**
     * Метод для получения среднего веса команды.
     *
     * @param teamName Имя команды.
     * @return Средний вес команды.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public double getAverageWeight(String teamName) throws SQLException {
        int teamId = getTeamNumber(teamName).getInt(1);
        String sql = "SELECT AVG(weight) FROM PlayerMetrics WHERE `teamId` = '" + teamId + "'";
        try (ResultSet averageHeight = connection.createStatement().executeQuery(sql)) {
            return averageHeight.getDouble(1);
        }
    }
```

* Создаём методы для обработки и вывода в консоль:

```
    /**
     * Поиск и отображение информации о команде с самыми старшими игроками и определенными характеристиками.
     *
     * @throws SQLException Если возникает ошибка SQL.
     */
    private static void findAndDisplayTeamWithOldestPlayers() throws SQLException {
        var teamsList = dataBase.getTeamsNames();
        var oldestPlayersTeam = findTeamWithMaxAgeAndAttributes(teamsList);

        // Отображение информации о команде с самыми старшими игроками и определенными характеристиками
        System.out.println("Команда, со средним ростом от 74 до 78 inches, средним весом от 190 до 210 lbs и с самым высоким средним возрастом - " + oldestPlayersTeam);
    }

    private static String findTeamWithMaxAgeAndAttributes(List<String> teams) throws SQLException {
        String oldestPlayersTeam = "";
        double maxAge = -1;

        // Поиск команды с самыми старшими игроками и определенными характеристиками
        for (String name : teams) {
            double age = dataBase.getAverageAge(name);
            double averageHeight = dataBase.getAverageHeight(name);
            double averageWeight = dataBase.getAverageWeight(name);

            if (age > maxAge && averageHeight >= 74 && averageHeight <= 78 && averageWeight >= 190 && averageWeight <= 210) {
                maxAge = age;
                oldestPlayersTeam = name;
            }
        }

        return oldestPlayersTeam;
    }
```
<br /><br /><br />

### 10. Итоговый вывод результата выполнения программой заданий:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/85c26fb5-5df6-47ec-ae4e-b3138c04afb0)

<br /><br /><br />

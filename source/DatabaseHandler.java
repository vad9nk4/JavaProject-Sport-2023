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

    /**
     * Метод для вставки команды в базу данных.
     *
     * @param sportsTeam Команда для вставки.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public void insertTeam(SportsTeam sportsTeam) throws SQLException {
        if (!teamExists(sportsTeam.getName())) {
            String sqlInsertTeam = "INSERT INTO teams(name) VALUES(?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertTeam)) {
                preparedStatement.setString(1, sportsTeam.getName());
                preparedStatement.executeUpdate();
            }
        }

        for (PlayerMetrics playerMetrics : sportsTeam.getPlayers()) {
            insertPlayer(playerMetrics, getTeamId(sportsTeam.getName()));
        }
    }

    /**
     * Метод для проверки существования команды в базе данных.
     *
     * @param name Имя команды.
     * @return true, если команда существует, иначе false.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    private boolean teamExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM teams WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt(1) > 0;
        }
    }

    /**
     * Метод для получения идентификатора команды по имени.
     *
     * @param name Имя команды.
     * @return Идентификатор команды.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    private int getTeamId(String name) throws SQLException {
        String sql = "SELECT id FROM teams WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("id");
        }
    }

    /**
     * Метод для вставки игрока в базу данных.
     *
     * @param playerMetrics Игрок для вставки.
     * @param teamId Идентификатор команды.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public void insertPlayer(PlayerMetrics playerMetrics, int teamId) throws SQLException {
        String sql = "INSERT INTO PlayerMetrics(name, position, height, weight, age, teamId) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerMetrics.getName());
            preparedStatement.setString(2, playerMetrics.getPosition());
            preparedStatement.setInt(3, playerMetrics.getHeight());
            preparedStatement.setInt(4, playerMetrics.getWeight());
            preparedStatement.setDouble(5, playerMetrics.getAge());
            preparedStatement.setInt(6, teamId);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Метод для получения номера команды по имени.
     *
     * @param name Имя команды.
     * @return Результат выполнения SQL-запроса.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    private ResultSet getTeamNumber(String name) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM `teams` WHERE `name` = '" + name + "'");
    }

    /**
     * Метод для получения списка имен команд.
     *
     * @return Список имен команд.
     * @throws SQLException Исключение, связанное с работой с базой данных.
     */
    public List<String> getTeamsNames() throws SQLException {
        List<String> teams = new ArrayList<>();
        try (ResultSet data = connection.createStatement().executeQuery("SELECT `name` FROM `teams`")) {
            while (data.next()) {
                teams.add(data.getString(1));
            }
        }
        return teams;
    }

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
}
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;



/**
 * Основной класс приложения для анализа спортивных данных.
 */
public class Main {

    private static SQLHandler dataBase;

    /**
     * Точка входа в программу.
     *
     * @param args Параметры командной строки.
     */
    public static void main(String[] args) {
        try {
            // Импорт данных из CSV и вставка в базу данных
            importDataFromCSV("Показатели спортивных команд.csv");

            // Вывод всех данных
            displayDatabaseData();

            // Задание 1: Создание и сохранение графика
            System.out.println("Задание 1:");
            createChart();
            System.out.println();

            // Задание 2: Расчет и отображение информации о команде с самыми высокими игроками
            System.out.println("Задание 2:");
            calculateAndDisplayTallestTeamInfo();
            System.out.println();

            // Задание 3: Поиск и отображение информации о команде с самыми старшими игроками и определенными характеристиками
            System.out.println("Задание 3:");
            findAndDisplayTeamWithOldestPlayers();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Импорт данных из CSV файла и вставка в базу данных.
     *
     * @param csvFilePath Путь к CSV файлу.
     * @throws ClassNotFoundException Если не удается найти класс.
     * @throws IOException            Если возникает ошибка ввода/вывода.
     * @throws SQLException           Если возникает ошибка SQL.
     */
    private static void importDataFromCSV(String csvFilePath) throws ClassNotFoundException, IOException, SQLException {
        // Разбор CSV файла
        var teams = CSVHandler.readFile(csvFilePath);

        // Инициализация базы данных
        dataBase = new SQLHandler();

        // Вставка данных в базу данных
        try {
            teams.values().forEach(team -> {
                try {
                    dataBase.insertTeam(team);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Отображение данных из базы данных.
     *
     * @throws SQLException Если возникает ошибка SQL.
     */
    private static void displayDatabaseData() throws SQLException {
        System.out.println("Данные из базы данных:");
        System.out.println(String.join("\n", dataBase.getAllData()));
        System.out.println();
    }

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
}

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
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/555c5da1-1678-409b-a98a-381f4c6c6cc7)

<br /><br /><br />

### 4. Читаем данные ( столбцы ), и создаём их в таблице базы данных SQLite, после чего передаём туда данных из csv-файла.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/117cc093-f9d0-4cb0-87b9-d74461a33b04)

<br /><br /><br />

### 5. Создаём метод getAllData в классе DatabaseHandler, который перебирает всю БД и возвращает все данные из неё и в последствии выводим эти данные на экран пользователя в консоли.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/4c7a5bfa-a058-47b7-a7dd-b16ef7c0eefc)
* Вывод данных в консоль:<br />
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/63503bb4-fbfe-43e7-8fcc-e61801da2dcd)

<br /><br /><br />

### 6. Создаём метод который выполняет первое задание, а именно строит график среднего возраста команд.

* Достаём данные из базы данных:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/5d0d5def-7e6d-4e6a-8b27-92d970a35069)


* Создаём график: <br />
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/92cf2f1d-5640-45f9-9be3-7cc883ddabd4)
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/c23ac017-cc9b-452e-9658-f92e5ab079b6)
<br /><br /><br />



### 7. Полученный график:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/8df31994-74b4-415f-bf10-756c9ae85213)

<br /><br /><br />

### 8. Создаём метод для второго задания, а именно: "Найдите команду с самым высоким средним ростом. Выведите в консоль 5 самых высоких игроков команды."

* Делаем нужные запросы из БД:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/90e1e0e6-83eb-4b29-8538-6913a470f752)

* Создаём методы для обработки и вывода в консоль:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/00d4dcc3-c568-4f8c-be91-84013c3db8fa)

<br /><br /><br />

### 9. Создаём метод для третьего задания, а именно "Найдите команду, с средним ростом равным от 74 до 78 inches и средним весом от 190 до 210 lbs, с самым высоким средним возрастом."
* Делаем нужные запросы из БД:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/a04cc553-e721-46a7-be76-893d905aedf5)

* Создаём методы для обработки и вывода в консоль:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/8932fde9-f8df-41e9-9f37-6b5a4f4758b6)

<br /><br /><br />

### 10. Итоговый вывод результата выполнения программой заданий:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/85c26fb5-5df6-47ec-ae4e-b3138c04afb0)

<br /><br /><br />

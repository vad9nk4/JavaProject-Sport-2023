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
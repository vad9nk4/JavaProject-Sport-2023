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
}
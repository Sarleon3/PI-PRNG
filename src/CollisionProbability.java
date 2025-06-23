public class CollisionProbability {
    public static void main(String[] args) {
        int totalSequences = 99999993;     // Общее количество последовательностей
        int uniqueSequences = 63209542;    // Количество уникальных последовательностей
        int repeatSequences = 36790451;    // Количество повторений

        // Рассчитываем вероятность коллизии
        double collisionProbability = (double) repeatSequences / totalSequences;

        // Выводим результат
        System.out.printf("Вероятность коллизии: %.2f%%%n", collisionProbability * 100);
    }
}

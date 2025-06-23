package RealBrainFuck;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JitterAnalysis {

    public static void main(String[] args) {
        // Массив абсолютных значений разниц (diffs)
        long[] diffs = {
                1862713793L,1308667182L,1945409297L,1051664615L,3500437235L,3519468003L,1615112616L,547370421L,141736684L,2294687651L,218822756L,165777822L,56259509L,2600102365L,630731935L,495252650L,816588294L,1673297556L,1398835150L,717758758L,567439750L,431107071L,116867675L,27208301L,570695662L,2725596611L,1434000539L,2588675331L,3644045757L,1262085921L,1545749738L,2500916972L,330489556L,2553768392L,328460949L,1046219005L,2313967888L,2392915256L,754908937L,156859743L,611501219L,1638690317L,1830626L,1947252261L,2278514193L,160780385L,687480185L,72223359L,3680751200L,3067253326L,264881903L,186182680L,2061055328L,1624603969L,696843407L,2430529492L,2332639403L,814816178L,454361529L,1091719201L,618436512L,589484712L,2266801173L,2930524280L,1018409490L,1216958761L,1494859112L,455991003L,579150505L,1780367162L,1614962109L,736797800L,1498344992L,1630192922L,1067068566L,3252157890L,76739275L,3887432665L,1531498242L,423612318L,1727833034L,2593502242L,2261019782L,1382100941L,1309541167L,2769219724L,1605074063L,41516816L,965150161L,2630462339L,2631101804L,2124340573L,1050385570L,968227659L,188924136L,35806092L,924079660L,386091449L,37776442L,398930265L,1210443566L,2687749662L,264354410L,42119389L,2761046298L,1212802797L,2463432899L,1605142888L,1687470045L,83099196L,2725707963L,596824050L,515735114L,1937463653L,3341732215L,1613607293L,1796584444L,619427841L,2546511714L,3256571516L,2423207106L,2257029073L,1841697583L,1485474086L,3139101445L,2390012596L,28377734L,2646623781L,78202672L,3615839972L,690977369L,2736397714L,3564469145L,1071848706L,410159699L,731320703L,354369898L,2021833281L,683118421L,280258717L,2939362285L,2460493254L,503623470L,1244543129L,1900479238L,3177203727L,1048601176L,137490312L,1900542793L,2906511371L,2587593994L,1791351996L,1293054234L,1567050966L,1912166105L,293509089L,1393977617L,2072564084L,3709574090L,3892702752L,2250651314L,157845045L,1366535902L,202403570L,65293544L,841689992L,1776201340L,2913748543L,1350968429L,200332585L,2059718415L,1353674509L,1496331577L,3825896520L,1732089348L,531213298L,1787713169L,2187048871L,2087677471L,2461403432L,976367487L,1236283079L,2451362391L,2033938350L,1420170636L,1206008995L,1941190795L,2283753951L,601096443L,822107836L,3368901418L,3042150404L,3340220338L,766584722L,1167398195L,2277158155L,568084352L,2818319345L,3518406265L,3125582074L

        };

        double mean = calculateMean(diffs);
        double stdDev = calculateStdDev(diffs, mean);
        double entropy = calculateShannonEntropy(diffs, 10); // 10 бинов по умолчанию

        System.out.printf("Mean: %.2f ns\n", mean);
        System.out.printf("Standard Deviation: %.2f ns\n", stdDev);
        System.out.printf("Shannon Entropy: %.4f bits\n", entropy);
    }

    // Среднее значение
    public static double calculateMean(long[] data) {
        long sum = 0;
        for (long v : data) {
            sum += v;
        }
        return (double) sum / data.length;
    }

    // Стандартное отклонение
    public static double calculateStdDev(long[] data, double mean) {
        double sum = 0;
        for (long v : data) {
            sum += (v - mean) * (v - mean);
        }
        return Math.sqrt(sum / data.length);
    }

    // Энтропия Шеннона с биннингом
    public static double calculateShannonEntropy(long[] data, int binsCount) {
        long min = Arrays.stream(data).min().getAsLong();
        long max = Arrays.stream(data).max().getAsLong();

        double binWidth = (double) (max - min) / binsCount;

        // Считаем сколько элементов попадает в каждый бин
        int[] bins = new int[binsCount];
        for (long v : data) {
            int binIndex = (int) ((v - min) / binWidth);
            if (binIndex == binsCount) binIndex--; // крайний случай для max значения
            bins[binIndex]++;
        }

        // Общая сумма
        double total = data.length;

        // Рассчитываем вероятность попадания в бин и энтропию
        double entropy = 0;
        for (int count : bins) {
            if (count == 0) continue;
            double p = count / total;
            entropy -= p * (Math.log(p) / Math.log(2));
        }

        return entropy;
    }
}

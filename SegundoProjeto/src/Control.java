import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

public class Control 
{
    
    public static void main(String[] args) throws Exception 
    {
        List<String> lines = Files.readAllLines(Paths.get("H:\\SecondProject\\src\\files\\game-reviews.csv"));
        lines = lines.subList(1, lines.size());

        Map<String, List<Review>> reviewsYear = lines.stream()
                                                     .map(Control::convertToReview)
                                                     .collect(groupingBy(Review::getReleaseYear, TreeMap::new, toList()));

        baseControl(reviewsYear);
        exibirAnoLancamentoMaiorNumeroJogosAcao(reviewsYear);
    }

    private static Review convertToReview(String line) 
    {
        String[] values = line.split(";");
        return new Review(values[0], values[6], values[2], values[4], Double.parseDouble(values[3]));
    }

    private static void exibirAnoLancamentoMaiorNumeroJogosAcao(Map<String, List<Review>> reviewsYear) 
    {
        long biggestActionGamesAmount = 0;
        String yearBiggestActionGamesAmount = null;

        for (String year : reviewsYear.keySet()) 
        {
            long actionGamesAmount = reviewsYear.get(year).stream().filter(review -> review.getGenre().equals("Action")).map(Review::getGame).distinct().count();

            if (biggestActionGamesAmount < actionGamesAmount) 
            {
                biggestActionGamesAmount = actionGamesAmount;
                yearBiggestActionGamesAmount = year;
            }
        }

        System.out.println("Ano em que foi lançado maior número de jogos do gênero ‘Action’?: " + yearBiggestActionGamesAmount);
    }

    private static void baseControl(Map<String, List<Review>> reviewsYear) 
    {
        reviewsYear.forEach((year, reviews) -> 
        {
            double sumScore = 0;
            double mediocre = 0;

            for (Review game : reviews) 
            {
                sumScore += game.getScore();
                mediocre += (game.getReview().equals("Mediocre") ? 1 : 0);
            }

            double getMediaAritmetica = sumScore / reviews.size();
            List<String> worstToBestReviews = sortReviewsFromWorstToBest(reviews);

            System.out.println();
            System.out.println(String.format("Número de reviews no ano %s: %s", year, reviews.size()));
            System.out.println(String.format("Mediocre reviews: %.2f%%", (mediocre / reviews.size()) * 100));
            System.out.println(String.format("Média aritmética: %.2f", getMediaAritmetica));
            System.out.println(String.format("Desvio padrão populacional: %.2f", getDesvioPadrao(reviews, getMediaAritmetica)));
            System.out.println(String.format("Melhor jogo: %s", worstToBestReviews.get(worstToBestReviews.size() - 1)));
            System.out.println(String.format("Pior jogo: %s", worstToBestReviews.get(0)));
            System.out.println("\n---------------------------------");
        });
    }

    private static List<String> sortReviewsFromWorstToBest(List<Review> reviews) 
    {
        return reviews.stream().collect(groupingBy(Review::getGame, summingDouble(Review::getScore))).entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).collect(toList());
    }

    private static double getDesvioPadrao(List<Review> reviews, final double average) 
    {
        double variacao = reviews.stream().mapToDouble(review -> Math.pow(review.getScore() - average, 2)).sum() / reviews.size();
        return Math.sqrt(variacao);
    }
}

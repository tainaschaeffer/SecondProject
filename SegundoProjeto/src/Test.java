import java.util.TreeMap;

public class Test 
{
    public static void main(String[] args) 
    {
        SimpleReader f = new SimpleReader("H:\\SecondProject\\src\\files\\game-reviews.csv");
        TreeMap<String, Integer> map = new TreeMap<>();
        
        String s = f.readLine(); 
        
        while (s != null) 
        {
            System.out.println(s);
            s = f.readLine();
        }
        
        f.close();
    }
}

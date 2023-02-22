public class Main2 {

    public static void correctUsage()
    {
        System.out.println("\nIncorrect number of arguments.\nUsage:\n "+
                "java   \n");
        System.exit(1);
    }

    public static void main (String args[])
    {
        if (args.length != 3) correctUsage();
        try
        {
            HelloPostgresql2 demo = new HelloPostgresql2(args);
            HelloPostgresql2.WeightedGraphBuilder(demo.db, "P1");

            demo.db.close();
        }
        catch (Exception ex)
        {
            System.out.println("***Exception:\n"+ex);
            ex.printStackTrace();
        }
    }
}

public class Main1 {

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
            HelloPostgresql demo = new HelloPostgresql(args);
            HelloPostgresql.recursion("P1", demo.db);

            demo.db.close();
        }
        catch (Exception ex)
        {
            System.out.println("***Exception:\n"+ex);
            ex.printStackTrace();
        }
    }
}

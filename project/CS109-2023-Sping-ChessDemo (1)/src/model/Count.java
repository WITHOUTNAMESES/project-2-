package model;

public class Count {

    private static int COUNT = 0;

    public static void setCOUNT(int a){
        COUNT = a;
    }

    public static void plusCOUNT(){
        COUNT++;
    }

    public static int getCOUNT(){
        return COUNT;
    }

    public static void resetCOUNT(){
        COUNT = 0;
    }

    public static void minCOUNT(){
        COUNT--;
    }
}

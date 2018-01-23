package test.java.com.bt.om;

public class Test {
    public static void main(String[] args) {
        int[] a = new int[] { 1, 2};
        int t = a.length % 3 == 0 ? a.length / 3 : a.length / 3 + 1;
        if (t < 3)
            t = 3;
        for (int i = 0; i < t; i++) {
            System.out.print(i);
            System.out.print(i+t);
            System.out.print(i+2*t);
            
//            for (int j = 0; j < 3; j++) {
//                if ((i + (j * 3)) < a.length) {
//                }else{
//                    System.out.print("无");
//                }
//            }
            System.out.println();
        }

    }
}

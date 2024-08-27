
import java.util.*;

public static void main(String[] args) {

    byte m1, m2, m3;

    Scanner sc = new Scanner(System.in);

    System.out.println("enter you're marks in physics: ");
    m1 = sc.nextByte();

    System.out.println("enter you're marks in maths: ");
    m2 = sc.nextByte();

    System.out.println("enter you're marks in chemistry: ");
    m3 = sc.nextByte();

    float avg = (m1 + m2 + m3) / 3.0f;
    System.out.println("average of you're all three subjects are : " + avg);

    if (avg >= 40 && m1 >= 33 && m2 >= 33 && m3 >= 33) {
        System.out.println("you're passed this exam");
    } else {
        System.out.println("you have failed this exam");

    }

}

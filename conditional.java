import java.util.*;
    
public static void main(String[] args) {
    Scanner sc=new Scanner(System.in);
    System.out.println("what's web-site do u wanna check ");
    String website = sc.next();

    
    if(website.endsWith(".org")){
        System.out.println("it's organisation website");
    }
    else if(website.endsWith(".in")){
        System.out.println("it's indian website");

    }
    else if(website.endsWith(".com")){
        System.out.println("it's commerical website ");

    }
}

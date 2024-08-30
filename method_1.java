public class method_1 {
    
    static void pattern_1(int n){
        for(int i=0;i<n;i++){
            for(int j=0;j<n-i;j++){
                System.out.print(" *");
            }
            System.out.println(" ");
        }
    }
    public static void main(String[] args) {
        pattern_1(7);
    }
}


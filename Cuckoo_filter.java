import java.util.Scanner;
import java.security.*;
import javax.crypto.*;

/**
 * Created by shaileshvajpayee on 2/14/17.
 */
public class Cuckoo_filter {
    private static int[] filter;
    private static int filter_size;
    private static int max_num_kicks;
    private static StringBuilder fingerprint_table;

    public Cuckoo_filter() {
        filter = new int[filter_size];
        fingerprint_table = new StringBuilder();
    }

    public int lookup(){
        return 0;
    }

    public int hash(int IP){
        return IP%10;
    }

    public int fingerprint(int IP){
        return (IP - 9);
    }

    public boolean insert(int IP){
        int f = fingerprint(IP);
        fingerprint_table.append(IP + " : " + f + "\n");
        int i1 = hash(IP);
        int i2 = i1 ^ hash(f);
        System.out.println(IP +" has possible indices : " + i1 + " , " + i2);
        int index = 0;
        if(filter[i1] == 0) { //empty?
            index = i1;
            filter[index] = f;
            System.out.println(IP + " Inserted at i1 : " + i1);
            return true;
        }
        else if(filter[i2] == 0) { //empty?
            index = i2;
            filter[index] = f;
            System.out.println(IP + " Inserted at i2 : " + i2);
            return true;
        }
        else{
            index = i1; // any random i1 or i2
            for(int n = 0; n < max_num_kicks; n++) {
                int f2 = filter[index]; // fingerprint to be replaced (old)
                filter[index] = f; // replace fingerprint
                System.out.println(f + " Inserted at : " + index);
                index = index ^ hash(f2); // second position of the old fingerprint
                System.out.println("New index for " + f2 + " is " + index);
                if(filter[index] == 0) { //empty?
                    filter[index] = f2;
                    System.out.println(f + " Inserted at : " + index);
                    return true;
                }
                f = f2;
            }
        }
        System.out.println("Could not insert " + IP);
        return false; // Filter is full
    }

    public boolean lookup(int IP){
        int f = fingerprint(IP);
        int i1 = hash(IP);
        if(filter[i1] == f){
            System.out.println("Found " + IP + " : " + f + " at " + i1);
            return true;
        }
        int i2 = i1 ^ hash(f);
        if(filter[i2] == f){
            System.out.println("Found " + IP + " : " + f + " at " + i2);
            return true;
        }
        return false;
    }

//    public int hashCode(String s) {
//        if (h == 0 && value.length > 0) {
//            char val[] = value;
//
//            for (int i = 0; i < value.length; i++) {
//                h = 31 * h + val[i];
//            }
//            hash = h;
//        }
//        return h;
//    }


    public static void get_SHA_fingerpint(String IP) throws Exception{
        byte[] input = IP.getBytes();
        MessageDigest SHA1 = MessageDigest.getInstance("SHA1");
        SHA1.update(input);
        byte[] digest = SHA1.digest();
        System.out.println("Size of digest is : " + digest.length);
        StringBuilder sb = new StringBuilder();
        for(byte b:digest){
            sb.append(String.format("%02X",b));
        }
        System.out.println(IP +" has fingerprint : " + sb.toString());
    }

    public void ToString(){
        for(int i = 0;i < filter.length;i++){
            System.out.println(i + " : " + filter[i]);
        }
    }


    public static void main(String[] args) throws Exception{
//        for(int i = 2; i < 255; i++) {
//            get_SHA_fingerpint("127.0.0." + i);
//        }
//        String s = "";
//        s.hashCode();
//        System.exit(0);
        Scanner s = new Scanner(System.in);
//        System.out.println("Enter size of the filter!");
//        filter_size = Integer.parseInt(s.nextLine());
        filter_size = 10;
        max_num_kicks = 10;
        Cuckoo_filter cuckoo = new Cuckoo_filter();
        int[] IPs = {11,18,10,28};
        for(int i:IPs){
            cuckoo.insert(i);
            System.out.println("\n" + fingerprint_table.toString());
            cuckoo.ToString();
        }
        for(int i:IPs){
            cuckoo.lookup(i);
        }
    }
}

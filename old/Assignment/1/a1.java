import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.lang.StringBuffer;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!"); 
    }
}

/*public class ReadFileCount 
{
   

    //public static void main (String[] args) // throws FileNotFoundException
   // {
   //     System.out.println("File does not exist");

        /*
         * Scanner scan = new Scanner(System.in);
         * System.out.println("Enter file name: "); String fileName = scan.next();
         * 
         * StringBuffer finalsb = new StringBuffer(); File file = new File(fileName); if
         * (file.exists()) { int numberOfLines = 0; int numberOfCharacters = 0; int
         * numberOfWords = 0; Scanner scan1 = new Scanner(file);
         * 
         * while (scan1.hasNextLine()) { StringBuffer sb = new StringBuffer();
         * 
         * String s = scan1.nextLine(); sb.append(s);
         * 
         * numberOfLines++; numberOfCharacters = numberOfCharacters + s.length();
         * 
         * } System.out.println("Number of lines : " + numberOfLines); PrintStream ps =
         * new PrintStream(file);
         * 
         * ps.print(finalsb.toString()); ps.flush(); ps.close();
         * System.out.println("Data written to the file " + fileName); } else {
         * System.out.println("File does not exist"); }
        

    }
} /*

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class hash {
    public static void main(String[] args) {
        String[] files = {"personal.txt", "personal_.txt"};
        String[] algorithms = {"md5sum", "sha1sum", "sha224sum", "sha256sum", "sha384sum", "sha512sum", "b2sum"};

        try {
            File outputFile = new File("diff.txt");
            // Create the file if it doesn't exist
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))){
            for (String algorithm : algorithms) {
                writer.println(algorithm + " " + files[0]);
                writer.println(algorithm + " " + files[1]);
                Process process1 = new ProcessBuilder(algorithm, files[0]).start();
                Process process2 = new ProcessBuilder(algorithm, files[1]).start();

                // Read the output of the first command
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
                String output1 = reader1.readLine();

                // Read the output of the second command
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
                String output2 = reader2.readLine();

                // Extract hash values
                String hash1 = extractHash(output1);
                String hash2 = extractHash(output2); 

                // Print results
                writer.println(hash1);
                writer.println(hash2);

                // Count the differing bits
                int differingBits = countDifferingBits(hash1, hash2);

                writer.println("Różniące się Bity: " + differingBits + " z " + hash1.length() * 4 + ", Procentowo: " + (100.0 * differingBits / (hash1.length() * 4)) + "%");
                writer.println();
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String extractHash(String output) {
        // Assuming the hash is the first word in the output
        return output.split("\\s+")[0];
    }
    private static int countDifferingBits(String hex1, String hex2) {
    if (hex1.length() != hex2.length()) {
        throw new IllegalArgumentException("Hex strings must be of equal length");
    }

    int differingBits = 0;
    for (int i = 0; i < hex1.length(); i++) {
        String bin1 = Integer.toBinaryString(Integer.parseInt(hex1.substring(i, i+1), 16));
        String bin2 = Integer.toBinaryString(Integer.parseInt(hex2.substring(i, i+1), 16));

        // Pad the binary strings with leading zeros to ensure they have the same length
        while (bin1.length() < 4) {
            bin1 = "0" + bin1;
        }
        while (bin2.length() < 4) {
            bin2 = "0" + bin2;
        }

        // Count the differing bits
        for (int j = 0; j < bin1.length(); j++) {
            if (bin1.charAt(j) != bin2.charAt(j)) {
                differingBits++;
            }
        }
    }

    return differingBits;
    }
}

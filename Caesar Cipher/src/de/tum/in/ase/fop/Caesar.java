package de.tum.in.ase.fop;
import java.lang.String;

public class Caesar {
    public static void main(String[] args) {
        Caesar caesar = new Caesar();
        String plain = "abcABCdef!!!";
        String encrypted = caesar.encrypt(plain, 14);
        String decrypted = caesar.decrypt(encrypted, 14);
        int shift = caesar.getShift(decrypted, encrypted);
        System.out.println("Plain: " + plain);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Shift: " + shift);
    }

    /**
     * Encrypts a String with the caesar cipher by shifting the chars by a specified amount.
     * Only letters from 'a' to 'z' and 'A' to 'Z' will be encrypted. All other chars should stay the same.
     * Positive numbers indicate a right shift.
     * Negative numbers indicate a left shift.
     *
     * @param plainText The plain text
     * @param shift The shift amount
     * @return The encrypted text
     */
    public String encrypt(String plainText, int shift) {

        StringBuilder result= new StringBuilder();
       if(shift<0){
            shift=(shift%26)+26;
        }
       for (char character : plainText.toCharArray()) {
           if ((character >= 'A' && character <= 'Z') || (character >= 'a' && character <= 'z')) {
               if (character >= 'a' && character <= 'z') {
                   int nowPosition = character - 'a';
                   int newPosition = (nowPosition + shift) % 26;
                   char newChar = (char) ('a' + newPosition);
                   result.append(newChar);
               } else if (character >= 'A' && character <= 'Z') {
                   int nowPosition = character - 'A';
                   int newPosition = (nowPosition + shift) % 26;
                   char newChar = (char) ('A' + newPosition);
                   result.append(newChar);
               }
           } else {
               result.append(character);
           }
       }

            return result.toString();

    }

    /**
     * Decrypts a String with the caesar cipher by shifting the chars by a specified amount.
     * Only letters from 'a' to 'z' and 'A' to 'Z' will be decrypted. All other chars should stay the same.
     * Positive numbers indicate a right shift.
     * Negative numbers indicate a left shift.
     *
     * @param encryptedText The encrypted text
     * @param shift The shift amount
     * @return The plain text
     */
    public String decrypt(String encryptedText, int shift) {

        return encrypt(encryptedText, ((26 - shift) % 26));

    }

    /**
     * Finds the caesar shift between a plain and encrypted text
     *
     * @param plainText The plain text
     * @param encryptedText The encrypted text
     * @return The shift in range -25 to 25
     */
    public int getShift(String plainText, String encryptedText) {
        for(int i = -25; i < 26; ++i) {
                if(encrypt(plainText, i).equals(encryptedText)) {
                    if(decrypt(encryptedText,i).equals(plainText))
                    return i;
                }
            }


        return 0;
    }
}

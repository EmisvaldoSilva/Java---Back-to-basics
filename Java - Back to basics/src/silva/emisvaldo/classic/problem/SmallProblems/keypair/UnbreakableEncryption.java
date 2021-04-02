package silva.emisvaldo.classic.problem.SmallProblems.keypair;

import java.util.Random;

public class UnbreakableEncryption {
	//Generate *length* random bytes
	private static byte[] randomKey(int length) {
		byte[] dummy = new byte[length];
		Random random = new Random();
		random.nextBytes(dummy);
		return dummy;
	}
	
	public static KeyPair encrypt(String original) {
		byte[] originalBytes = original.getBytes();
		byte[] dummyKey = randomKey(originalBytes.length);
		byte[] encryptKey = new byte[originalBytes.length];
		
		for(int i = 0; i < originalBytes.length; i++) {
			//XOR every byte
			encryptKey[i] = (byte) (originalBytes[i] ^ dummyKey[i]);
		}
		return new KeyPair(dummyKey, encryptKey);
	}
	
	public static String decrypt(KeyPair kp) {
		byte[] decrypted = new byte[kp.key1.length];
		
		for(int i = 0; i < decrypted.length; i++) {
			//XOR every byte
			decrypted[i] = (byte) (kp.key1[i] ^ kp.key2[i]);
		}
		return new String(decrypted);
	}
	
	public static void main(String[] args) {
		KeyPair kp = encrypt("One Time Pad!");
		String result = decrypt(kp);
		
		System.out.println(result);
	}
	
}

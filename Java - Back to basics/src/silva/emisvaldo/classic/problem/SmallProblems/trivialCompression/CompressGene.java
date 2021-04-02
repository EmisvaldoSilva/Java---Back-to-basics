package silva.emisvaldo.classic.problem.SmallProblems.trivialCompression;

import java.util.BitSet;

public class CompressGene {
	
	private BitSet bitset;
	private int length;
	
	public CompressGene(String gene ) {
		compress(gene);
	}

	private void compress(String gene) {
		length = gene.length();
		
		// reservar capacidade suficiente para todos os bits
		bitset = new BitSet(length * 2);
		// convert to upper case for consistency
		final String upperGene = gene.toUpperCase();
		// convert String to bit representation
		for(int i = 0; i < length; i++) {
			final int firstLocation = 2 * i;
			final int secundLocation = 2 * i + 1;
			switch (upperGene.charAt(i)) {
			case 'A': // 00 are next two bits
				bitset.set(firstLocation, false);
				bitset.set(secundLocation, false);
				break;
			case 'C': // 01 are next two bits
				bitset.set(firstLocation, false);
				bitset.set(secundLocation, true);
				break;
			case 'G': // 10 are next two bits
				bitset.set(firstLocation, true);
				bitset.set(secundLocation, false);
				break;
			case 'T': // 11 are next two bits
				bitset.set(firstLocation, true);
				bitset.set(secundLocation, true);
				break;
			
			default:
				throw new IllegalArgumentException("The provided gene String contains characters other than ACGT");
			}
		}
		
	}
	
	public String decompress() {
		if(bitset == null) {
			return "";
		}
		// create a mutable place for characters with the right capacity
		StringBuilder builder = new StringBuilder(length);
		for(int i = 0; i < (length * 2); i+= 2) {
			final int firstBit = (bitset.get(i) ? 1 : 0);
			final int secundBit = (bitset.get(i + 1) ? 1: 0);
			final int lastbits = firstBit << 1 | secundBit;
			
			switch (lastbits) {
			case 0b00: // 00 is 'A'
				builder.append('A');
				break;
			case 0b01: // 01 is 'C'
				builder.append('C');
				break;
			case 0b10: // 10 is 'G'
				builder.append('G');
				break;
			case 0b11: // 11 is 'T'
				builder.append('T');
				break;
			}
		}
		return builder.toString();
	}
	
	public static void main(String[] args) {
		final String original = "TAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATATAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATA";
		CompressGene compressed = new CompressGene(original);
		final String decompressed = compressed.decompress();
		System.out.println(decompressed);
		System.out.println("Original is the same as decompressed: " + original.equalsIgnoreCase(decompressed));
	}

}

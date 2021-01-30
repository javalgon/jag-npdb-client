/*
 * This class file was automatically generated by ASN1bean (http://www.beanit.com)
 */

package com.beanit.asn1bean.compiler.pedefinitions;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import com.beanit.asn1bean.ber.*;
import com.beanit.asn1bean.ber.types.*;
import com.beanit.asn1bean.ber.types.string.*;


public class FileManagement implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class CHOICE implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		private byte[] code = null;
		public BerOctetString filePath = null;
		public Fcp createFCP = null;
		public UInt16 fillFileOffset = null;
		public BerOctetString fillFileContent = null;
		
		public CHOICE() {
		}

		public CHOICE(byte[] code) {
			this.code = code;
		}

		public CHOICE(BerOctetString filePath, Fcp createFCP, UInt16 fillFileOffset, BerOctetString fillFileContent) {
			this.filePath = filePath;
			this.createFCP = createFCP;
			this.fillFileOffset = fillFileOffset;
			this.fillFileContent = fillFileContent;
		}

		@Override public int encode(OutputStream reverseOS) throws IOException {

			if (code != null) {
				reverseOS.write(code);
				return code.length;
			}

			int codeLength = 0;
			if (fillFileContent != null) {
				codeLength += fillFileContent.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, PRIMITIVE, 1
				reverseOS.write(0x81);
				codeLength += 1;
				return codeLength;
			}
			
			if (fillFileOffset != null) {
				codeLength += fillFileOffset.encode(reverseOS, true);
				return codeLength;
			}
			
			if (createFCP != null) {
				codeLength += createFCP.encode(reverseOS, false);
				// write tag: APPLICATION_CLASS, CONSTRUCTED, 2
				reverseOS.write(0x62);
				codeLength += 1;
				return codeLength;
			}
			
			if (filePath != null) {
				codeLength += filePath.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, PRIMITIVE, 0
				reverseOS.write(0x80);
				codeLength += 1;
				return codeLength;
			}
			
			throw new IOException("Error encoding CHOICE: No element of CHOICE was selected.");
		}

		@Override public int decode(InputStream is) throws IOException {
			return decode(is, null);
		}

		public int decode(InputStream is, BerTag berTag) throws IOException {

			int tlvByteCount = 0;
			boolean tagWasPassed = (berTag != null);

			if (berTag == null) {
				berTag = new BerTag();
				tlvByteCount += berTag.decode(is);
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 0)) {
				filePath = new BerOctetString();
				tlvByteCount += filePath.decode(is, false);
				return tlvByteCount;
			}

			if (berTag.equals(BerTag.APPLICATION_CLASS, BerTag.CONSTRUCTED, 2)) {
				createFCP = new Fcp();
				tlvByteCount += createFCP.decode(is, false);
				return tlvByteCount;
			}

			if (berTag.equals(UInt16.tag)) {
				fillFileOffset = new UInt16();
				tlvByteCount += fillFileOffset.decode(is, false);
				return tlvByteCount;
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
				fillFileContent = new BerOctetString();
				tlvByteCount += fillFileContent.decode(is, false);
				return tlvByteCount;
			}

			if (tagWasPassed) {
				return 0;
			}

			throw new IOException("Error decoding CHOICE: Tag " + berTag + " matched to no item.");
		}

		public void encodeAndSave(int encodingSizeGuess) throws IOException {
			ReverseByteArrayOutputStream reverseOS = new ReverseByteArrayOutputStream(encodingSizeGuess);
			encode(reverseOS);
			code = reverseOS.getArray();
		}

		@Override public String toString() {
			StringBuilder sb = new StringBuilder();
			appendAsString(sb, 0);
			return sb.toString();
		}

		public void appendAsString(StringBuilder sb, int indentLevel) {

			if (filePath != null) {
				sb.append("filePath: ").append(filePath);
				return;
			}

			if (createFCP != null) {
				sb.append("createFCP: ");
				createFCP.appendAsString(sb, indentLevel + 1);
				return;
			}

			if (fillFileOffset != null) {
				sb.append("fillFileOffset: ").append(fillFileOffset);
				return;
			}

			if (fillFileContent != null) {
				sb.append("fillFileContent: ").append(fillFileContent);
				return;
			}

			sb.append("<none>");
		}

	}

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);
	private byte[] code = null;
	public List<CHOICE> seqOf = null;

	public FileManagement() {
		seqOf = new ArrayList<CHOICE>();
	}

	public FileManagement(byte[] code) {
		this.code = code;
	}

	public FileManagement(List<CHOICE> seqOf) {
		this.seqOf = seqOf;
	}

	@Override public int encode(OutputStream reverseOS) throws IOException {
		return encode(reverseOS, true);
	}

	public int encode(OutputStream reverseOS, boolean withTag) throws IOException {

		if (code != null) {
			reverseOS.write(code);
			if (withTag) {
				return tag.encode(reverseOS) + code.length;
			}
			return code.length;
		}

		int codeLength = 0;
		for (int i = (seqOf.size() - 1); i >= 0; i--) {
			codeLength += seqOf.get(i).encode(reverseOS);
		}

		codeLength += BerLength.encodeLength(reverseOS, codeLength);

		if (withTag) {
			codeLength += tag.encode(reverseOS);
		}

		return codeLength;
	}

	@Override public int decode(InputStream is) throws IOException {
		return decode(is, true);
	}

	public int decode(InputStream is, boolean withTag) throws IOException {
		int tlByteCount = 0;
		int vByteCount = 0;
		int numDecodedBytes;
		BerTag berTag = new BerTag();
		if (withTag) {
			tlByteCount += tag.decodeAndCheck(is);
		}

		BerLength length = new BerLength();
		tlByteCount += length.decode(is);
		int lengthVal = length.val;

		while (vByteCount < lengthVal || lengthVal < 0) {
			vByteCount += berTag.decode(is);

			if (lengthVal < 0 && berTag.equals(0, 0, 0)) {
				vByteCount += BerLength.readEocByte(is);
				break;
			}

			CHOICE element = new CHOICE();
			numDecodedBytes = element.decode(is, berTag);
			if (numDecodedBytes == 0) {
				throw new IOException("Tag did not match");
			}
			vByteCount += numDecodedBytes;
			seqOf.add(element);
		}
		if (lengthVal >= 0 && vByteCount != lengthVal) {
			throw new IOException("Decoded SequenceOf or SetOf has wrong length. Expected " + lengthVal + " but has " + vByteCount);

		}
		return tlByteCount + vByteCount;
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		ReverseByteArrayOutputStream reverseOS = new ReverseByteArrayOutputStream(encodingSizeGuess);
		encode(reverseOS, false);
		code = reverseOS.getArray();
	}

	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		appendAsString(sb, 0);
		return sb.toString();
	}

	public void appendAsString(StringBuilder sb, int indentLevel) {

		sb.append("{\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (seqOf == null) {
			sb.append("null");
		}
		else {
			Iterator<CHOICE> it = seqOf.iterator();
			if (it.hasNext()) {
				it.next().appendAsString(sb, indentLevel + 1);
				while (it.hasNext()) {
					sb.append(",\n");
					for (int i = 0; i < indentLevel + 1; i++) {
						sb.append("\t");
					}
					it.next().appendAsString(sb, indentLevel + 1);
				}
			}
		}

		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}


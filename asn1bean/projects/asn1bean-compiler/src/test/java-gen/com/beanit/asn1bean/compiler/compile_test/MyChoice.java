/*
 * This class file was automatically generated by ASN1bean (http://www.beanit.com)
 */

package com.beanit.asn1bean.compiler.compile_test;

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


public class MyChoice implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] code = null;
	public MyChoice2 myChoice2 = null;
	public BerBoolean myboolean = null;
	
	public MyChoice() {
	}

	public MyChoice(byte[] code) {
		this.code = code;
	}

	public MyChoice(MyChoice2 myChoice2, BerBoolean myboolean) {
		this.myChoice2 = myChoice2;
		this.myboolean = myboolean;
	}

	@Override public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			reverseOS.write(code);
			return code.length;
		}

		int codeLength = 0;
		if (myboolean != null) {
			codeLength += myboolean.encode(reverseOS, true);
			return codeLength;
		}
		
		if (myChoice2 != null) {
			codeLength += myChoice2.encode(reverseOS);
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

		int numDecodedBytes;

		myChoice2 = new MyChoice2();
		numDecodedBytes = myChoice2.decode(is, berTag);
		if (numDecodedBytes != 0) {
			return tlvByteCount + numDecodedBytes;
		}
		else {
			myChoice2 = null;
		}

		if (berTag.equals(BerBoolean.tag)) {
			myboolean = new BerBoolean();
			tlvByteCount += myboolean.decode(is, false);
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

		if (myChoice2 != null) {
			sb.append("myChoice2: ");
			myChoice2.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (myboolean != null) {
			sb.append("myboolean: ").append(myboolean);
			return;
		}

		sb.append("<none>");
	}

}


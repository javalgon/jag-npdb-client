/*
 * This class file was automatically generated by ASN1bean (http://www.beanit.com)
 */

package com.beanit.asn1bean.compiler.tagging_test;

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


public class TaggedChoice implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] code = null;
	public static final BerTag tag = new BerTag(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 34);

	private BerInteger myInteger = null;
	private BerBoolean myBoolean = null;
	
	public TaggedChoice() {
	}

	public TaggedChoice(byte[] code) {
		this.code = code;
	}

	public void setMyInteger(BerInteger myInteger) {
		this.myInteger = myInteger;
	}

	public BerInteger getMyInteger() {
		return myInteger;
	}

	public void setMyBoolean(BerBoolean myBoolean) {
		this.myBoolean = myBoolean;
	}

	public BerBoolean getMyBoolean() {
		return myBoolean;
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
		int sublength;

		if (myBoolean != null) {
			codeLength += myBoolean.encode(reverseOS, true);
			codeLength += BerLength.encodeLength(reverseOS, codeLength);
			if (withTag) {
				codeLength += tag.encode(reverseOS);
			}
			return codeLength;
		}
		
		if (myInteger != null) {
			sublength = myInteger.encode(reverseOS, true);
			codeLength += sublength;
			codeLength += BerLength.encodeLength(reverseOS, sublength);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 4
			reverseOS.write(0xA4);
			codeLength += 1;
			codeLength += BerLength.encodeLength(reverseOS, codeLength);
			if (withTag) {
				codeLength += tag.encode(reverseOS);
			}
			return codeLength;
		}
		
		throw new IOException("Error encoding CHOICE: No element of CHOICE was selected.");
	}

	@Override public int decode(InputStream is) throws IOException {
		return decode(is, true);
	}

	public int decode(InputStream is, boolean withTag) throws IOException {
		int tlvByteCount = 0;
		BerTag berTag = new BerTag();

		if (withTag) {
			tlvByteCount += tag.decodeAndCheck(is);
		}

		BerLength explicitTagLength = new BerLength();
		tlvByteCount += explicitTagLength.decode(is);
		tlvByteCount += berTag.decode(is);

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 4)) {
			BerLength length = new BerLength();
			tlvByteCount += length.decode(is);
			myInteger = new BerInteger();
			tlvByteCount += myInteger.decode(is, true);
			tlvByteCount += length.readEocIfIndefinite(is);
			tlvByteCount += explicitTagLength.readEocIfIndefinite(is);
			return tlvByteCount;
		}

		if (berTag.equals(BerBoolean.tag)) {
			myBoolean = new BerBoolean();
			tlvByteCount += myBoolean.decode(is, false);
			tlvByteCount += explicitTagLength.readEocIfIndefinite(is);
			return tlvByteCount;
		}

		throw new IOException("Error decoding CHOICE: Tag " + berTag + " matched to no item.");
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

		if (myInteger != null) {
			sb.append("myInteger: ").append(myInteger);
			return;
		}

		if (myBoolean != null) {
			sb.append("myBoolean: ").append(myBoolean);
			return;
		}

		sb.append("<none>");
	}

}

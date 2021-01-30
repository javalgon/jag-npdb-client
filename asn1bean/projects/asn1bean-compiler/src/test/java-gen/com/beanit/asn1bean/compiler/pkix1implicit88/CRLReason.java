/*
 * This class file was automatically generated by ASN1bean (http://www.beanit.com)
 */

package com.beanit.asn1bean.compiler.pkix1implicit88;

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

import com.beanit.asn1bean.compiler.pkix1explicit88.Attribute;
import com.beanit.asn1bean.compiler.pkix1explicit88.CertificateSerialNumber;
import com.beanit.asn1bean.compiler.pkix1explicit88.DirectoryString;
import com.beanit.asn1bean.compiler.pkix1explicit88.Name;
import com.beanit.asn1bean.compiler.pkix1explicit88.ORAddress;
import com.beanit.asn1bean.compiler.pkix1explicit88.RelativeDistinguishedName;

public class CRLReason extends BerEnum {

	private static final long serialVersionUID = 1L;

	public CRLReason() {
	}

	public CRLReason(byte[] code) {
		super(code);
	}

	public CRLReason(BigInteger value) {
		super(value);
	}

	public CRLReason(long value) {
		super(value);
	}

}

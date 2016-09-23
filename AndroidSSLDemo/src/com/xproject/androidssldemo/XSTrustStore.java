package com.xproject.androidssldemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;

public class XSTrustStore {

	private final String trustStoreName = "truststore.bks";
	private final String trustStorePsw = "";
	private File trustStoreFile;
	
	public XSTrustStore(Context context) {
		// TODO Auto-generated constructor stub
		File dir = context.getDir("private", Context.MODE_PRIVATE);
		trustStoreFile = new File(dir + File.separator + trustStoreName);
	}

	public void addTrustStore(String path) throws FileNotFoundException {
		try {
			// Load certificate
			java.io.FileInputStream fis = new java.io.FileInputStream(path);
			CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(fis);
			
			if (trustStoreFile.exists()) {
				// bks file exist
				try {
					java.io.FileInputStream fisKS = new java.io.FileInputStream(trustStoreFile);
					KeyStore appTs = KeyStore.getInstance("PKCS12");
					appTs.load(fisKS, trustStorePsw.toCharArray());
					fisKS.close();
					
					X509Certificate cert = (X509Certificate)cer;
					appTs.setCertificateEntry(cert.getSubjectDN().toString(), cer);
					
					java.io.FileOutputStream fos = new java.io.FileOutputStream(trustStoreFile);
					appTs.store(fos, trustStorePsw.toCharArray());
					fos.close();
				} catch (IOException e) {
	
				}
				
			} else {
				// bks file don't exist
				try {
					java.io.FileOutputStream fos = new java.io.FileOutputStream(trustStoreFile);
					KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
					keyStore.load(null, null);
					X509Certificate cert = (X509Certificate)cer;
					keyStore.setCertificateEntry(cert.getSubjectDN().toString(), cer);
					keyStore.store(fos, trustStorePsw.toCharArray());
					fos.close();
				} catch (IOException e) {
					
				}
			}
		} catch (KeyStoreException e) {
			return;
		} catch (CertificateException e) {
			return;
		} catch (NoSuchAlgorithmException e) {
			return;
		} catch (NoSuchProviderException e) {
			return;
		}
	}
	
	public KeyStore getTrustStore() {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			java.io.FileInputStream fis = new java.io.FileInputStream(trustStoreFile);
			ks.load(fis, trustStorePsw.toCharArray());
			fis.close();
			return ks;
		} catch (Exception e) {
		}
		
		return null;
	}

}

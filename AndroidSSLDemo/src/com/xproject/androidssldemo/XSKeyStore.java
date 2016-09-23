package com.xproject.androidssldemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import android.content.Context;

public class XSKeyStore {

		private final String keyStoreName = "keystore.bks";
		private final String keyStorePsw = "";
		private File keyStoreFile;
		

		public XSKeyStore(Context context) {
			// TODO Auto-generated constructor stub
			File dir = context.getDir("private", Context.MODE_PRIVATE);
			keyStoreFile = new File(dir + File.separator + keyStoreName);
		}
		
		public void addKeyStore(String path, String psw) throws FileNotFoundException, IOException {
			try {
				KeyStore ks = KeyStore.getInstance("PKCS12");
				
				java.io.FileInputStream fis = new java.io.FileInputStream(path);
				ks.load(fis, psw.toCharArray());
				fis.close();

				java.io.FileOutputStream fos = new java.io.FileOutputStream(keyStoreFile);
				ks.store(fos, keyStorePsw.toCharArray());
				fos.close();
					
			} catch (NoSuchAlgorithmException e) {
				return;
			} catch (KeyStoreException e) {
				return;
			} catch (CertificateException e) {
				return;
			}

		}
		
		public KeyStore getKeyStore() {
			try {
				KeyStore ks = KeyStore.getInstance("PKCS12");
				java.io.FileInputStream fis = new java.io.FileInputStream(keyStoreFile);
				ks.load(fis, keyStorePsw.toCharArray());
				fis.close();
				return ks;
			} catch (Exception e) {
			}
			return null;
		}
}

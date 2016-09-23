package com.xproject.androidssldemo;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import android.os.Build;

public class XSSSLSocketFactory extends SSLSocketFactory{
	
	private SSLContext sslContext = SSLContext.getInstance("TLS");
	
	public XSSSLSocketFactory(KeyStore truststore, Context context)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		// TODO Auto-generated constructor stub
		super(truststore);
		
		XSTrustManager tm = new XSTrustManager(context);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// After Android4.0
			XSKeyManager km = new XSKeyManager(context);
			
			sslContext.init(new KeyManager[] { km }, new TrustManager[] { tm }, new java.security.SecureRandom());
		} else {
			// Before Android4.0
			KeyManagerFactory keyManager = KeyManagerFactory.getInstance("X509");
			
			XSKeyStore ks = new XSKeyStore(context);
			KeyStore appKS = ks.getKeyStore();
			keyManager.init(appKS, null);
			
			sslContext.init(keyManager.getKeyManagers(), new TrustManager[] { tm }, new java.security.SecureRandom());
		}
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}


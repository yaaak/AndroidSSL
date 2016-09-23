package com.xproject.androidssldemo;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.os.Build;

public class XSTrustManager implements X509TrustManager {

	private Context context;
	
	public XSTrustManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		try {
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
			
			KeyStore ks = null;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				// After Android4.0
				
				// Get system's CA certificate store
				ks = KeyStore.getInstance("AndroidCAStore");
				ks.load(null, null);
			} else {
				// Before Android4.0
				
				// Get user input's CA certificate store
				XSTrustStore ts = new XSTrustStore(context);
				ks = ts.getTrustStore();
			}
			
			tmf.init(ks);
			
			X509TrustManager tm = null;
			for (TrustManager t : tmf.getTrustManagers()) {
				tm = (X509TrustManager) t;
			}
			
			// Check certificate chain from server 
			tm.checkServerTrusted(chain, authType);
		} catch (CertificateException e) {
			
			// 如果发生CertificateException后，可以根据具体业务来设置bHas的值
			// true的场合，服务器证书验证通过，false的场合，服务器的证书失败。
			boolean bHas = false;
			
			if (!bHas) {
				// Don't find record from database, then throw exception
				throw(e);
			}
		} catch (Exception e) {
			
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}


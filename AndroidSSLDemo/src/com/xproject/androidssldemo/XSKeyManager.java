package com.xproject.androidssldemo;



import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyChain;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class XSKeyManager implements X509KeyManager{

	Context context;
	public XSKeyManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public String chooseClientAlias(String[] keyType, Principal[] issuers,
			Socket socket) {
		// TODO Auto-generated method stub
		// 设置从UI选择客户端证明书的alias。
		String alias = "alias"; 
        return alias;
	}

	@Override
	public String chooseServerAlias(String keyType, Principal[] issuers,
			Socket socket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public X509Certificate[] getCertificateChain(String alias) {
		// TODO Auto-generated method stub
		try {
			// Get certificate chain based on alias
			return KeyChain.getCertificateChain(this.context, alias);
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public String[] getClientAliases(String keyType, Principal[] issuers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getServerAliases(String keyType, Principal[] issuers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrivateKey getPrivateKey(String alias) {
		// TODO Auto-generated method stub
		try {
			// Get private key based on alias
			return KeyChain.getPrivateKey(this.context, alias);
		} catch (Exception e) {

		}
		return null;
	}

}

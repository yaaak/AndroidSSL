package com.xproject.androidssldemo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.androidssldemo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

	private Button btn1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

		btn1 = (Button) findViewById(R.id.button01);
		
		btn1.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					// After Android 4.0
					// 加载Root证明书
					XSTrustStore ts = new XSTrustStore(MainActivity.this);
					try {
						ts.addTrustStore("输入Root证明书的全路径");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// 加载客户端证明书
					XSKeyStore ks = new XSKeyStore(MainActivity.this);
					try {
						ks.addKeyStore("输入客户端证明书的全路径", "客户端证明书的密码");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Before Android 4.0
					KeyChain.choosePrivateKeyAlias(MainActivity.this, new KeyChainAliasCallback(){
						public void alias(String alias) {
							if (alias != null) {
								// 根据UI选择客户端证明书，并纪录选择的客户端证明书的alias
								
							}
						}
					}, null, null, null, -1, null);
				}
				
				XSSSLSocketFactory sf = null;
				
				try {
	        		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    			trustStore.load(null, null);
	    			sf = new XSSSLSocketFactory(trustStore, MainActivity.this);
	        	} catch (Exception e) {
	        		
	        	}
				
//				SchemeRegistry schreg = new SchemeRegistry();
//	            schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//	            schreg.register(new Scheme("https", sf, 443));
	            
	            
	            HttpClient mHttpClient = new DefaultHttpClient();
				mHttpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));
				
				// 输入请求的url
				String url = "输入请求的url";
				
				try {
					HttpGet request = new HttpGet();
					request.setURI(new URI(url));
					HttpResponse response = mHttpClient.execute(request);
					if (response.getStatusLine().getStatusCode() == 200) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						StringBuffer buffer = new StringBuffer();
						String line = null;
						while ((line = reader.readLine()) != null) {
							buffer.append(line);
						}
						String result = buffer.toString();
						displayMessage("Result", result);
						return;
					} else if (response.getStatusLine().getStatusCode() == 401) {
						// Response code为401或403时，客户端证明书验证失败
					} else if (response.getStatusLine().getStatusCode() == 403) {
						// Response code为401或403时，客户端证明书验证失败
					} else {
						request.abort();
					}
				} catch (javax.net.ssl.SSLHandshakeException e) {
					// 服务器证书验证失败异常
				} catch (Exception e) {
					
				}     
				
// 遍历KeyStore中的证明书的方法	
//				Enumeration<String> aliases1 = appKeyStore.aliases();
//		        
//		        while (aliases1.hasMoreElements()) {
//		            String alias = aliases1.nextElement();
//		            X509Certificate cert1 = (X509Certificate) appKeyStore.getCertificate(alias);
//		            Log.d("", "Subject DN: " + cert1.getSubjectDN().getName());
//		            Log.d("", "Issuer DN: " + cert1.getIssuerDN().getName());
//		            Log.i("", "Issuer DN: " + Log.i("", "Issuer DN: " + cert1.getIssuerDN().getName()));
//		            appKeyStore1.setCertificateEntry(cert1.getSubjectDN().toString(), cert1);
//		        }
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void displayMessage(String title, String content) {
		new AlertDialog.Builder(MainActivity.this).setTitle(title)
				.setNegativeButton("确定", null)
				.setIcon(android.R.drawable.ic_menu_agenda).setMessage(content)
				.show();
	}
		
}

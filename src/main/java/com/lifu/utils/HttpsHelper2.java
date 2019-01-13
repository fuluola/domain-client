package com.lifu.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;



public class HttpsHelper2 {


	HostnameVerifier hv = new HostnameVerifier(){

		@Override
		public boolean verify(String hostName, SSLSession session) {
			System.out.println("Warning: URL Host:"+hostName);
			return true;
		}};

	public static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException{
		
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new MITM();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null,trustAllCerts,null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
	
	
	static class MITM implements javax.net.ssl.TrustManager,javax.net.ssl.X509TrustManager{

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
	}
}

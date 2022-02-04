
package com.exflyer.oddiad.network.api;

import com.google.gson.GsonBuilder;
import com.exflyer.oddiad.AppConstants;
import com.exflyer.oddiad.util.GLog;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OddiAdApiManager {
    private static Retrofit retrofit;
    private static Retrofit retrofitDirectURL;
    private static OddiAdApiManager instance;


    private OddiAdApiManager() {
    }

    public static OddiAdApiManager getInstance() {
        if (instance == null) {
            instance = new OddiAdApiManager();
        }

        return instance;
    }

    public OddiAdApiService createOddiAdApiService() {
        return getRetrofit().create(OddiAdApiService.class);
    }



    public static void clearRetrofites() {
        retrofit = null;
        retrofitDirectURL = null;
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = buildRetrofit(true, false);
        }

        return retrofit;
    }

    private Retrofit getRetrofitDirectURL() {
        if (retrofitDirectURL == null) {
            retrofitDirectURL = buildRetrofit(true, true);
        }

        return retrofitDirectURL;
    }

    private Retrofit buildRetrofit(boolean debug, boolean directURL) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(AppConstants.API_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(AppConstants.API_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(AppConstants.API_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);


        if (GLog.isRetrofitLog && debug) {

            RetrofitLogInterceptor logging = new RetrofitLogInterceptor(new RetrofitLogInterceptor.Logger() {

                @Override
                public void log(String message) {
                    if (GLog.isRetrofitLog) {
                        try {
                            GLog.d("JSON -> " + GLog.getPretty(message));
                        } catch (Exception e) {
                            GLog.d(message);
                        }
                    }
                }
            });
            logging.setLevel(RetrofitLogInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(logging).build();
        }

        okHttpClientBuilder.addInterceptor(chain -> {
            Request origin = chain.request();
            Request.Builder requestBuilder = origin.newBuilder();

            //requestBuilder.header(AppConstants.HEADER_NAME_USER_AGENT, AppConstants.HEADER_VALUE_USER_AGENT);
            return chain.proceed(requestBuilder.build());
        });

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory());
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    GLog.d("(network) hostName -> " + hostname + ", VerifyHost -> " + AppConstants.VERIFY_HOST_LIST.toString() + ", session -> " + session);

                    try {
                        return AppConstants.VERIFY_HOST_LIST.contains(hostname);
                    } catch (Exception e) {
                        GLog.e("(network) exception -> " + e.toString());
                    }
                    GLog.d("(network) outside");
                    return false;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(AppConstants.LOAD_BASE_URL);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
        retrofitBuilder.client(okHttpClientBuilder.build());

        return retrofitBuilder.build();
    }
}

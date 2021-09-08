package xyz.hellothomas.jedi.core.utils;

import xyz.hellothomas.jedi.core.dto.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Thomas
 * @date 2021/1/4 22:46
 * @description
 * @version 1.0
 */
public class JediRemotingUtil {
    public static final String JEDI_ACCESS_TOKEN = "JEDI-ACCESS-TOKEN";
    private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }};
    private static Logger logger = LoggerFactory.getLogger(JediRemotingUtil.class);

    // trust-https start
    private static void trustAllHosts(HttpsURLConnection connection) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();

            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        connection.setHostnameVerifier((hostname, session) -> true);
    }
    // trust-https end

    /**
     * post
     *
     * @param url
     * @param accessToken
     * @param timeout
     * @param requestObj
     * @param returnTargetClassOfT
     * @return
     */
    public static ReturnT postBody(String url, String accessToken, int timeout, Object requestObj,
                                   Class returnTargetClassOfT) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            // connection
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();

            // trust-https
            boolean useHttps = url.startsWith("https");
            if (useHttps) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                trustAllHosts(https);
            }

            // connection setting
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(timeout * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

            if (accessToken != null && accessToken.trim().length() > 0) {
                connection.setRequestProperty(JEDI_ACCESS_TOKEN, accessToken);
            }

            // do connection
            connection.connect();

            // write requestBody
            if (requestObj != null) {
//                String requestBody = GsonTool.toJson(requestObj);
                String requestBody = JsonUtil.serialize(requestObj);
                logger.debug("requestBody : {}", requestBody);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            /*byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
            OutputStream outwritestream = connection.getOutputStream();
            outwritestream.write(requestBodyBytes);
            outwritestream.flush();
            outwritestream.close();*/

            // valid StatusCode
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "jedi-rpc remoting fail, StatusCode(" + statusCode + ") " +
                        "invalid. for url : " + url);
            }

            // result
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            String resultJson = result.toString();

            // parse returnT
            try {
//                ReturnT returnT = GsonTool.fromJson(resultJson, ReturnT.class, returnTargetClassOfT);
                ReturnT returnT = JsonUtil.deserializeReturnT(resultJson, returnTargetClassOfT);
                return returnT;
            } catch (Exception e) {
                logger.error("jedi-rpc remoting (url=" + url + ") response content invalid(" + resultJson + ").", e);
                return new ReturnT<String>(ReturnT.FAIL_CODE, "jedi-rpc remoting (url=" + url + ") response content " +
                        "invalid(" + resultJson + ").");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ReturnT<String>(ReturnT.FAIL_CODE, "jedi-rpc remoting error(" + e.getMessage() + "), for " +
                    "url : " + url);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
    }

}

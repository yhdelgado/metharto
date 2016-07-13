package cu.uci.gws.metharto.tools;

import cu.uci.gws.metharto.config.MethartoConfigManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class Conection {

    private PasswordAuthentication authenticator;
    private String domain;
    private String host;
    private int port;
    private String user;
    private String pass;

    public Conection() {
        Properties prop = null;
        try {
            prop = MethartoConfigManager.getInstance().loadConfigFile();
        } catch (IOException ex) {
            Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.domain = prop.getProperty("metharto.proxy.domain");
        this.host = prop.getProperty("metharto.proxy.host");
        this.port = Integer.parseInt(prop.getProperty("metharto.proxy.port"));
        this.user = prop.getProperty("metharto.proxy.user");
        this.pass = prop.getProperty("metharto.proxy.pass");
        this.authenticator = new PasswordAuthentication(this.user, this.pass.toCharArray());
    }

    public DefaultHttpClient BuildHttpClient() throws UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {

        SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                return true;
            }
        }, new AllowAllHostnameVerifier());
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager(registry);
        //ClientConnectionManager ccm=new SingleClientConnManager(registry);
        DefaultHttpClient httpclient = new DefaultHttpClient(ccm);
        ccm.setMaxTotal(999);
        ccm.setDefaultMaxPerRoute(10);

        return httpclient;
    }
    public DefaultHttpClient ConectionProxy(DefaultHttpClient httpclient) throws UnknownHostException {
        NTCredentials cred = new NTCredentials(authenticator.getUserName(), String.valueOf(authenticator.getPassword()), InetAddress.getLocalHost().getHostName(), domain);
        httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY, cred);
        HttpHost proxy = new HttpHost(host, port);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        return httpclient;
    }

    public PasswordAuthentication getAuthenticator() {
        return authenticator;
    }

    public String getDomain() {
        return domain;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setAuthenticator(PasswordAuthentication authenticator) {
        this.authenticator = authenticator;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

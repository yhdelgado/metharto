package cu.uci.gws.metharto.config;

import java.io.*;
import java.util.Properties;

public class MethartoConfigManager {

    private String path;

    private MethartoConfigManager(String configPath) {
        path = configPath;
    }

    public String getPath() {
        return path;
    }

    public static MethartoConfigManager getInstance(String path) {
        MethartoConfigManager cm;
        cm = new MethartoConfigManager(path);
        return cm;
    }

    public static MethartoConfigManager getInstance() {
        MethartoConfigManager cm;
        return cm = new MethartoConfigManager("config/metharto.conf");
    }

    public void generateDefaultConfigFile() throws IOException {
        Properties configOpts = new Properties();
        configOpts.setProperty("metharto.proxy.domain", "");
        configOpts.setProperty("metharto.proxy.host", "");
        configOpts.setProperty("metharto.proxy.port", "0");
        configOpts.setProperty("metharto.proxy.user", "");
        configOpts.setProperty("metharto.proxy.pass", "");
        configOpts.setProperty("metharto.database", "database/metharto.db");
        configOpts.setProperty("metharto.metadata.format", "oai_dc");
        configOpts.setProperty("metharto.stopwords", "stopwords.list");
        configOpts.store(new FileOutputStream(new File(path)), "Configuration Options");
    }

    public Properties loadConfigFile() throws IOException {
        Properties loadedProperties = new Properties();
        File file = new File(this.getPath());
        if (file.exists()) {
            loadedProperties.load(new FileInputStream(file));
        } else {
            generateDefaultConfigFile();
            loadedProperties.load(new FileInputStream(file));
        }
        return loadedProperties;
    }
}

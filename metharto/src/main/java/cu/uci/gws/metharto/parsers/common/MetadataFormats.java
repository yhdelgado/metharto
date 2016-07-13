package cu.uci.gws.metharto.parsers.common;

public class MetadataFormats {

    private String metadataNamespace;
    private String metadataPrefix;
    private String schema;

    public MetadataFormats() {
    }

    public String getMetadataNamespace() {
        return metadataNamespace;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public String getSchema() {
        return schema;
    }

    public void setMetadataNamespace(String metadataNamespace) {
        this.metadataNamespace = metadataNamespace;
    }

    public void setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

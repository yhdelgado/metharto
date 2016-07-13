package cu.uci.gws.metharto.parsers.common;

public class Identify {

    private String repositoryName;
    private String baseUrl;
    private String protocolVersion;
    private String adminEmail;
    private String granularity;
    private String earliestDate;
    private boolean deleteRecord;
    private String schema;
    private String repositoryIdentifier;
    private String delimiter;

    public Identify() {
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isDeleteRecord() {
        return deleteRecord;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getEarliestDate() {
        return earliestDate;
    }

    public String getGranularity() {
        return granularity;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getRepositoryIdentifier() {
        return repositoryIdentifier;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getSchema() {
        return schema;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDeleteRecord(boolean deleteRecord) {
        this.deleteRecord = deleteRecord;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setEarliestDate(String earliestDate) {
        this.earliestDate = earliestDate;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setRepositoryIdentifier(String repositoryIdentifier) {
        this.repositoryIdentifier = repositoryIdentifier;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

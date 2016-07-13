package cu.uci.gws.metharto.parsers.oaidc;

public class Header {

    private String identifier;
    private String datestamp;
    private String setSpec;

    public Header() {
    }

    public String getDatestamp() {
        return datestamp;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSetSpec() {
        return setSpec;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setSetSpec(String setSpec) {
        this.setSpec = setSpec;
    }
}

package cu.uci.gws.metharto.parsers.oaidc;

public class Record {

    private Header header;
    private Metadata metadata;

    public Record() {
    }

    public Record(Header header, Metadata metadata) {
        this.header = header;
        this.metadata = metadata;
    }

    public Header getHeader() {
        return header;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}

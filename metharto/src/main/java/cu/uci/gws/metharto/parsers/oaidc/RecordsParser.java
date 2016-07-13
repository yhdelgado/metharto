package cu.uci.gws.metharto.parsers.oaidc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;

public class RecordsParser {

    private List<Record> listRecords;

    public RecordsParser() {
        listRecords = new ArrayList<Record>();
    }

    public List<Record> getRecords() {
        return listRecords;
    }

    public void setRecords(List<Record> records) {
        this.listRecords = records;
    }

    /**
     * @autor Yusniel Hidalgo Delgado
     *
     * @param OMElement object that represent whole XML file
     * @return void
     * @Description This method insert Record objects in the list records
     */
    public void parse(OMElement xmlTree) {
        Header h = null;
        Metadata m = null;
        Iterator<OMElement> getListRecords = xmlTree.getChildrenWithName(new QName("http://www.openarchives.org/OAI/2.0/", "ListRecords"));
        while (getListRecords.hasNext()) {
            OMElement elem2 = getListRecords.next();
            Iterator<OMElement> records = elem2.getChildElements();
            while (records.hasNext()) {
                OMElement elem3 = records.next();
                if (elem3.getLocalName().equals("record")) {
                    Iterator<OMElement> getRecordContent = elem3.getChildElements();
                    while (getRecordContent.hasNext()) {
                        OMElement recordElement = getRecordContent.next();
                        if (recordElement.getLocalName().equals("header")) {
                            h = parseHeader(recordElement);
                        }
                        if (recordElement.getLocalName().equals("metadata")) {
                            m = parseMetadata(recordElement);
                        }
                    }
                }
                Record r = new Record(h, m);
                listRecords.add(r);
            }
        }
    }

    private Header parseHeader(OMElement xmlHeader) {
        Header h = new Header();
        Iterator<OMElement> childs = xmlHeader.getChildElements();
        while (childs.hasNext()) {
            OMElement elem = childs.next();
            if (elem.getLocalName().equals("identifier")) {
                h.setIdentifier(elem.getText());
            } else if (elem.getLocalName().equals("datestamp")) {
                h.setDatestamp(elem.getText());
            } else if (elem.getLocalName().equals("setSpec")) {
                h.setSetSpec(elem.getText());
            }
        }
        return h;
    }

    private Metadata parseMetadata(OMElement xmlMetadata) {
        Metadata m = new Metadata();
        Iterator<OMElement> childs = xmlMetadata.getChildElements();
        while (childs.hasNext()) {
            OMElement elem = childs.next();
            Iterator<OMElement> nodes = elem.getChildElements();
            while (nodes.hasNext()) {
                OMElement elem2 = nodes.next();
                if (elem2.getLocalName().equals("title")) {
                    m.getTitles().add(elem2.getText());
                } else if (elem2.getLocalName().equals("creator")) {
                    m.getCreators().add(elem2.getText());
                } else if (elem2.getLocalName().equals("description")) {
                    m.getDescriptions().add(elem2.getText());
                } else if (elem2.getLocalName().equals("identifier")) {
                    m.getIdentifiers().add(elem2.getText());
                } else if (elem2.getLocalName().equals("source")) {
                    m.getSources().add(elem2.getText());
                } else if (elem2.getLocalName().equals("date")) {
                    m.getDates().add(elem2.getText());
                } else if (elem2.getLocalName().equals("subject")) {
                    m.getSubjects().add(elem2.getText());
                } else if (elem2.getLocalName().equals("publisher")) {
                    m.getPublishers().add(elem2.getText());
                } else if (elem2.getLocalName().equals("contributor")) {
                    m.getContributors().add(elem2.getText());
                } else if (elem2.getLocalName().equals("type")) {
                    m.getTypes().add(elem2.getText());
                } else if (elem2.getLocalName().equals("format")) {
                    m.getFormats().add(elem2.getText());
                } else if (elem2.getLocalName().equals("language")) {
                    m.getLanguages().add(elem2.getText());
                } else if (elem2.getLocalName().equals("relation")) {
                    m.getRelations().add(elem2.getText());
                } else if (elem2.getLocalName().equals("coverage")) {
                    m.getCoverages().add(elem2.getText());
                } else if (elem2.getLocalName().equals("rights")) {
                    m.getRights().add(elem2.getText());
                }
            }
        }
        return m;
    }
}

package cu.uci.gws.metharto.parsers.common;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;

public class IdentifyParser {

    private Identify identify;

    public IdentifyParser() {
        identify = new Identify();
    }

    public Identify getIdentify() {
        return identify;
    }

    public void parse(OMElement xmltree) throws XMLStreamException {
        Iterator<OMElement> it = xmltree.getChildrenWithName(new QName("http://www.openarchives.org/OAI/2.0/", "Identify"));
        it.hasNext();
        Iterator<OMElement> identifyElements = it.next().getChildElements();
        while (identifyElements.hasNext()) {
            OMElement eleme = identifyElements.next();
            switch (eleme.getLocalName()) {
                case "repositoryName":
                    identify.setRepositoryName(eleme.getText());
                    break;
                case "protocolVersion":
                    identify.setProtocolVersion(eleme.getText());
                    break;
                case "granularity":
                    identify.setGranularity(eleme.getText());
                    break;
                case "adminEmail":
                    identify.setAdminEmail(eleme.getText());
                    break;
                case "baseURL":
                    identify.setBaseUrl(eleme.getText());
                    break;
                case "earliestDatestamp":
                    identify.setEarliestDate(eleme.getText());
                    break;
                case "deletedRecord":
                    if (eleme.getText().equals("yes")) {
                        identify.setDeleteRecord(true);
                    } else {
                        identify.setDeleteRecord(false);
                    }
                    break;
                case "description":
                    Iterator<OMElement> oai_identifier = eleme.getChildElements();
                    Iterator<OMElement> fields = oai_identifier.next().getChildElements();
                    while (fields.hasNext()) {
                        OMElement element = fields.next();
                        switch (element.getLocalName()) {
                            case "scheme":
                                identify.setSchema(element.getText());
                                break;
                            case "repositoryIdentifier":
                                identify.setRepositoryIdentifier(element.getText());
                                break;

                            case "delimiter":
                                identify.setDelimiter(element.getText());
                                break;
                        }
                    }
                    break;
            }
        }

    }
}

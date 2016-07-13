package cu.uci.gws.metharto.parsers.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;

public class MetadataFormatsParser {

    private List<MetadataFormats> listMetadataFormats;

    public MetadataFormatsParser() {
        listMetadataFormats = new ArrayList<MetadataFormats>();
    }

    public List<MetadataFormats> getListMetadataFormats() {
        return listMetadataFormats;
    }

    public void parse(OMElement xmltree) {
        MetadataFormats f = null;
        Iterator<OMElement> formats = xmltree.getChildrenWithName(new QName("http://www.openarchives.org/OAI/2.0/", "ListMetadataFormats"));
        Iterator<OMElement> metadataFormats = formats.next().getChildElements();
        while (metadataFormats.hasNext()) {
            f = new MetadataFormats();
            Iterator<OMElement> mf = metadataFormats.next().getChildElements();
            while (mf.hasNext()) {
                OMElement child = mf.next();
                if (child.getLocalName().equals("metadataPrefix")) {
                    f.setMetadataPrefix(child.getText());
                } else if (child.getLocalName().equals("schema")) {
                    f.setSchema(child.getText());
                } else if (child.getLocalName().equals("metadataNamespace")) {
                    f.setMetadataNamespace(child.getText());
                }
            }
            listMetadataFormats.add(f);

        }

    }
}

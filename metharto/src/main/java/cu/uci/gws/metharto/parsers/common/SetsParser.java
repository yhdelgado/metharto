package cu.uci.gws.metharto.parsers.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;

public class SetsParser {

    private List<Set> listSets;

    public SetsParser() {
        listSets = new ArrayList<Set>();
    }

    public List<Set> getListSets() {
        return listSets;
    }

    public void parse(OMElement xmltree) {
        Set s = null;
        Iterator<OMElement> set = xmltree.getChildrenWithName(new QName("http://www.openarchives.org/OAI/2.0/", "ListSets"));
        Iterator<OMElement> lsets = set.next().getChildElements();
        while (lsets.hasNext()) {
            s = new Set();
            Iterator<OMElement> mf = lsets.next().getChildElements();
            while (mf.hasNext()) {
                OMElement child = mf.next();
                if (child.getLocalName().equals("setName")) {
                    s.setSetName(child.getText());
                } else if (child.getLocalName().equals("setSpec")) {
                    s.setSetSpec(child.getText());
                }
            }
            listSets.add(s);
        }
    }
}

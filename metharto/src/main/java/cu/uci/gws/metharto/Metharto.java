package cu.uci.gws.metharto;

import cu.uci.gws.metharto.harvester.MHarvester;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.cli.ParseException;
import org.apache.http.client.ClientProtocolException;

public class Metharto {

    org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger("example");

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.stream.XMLStreamException
     * @throws org.apache.http.client.ClientProtocolException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, XMLStreamException, ClientProtocolException, ParseException {
        List urls = new ArrayList();
        try {
            try {
                System.out.println(new Date());
                String line = "";
                FileReader urlfile = new FileReader("config/dataproviders.list");
                BufferedReader filereader = new BufferedReader(urlfile);
                while ((line = filereader.readLine()) != null) {
                    urls.add(line);
                }
                filereader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (urls.size() > 0) {
                int size = urls.size();
                for (int i = 0; i < size; i++) {
                    MHarvester mh = MHarvester.getMHarvester();
                    String url = urls.get(i).toString();
                    mh.setBaseURL(url);
                    Logger
                            .getLogger(Metharto.class
                            .getName()).log(Level.INFO,
                            "Processing URL: {0}.", url);
                    mh.parseAllData();
                    mh.SaveAllInDB();
                    mh = null;
                    System.out.println(
                            new Date());
                }
            } else {
                Logger.getLogger(Metharto.class
                        .getName()).log(Level.INFO,
                        "Config file is empty");
            }
        } catch (Exception ex) {
            Logger.getLogger(Metharto.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}

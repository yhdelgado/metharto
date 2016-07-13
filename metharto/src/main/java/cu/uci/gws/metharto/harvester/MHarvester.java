package cu.uci.gws.metharto.harvester;

import cu.uci.gws.cleaner.MetadataCleaner;
import cu.uci.gws.metharto.database.SQLiteConnection;
import cu.uci.gws.metharto.parsers.common.Identify;
import cu.uci.gws.metharto.parsers.common.IdentifyParser;
import cu.uci.gws.metharto.parsers.common.MetadataFormats;
import cu.uci.gws.metharto.parsers.common.MetadataFormatsParser;
import cu.uci.gws.metharto.parsers.common.Set;
import cu.uci.gws.metharto.parsers.common.SetsParser;
import cu.uci.gws.metharto.parsers.oaidc.Record;
import cu.uci.gws.metharto.parsers.oaidc.RecordsParser;
import cu.uci.gws.metharto.tools.Conection;

import java.io.*;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MHarvester {

    private String url;
    private List<File> files;
    private String resumptionToken;
    private String baseURL;
    private Identify identify;
    private List<Set> listSets;
    private List<MetadataFormats> listMetadataFormats;
    private List<Record> listRecords;
    private Conection conection;

    private static MHarvester mh = new MHarvester();

    private MHarvester() {
        this.resumptionToken = "";
        listSets = new ArrayList<>();
        listMetadataFormats = new ArrayList<>();
        listRecords = new ArrayList<>();
        conection = new Conection();
    }

    public MHarvester(File file) {
        this.files = new ArrayList<>();
        this.resumptionToken = "";
        listSets = new ArrayList<>();
        listMetadataFormats = new ArrayList<>();
        listRecords = new ArrayList<>();
        conection = new Conection();

    }

    private OMElement getReaderFromHttpGet(String url, boolean debugging) throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {

        DefaultHttpClient httpclient = conection.BuildHttpClient();
        if (!url.contains(conection.getDomain().toLowerCase())) {
            httpclient = conection.ConectionProxy(httpclient);
        }
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("User-Agent", "DBJHarverster");
        httpget.addHeader("From", "yhdelgado@uci.cu");
        HttpResponse response = null;

        response = httpclient.execute(httpget);
        StatusLine status = response.getStatusLine();
        System.out.println("HTTP Status: " + status.getStatusCode() + " for url: " + url);
        if (debugging) {
            System.out.println(response.getStatusLine());
            org.apache.http.Header[] header = response.getAllHeaders();
            for (int i = 0; i < header.length; i++) {
                System.out.println(header[i].getName() + " value: " + header[i].getValue());
            }
        }

        if (status.getStatusCode() == 503) {
            org.apache.http.Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Retry-After")) {
                    String retry_time = headers[i].getValue();
                    Thread.sleep(Integer.parseInt(retry_time) * 1000);
                    httpclient.getConnectionManager().shutdown();
                    httpclient = new DefaultHttpClient();
                    response = httpclient.execute(httpget);
                }
            }
        }

        HttpEntity entity = response.getEntity();
        InputStream instream = entity.getContent();
        BufferedInputStream bis = new BufferedInputStream(instream);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(bis);

        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement documentElement = builder.getDocumentElement();
        return documentElement;

    }

    public OMElement getRawIdentify() throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        url = this.baseURL + "?verb=" + "Identify";
        OMElement rawIdentify = getReaderFromHttpGet(url, false);
        return rawIdentify;
    }

    public OMElement getRawMetadataFormats() throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        url = this.baseURL + "?verb=" + "ListMetadataFormats";
        OMElement rawFormats = getReaderFromHttpGet(url, false);
        return rawFormats;
    }

    public OMElement getRawListSets() throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        url = this.baseURL + "?verb=" + "ListSets";
        OMElement rawSets = getReaderFromHttpGet(url, false);
        return rawSets;
    }

    public void parseIdentify() throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        url = this.baseURL + "?verb=" + "Identify";
        IdentifyParser identifyParser = new IdentifyParser();
        identifyParser.parse(getReaderFromHttpGet(url, false));
        identify = identifyParser.getIdentify();
    }

    public void parseListMetadataFormats() throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        url = this.baseURL + "?verb=" + "ListMetadataFormats";
        MetadataFormatsParser formatsParser = new MetadataFormatsParser();
        formatsParser.parse(getReaderFromHttpGet(url, false));
        listMetadataFormats = formatsParser.getListMetadataFormats();
    }

    public void parseListSets() throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, UnknownHostException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        url = this.baseURL + "?verb=" + "ListSets";
        SetsParser setsParser = new SetsParser();
        setsParser.parse(getReaderFromHttpGet(url, false));
        listSets = setsParser.getListSets();
    }

    public void parseListRecords(String file) throws FileNotFoundException, XMLStreamException {
        InputStream in = new FileInputStream(new File(file));
        BufferedInputStream bis = new BufferedInputStream(in);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(bis);
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement documentElement = builder.getDocumentElement();
        RecordsParser dcParser = new RecordsParser();
        dcParser.parse(documentElement);
        listRecords = dcParser.getRecords();
    }

    public void parseAllData() throws FileNotFoundException, IOException, Exception {
        Random random = new Random();
        String fileName = "file" + random.nextInt(9999);
        File f = getAllRawData(fileName);
        parseFromFile(f);
    }

    public void parseFromFile(File f) throws FileNotFoundException, XMLStreamException, ClientProtocolException, IOException, InterruptedException {
        InputStream in = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(in);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(bis);
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement documentElement = builder.getDocumentElement();
        Iterator hijos = documentElement.getChildElements();
        IdentifyParser identifyParser = new IdentifyParser();
        MetadataFormatsParser mfp = new MetadataFormatsParser();
        RecordsParser rp = new RecordsParser();
        SetsParser sp = new SetsParser();
        while (hijos.hasNext()) {
            OMElement id = (OMElement) hijos.next();
            Iterator req = id.getChildrenWithName(new QName("request"));
            while (req.hasNext()) {
                Object o = req.next();
                if (o.toString().contains("Identify")) {
                    identifyParser.parse(id);
                    break;
                }
                if (o.toString().contains("ListMetadataFormats")) {
                    mfp.parse(id);
                    break;
                }
                if (o.toString().contains("ListSets")) {
                    sp.parse(id);
                    break;
                }
                if (o.toString().contains("ListRecords")) {
                    rp.parse(id);
                    break;
                }
            }
        }
        identify = identifyParser.getIdentify();
        listMetadataFormats = mfp.getListMetadataFormats();
        listSets = sp.getListSets();
        listRecords = rp.getRecords();
        f.delete();
    }

    public OMElement requestListRecords(String resumptionToken, String from, String until, String set) throws ClientProtocolException, IOException, XMLStreamException, InterruptedException, Exception {
        OMElement documentElement = null;
        if (resumptionToken.isEmpty()) {
            if (from == null && until == null && set == null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc";
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from != null && until == null && set == null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&from=" + from;
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from == null && until != null && set == null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&until=" + from;
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from == null && until == null && set != null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&set=" + set;
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from != null && until != null && set == null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&from=" + from + "&until=" + until;
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from != null && until != null && set != null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&from=" + from + "&until=" + until + "&set=" + set;
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from == null && until != null && set != null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&until=" + until + "&set=" + set;
                documentElement = getReaderFromHttpGet(url, false);
            } else if (from != null && until == null && set != null) {
                url = this.baseURL + "?verb=" + "ListRecords&metadataPrefix=oai_dc&from=" + from + "&set=" + set;
                documentElement = getReaderFromHttpGet(url, false);
            }
        } else {
            url = this.baseURL + "?verb=" + "ListRecords&resumptionToken=" + resumptionToken;
            documentElement = getReaderFromHttpGet(url, false);
        }
        Iterator<OMElement> getRecordError = documentElement.getChildrenWithName(new QName("http://www.openarchives.org/OAI/2.0/", "error"));
        if (getRecordError.hasNext()) {
            sendException(getRecordError.next());
            return null;
        }
        return documentElement;
    }

    public void harvestRecords(String from, String until, String set, FileOutputStream fos) throws Exception {
        OMElement xml = requestListRecords(this.resumptionToken, from, until, set);
        String token = getResumptionToken(xml);
        xml.serializeAndConsume(fos);
        System.out.println("Token: " + token);
        if (!token.equals("")) {
            harvestRecords(from, until, set, fos);
        }

    }

    public void saveAllRecords() throws FileNotFoundException, IOException, Exception {
        try (FileOutputStream fos = new FileOutputStream(new File("temp/item1.xml"), true)) {
            fos.write("<Harverster>".getBytes("UTF-8"));
            harvestRecords(null, null, null, fos);
            fos.write("</Harverster>".getBytes("UTF-8"));
        }
    }

    public File getAllRawData(String pathOutput) throws FileNotFoundException, IOException, Exception {
        File f = new File(pathOutput);
        try (FileOutputStream fos = new FileOutputStream(f, true)) {
            fos.write("<Harverster>".getBytes("UTF-8"));
            getRawIdentify().serializeAndConsume(fos);
            getRawMetadataFormats().serializeAndConsume(fos);
            getRawListSets().serializeAndConsume(fos);
            harvestRecords(null, null, null, fos);
            fos.write("</Harverster>".getBytes("UTF-8"));
        }
        return f;
    }

    private void sendException(OMElement element) throws Exception {
        String msg = null;
        if (!element.getText().isEmpty()) {
            msg = ": " + element.getText();
        }
        OMAttribute attr = element.getAttribute(new QName("code"));
        switch (attr.getAttributeValue()) {
            case "badArgument":
                throw new Exception("badArgument " + msg);
            case "cannotDisseminateFormat":
                throw new Exception("cannotDisseminateFormat " + msg);
            case "idDoesNotExist":
                throw new Exception("idDoesNotExist " + msg);
        }
    }

    public String getResumptionToken(OMElement documentElement) {
        Iterator<OMElement> getListRecords = documentElement.getChildrenWithName(new QName("http://www.openarchives.org/OAI/2.0/", "ListRecords"));
        Iterator<OMElement> getRecords = getListRecords.next().getChildElements();
        while (getRecords.hasNext()) {
            OMElement elementType = getRecords.next();
            if (elementType.getLocalName().equals("resumptionToken")) {
                this.resumptionToken = elementType.getText();
            }
        }
        return resumptionToken;
    }

    public static MHarvester getMHarvester() {
        return mh;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void addFile(String fileName) {
        File f = new File(fileName);
        files.add(f);
    }

    public Identify getIdentify() {
        return identify;
    }

    public List<MetadataFormats> getListMetadataFormats() {
        return listMetadataFormats;
    }

    public List<Record> getListRecords() {
        return listRecords;
    }

    public List<Set> getListSets() {
        return listSets;
    }

    public void SaveAllInDB() {
        try {
            SQLiteConnection sqlitecon = new SQLiteConnection();
            MetadataCleaner mc = new MetadataCleaner();
            File f = new File("database/metharto.db");
            if (!f.isFile()) {
                sqlitecon.createDatabase();
            }
            List<Record> records = getListRecords();
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i).getMetadata() != null) {
                    String title = records.get(i).getMetadata().getTitles().get(0);
                    List<String> authors = records.get(i).getMetadata().getCreators();
                    String author = mc.getAuthorName(authors.get(0));
                    for (int j = 1; j < authors.size(); j++) {
                        try {
                            author += ";" + mc.getAuthorName(authors.get(j));
                        } catch (IOException ex) {
                            Logger.getLogger(MHarvester.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String description = "";
                    if (records.get(i).getMetadata().getDescriptions().size() > 0) {
                        description = records.get(i).getMetadata().getDescriptions().get(0);
                    }
                    String source = "";
                    if (records.get(i).getMetadata().getSources().size() > 0) {
                        source = records.get(i).getMetadata().getSources().get(0);
                    }
                    String date = "";
                    if (records.get(i).getMetadata().getDates().size() > 0) {
                        date = records.get(i).getMetadata().getDates().get(0);
                    }
                    String identifier = "";
                    if (records.get(i).getMetadata().getIdentifiers().size() > 0) {
                        identifier = records.get(i).getMetadata().getIdentifiers().get(0);
                    }
                    String type = "";
                    if (records.get(i).getMetadata().getTypes().size() > 0) {
                        type = records.get(i).getMetadata().getTypes().get(0);
                    }
                    String format = "";
                    if (records.get(i).getMetadata().getFormats().size() > 0) {
                        format = records.get(i).getMetadata().getFormats().get(0);
                    }
                    String language = "";
                    if (records.get(i).getMetadata().getLanguages().size() > 0) {
                        language = records.get(i).getMetadata().getLanguages().get(0);
                    }

                    sqlitecon.insertRecord(title, author, description, source, date, identifier, type, format, language);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MHarvester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

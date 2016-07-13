package cu.uci.gws.cleaner;

import cu.uci.gws.metharto.config.MethartoConfigManager;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class MetadataCleaner {

    private List<String> tit;

    public MetadataCleaner() throws IOException {
        tit = new ArrayList<String>();
        getStopWords();
    }

    public String getAuthorName(String creator) throws IOException {

        String name = null;
        String[] dataAll;
        String[] dataName;
        if (creator.contains(";")) {
            dataAll = creator.split(";");
            if (dataAll[0].contains(",")) {
                dataName = dataAll[0].split(",");
                if (dataName.length > 1) {
                    name = dataName[1].trim() + " " + dataName[0].trim();
                } else {
                    name = dataName[0].trim();
                }
            } else {
                name = dataAll[0].trim();
            }
        } else {
            if (creator.contains(",")) {
                dataName = creator.split(",");
                name = dataName[1].trim() + " " + dataName[0].trim();
            } else {
                name = creator;
            }
        }
        name = clearTitulation(name, tit);
        if (name.contains("-")) {
            name = name.replaceAll("-", " ");
        }
        if (name.contains(":")) {
            name = name.replaceAll(":", " ");
        }
        if (name.contains("\'")) {
            name = name.replaceAll("\'", "");
        }
        return name.trim();
    }

    public String clearSource(String source) {
        String result = "";
        if (source.contains(":")) {
            result = source.split(":")[0];
        } else {
            result = source;
        }
        return result;
    }

    public String getAuthorAffiliation(String text) {
        String aff = null;
        if (text.contains(";")) {
            aff = text.split(";")[1];
        }        
        if (aff != null && aff.length() > 2) {
            aff = aff.substring(1);
        }
        return aff;
    }

    public String clearAccents(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\P{ASCII}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public List<String> getStopWords() throws IOException {
        Properties cm = MethartoConfigManager.getInstance().loadConfigFile();
        String sw = cm.getProperty("metharto.stopwords");
        DataInputStream dis = new DataInputStream(new FileInputStream("config/" + sw));
        String data = "";
        while ((data = dis.readLine()) != null) {
            tit.add(data);
        }
        return tit;
    }

    public String clearTitulation(String name, List<String> titulations) throws IOException {
        int count = 0;
        while (count < 2) {
            for (int i = 0; i < titulations.size(); i++) {
                String titulation = titulations.get(i);
                if (name.contains(titulation)) {
                    name = name.replace(titulation, "");
                }
            }
            count++;
        }
        return name;
    }

    public String camelCase(String string) {
        String result = "";
        for (int i = 0; i < string.length(); i++) {
            String next = string.substring(i, i + 1);
            if (i == 0) {
                result += next.toUpperCase();
            } else {
                result += next.toLowerCase();
            }
        }
        return result;
    }
}

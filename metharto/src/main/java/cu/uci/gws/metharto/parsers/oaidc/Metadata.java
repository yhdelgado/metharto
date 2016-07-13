package cu.uci.gws.metharto.parsers.oaidc;

import java.util.ArrayList;
import java.util.List;

public class Metadata {

    private List<String> titles;
    private List<String> creators;
    private List<String> descriptions;
    private List<String> dates;
    private List<String> identifiers;
    private List<String> sources;
    private List<String> subjects;
    private List<String> types;
    private List<String> formats;
    private List<String> languages;
    private List<String> relations;
    private List<String> publishers;
    private List<String> contributors;
    private List<String> coverages;
    private List<String> rights;

    public Metadata() {
        this.titles = new ArrayList<String>();
        this.creators = new ArrayList<String>();
        this.descriptions = new ArrayList<String>();
        this.dates = new ArrayList<String>();
        this.identifiers = new ArrayList<String>();
        this.sources = new ArrayList<String>();
        this.subjects = new ArrayList<String>();
        this.types = new ArrayList<String>();
        this.formats = new ArrayList<String>();
        this.languages = new ArrayList<String>();
        this.relations = new ArrayList<String>();
        this.publishers = new ArrayList<String>();
        this.contributors = new ArrayList<String>();
        this.coverages = new ArrayList<String>();
        this.rights = new ArrayList<String>();
    }

    public List<String> getCreators() {
        return creators;
    }

    public List<String> getDates() {
        return dates;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<String> getFormats() {
        return formats;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public List<String> getRelations() {
        return relations;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public List<String> getCoverages() {
        return coverages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getRights() {
        return rights;
    }

    public void setCreators(List<String> creators) {
        this.creators = creators;
    }

    public void setDates(List<String> date) {
        this.dates = date;
    }

    public void setDescriptions(List<String> description) {
        this.descriptions = description;
    }

    public void setFormats(List<String> format) {
        this.formats = format;
    }

    public void setIdentifiers(List<String> identifier) {
        this.identifiers = identifier;
    }

    public void setLanguages(List<String> language) {
        this.languages = language;
    }

    public void setPublishers(List<String> publisher) {
        this.publishers = publisher;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public void setSources(List<String> source) {
        this.sources = source;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public void setTitles(List<String> title) {
        this.titles = title;
    }

    public void setTypes(List<String> type) {
        this.types = type;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public void setCoverages(List<String> coverages) {
        this.coverages = coverages;
    }

    public void setRights(List<String> rights) {
        this.rights = rights;
    }
}

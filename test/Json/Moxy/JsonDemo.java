package Json.Moxy;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JsonDemo {

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(SearchResults.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty("eclipselink.media-type", "application/json");
        unmarshaller.setProperty("eclipselink.json.include-root", false);
        StreamSource source = new StreamSource("http://search.twitter.com/search.json?q=jaxb");
        JAXBElement<SearchResults> jaxbElement = unmarshaller.unmarshal(source, SearchResults.class);

        Result result = new Result();
        result.setCreatedAt(new Date());
        result.setFromUser("bdoughan");
        result.setText("You can now use EclipseLink JAXB (MOXy) with JSON :)");
        jaxbElement.getValue().getResults().add(result);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty("eclipselink.media-type", "application/json");
        marshaller.setProperty("eclipselink.json.include-root", false);
        marshaller.marshal(jaxbElement, System.out);
    }

    private static class Result {

        private Date createdAt;
        private String fromUser;
        private String text;

        @XmlElement(name = "created_at")
        @XmlJavaTypeAdapter(DateAdapter.class)
        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        @XmlElement(name = "from_user")
        public String getFromUser() {
            return fromUser;
        }

        public void setFromUser(String fromUser) {
            this.fromUser = fromUser;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private static class SearchResults {

        private String query;
        private float completedIn;
        List<Result> results;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        @XmlElement(name = "completed_in")
        public float getCompletedIn() {
            return completedIn;
        }

        public void setCompletedIn(float completedIn) {
            this.completedIn = completedIn;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }
    }

    private static class DateAdapter extends XmlAdapter<String, Date> {

        private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

        @Override
        public String marshal(Date date) throws Exception {
            return dateFormat.format(date);
        }

        @Override
        public Date unmarshal(String string) throws Exception {
            return dateFormat.parse(string);
        }
    }
}

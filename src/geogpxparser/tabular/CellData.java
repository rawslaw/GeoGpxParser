package geogpxparser.tabular;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that represents data in a table cell.
 */
public class CellData {
    /** The text that's displayed in the cell. **/
    private final String text;

    /** The URL to which the text should link to, if any. **/
    private final URL url;

    public CellData(String textParam) {
        this.text = textParam;
        this.url = null;
    }

    public CellData(String textParam, String urlParam) {
        this.text = textParam;
        URL tempUrl = null;
        if (urlParam != null) {
            try {
                tempUrl = new URL(urlParam);
            } catch (MalformedURLException ex) {
                Logger.getLogger(CellData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.url = tempUrl;
    }

    public String getText() {
        return text;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "[Text: '" + text + "', URL: '" + url + "']";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CellData)) {
            return false;
        }
        CellData other = (CellData) o;
        if ((other.getText() == null && text == null) || (other.getText() != null && other.getText().equals(text))) {
            if ((other.getUrl() == null && url == null) || (other.getUrl() != null && other.getUrl().equals(url))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.text);
        hash = 37 * hash + Objects.hashCode(this.url);
        return hash;
    }
}

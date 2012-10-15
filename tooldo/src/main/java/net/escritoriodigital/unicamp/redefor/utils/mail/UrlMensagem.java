package net.escritoriodigital.unicamp.redefor.utils.mail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Creates a email message with url data.
 */
public class UrlMensagem
{
    /** Url address and parameters */
    private String uri;

    /** Final email message */
    private StringBuffer message;

    /** Flag to indicates if the current parameter is the first */
    private boolean firstParameter;

    /**
     * Creates a new UrlMessage object.
     *
     * @param uri URL address
     */
    public UrlMensagem(String uri)
    {
        this.uri = uri;
        firstParameter = true;
    }

    /**
     * Adds a parameter in url.
     *
     * @param parameter Parameter to be added
     * @param value Value of parameter
     */
    public void addParameter(String parameter, String value)
    {
        if (firstParameter)
        {
            uri += "?";
            firstParameter = false;
        }
        else
        {
            uri += "&";
        }

        uri += parameter;
        uri += "=";
        uri += value;
    }

    /**
     * Gets the final email message.
     *
     * @return Final email message
     *
     * @throws MalformedURLException If there is some error in url
     *         address/parameters
     * @throws IOException If there is some error reading url message
     */
    public StringBuffer getMessage() throws MalformedURLException, IOException
    {
        URL url = new URL(uri);
        
        URLConnection connection = url.openConnection();
      
        Integer conTime = 10;
        Integer redTime = 10;
        
        connection.setConnectTimeout(conTime.intValue());
        connection.setReadTimeout(redTime.intValue());
        
        String param = "User-Agent";
        String value = "MSIE 6.0";
        connection.setRequestProperty(param, value);

        InputStream conn = (InputStream) connection.getContent();

        BufferedReader data = new BufferedReader(new InputStreamReader(conn));

        String line;

        StringBuffer buf = new StringBuffer();

        while ((line = data.readLine()) != null)
        {
            buf.append(line + "\n");
        }
        
        message = buf;
        data.close();
        conn.close();
        
        return message;
    }
    
    public static StringBuffer replaceByToken(StringBuffer buff,String value, String token) throws Exception {
		
		int index = buff.indexOf(token);
		buff.replace(index, index+token.length(), value);
		if(buff.indexOf(token)!=-1)
		{
			//loop replaceByToken again until there is no token at all.
			return replaceByToken(buff,value,token);
		}
		return buff;
	}
}

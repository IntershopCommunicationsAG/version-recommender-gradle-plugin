package com.intershop.tool.version.recommender.resolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class MavenVersionFetcher implements VersionResolver
{
    private static final int DEFAULT_MAX_VERSIONS = 200;
    private static final int DEFAULT_WAITING_TIME = 100; // millis

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private final long waitingTime;

    public MavenVersionFetcher()
    {
        this(DEFAULT_WAITING_TIME);
    }

    public MavenVersionFetcher(long waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    private static List<String> extractVersions(String jsonResult)
    {
        List<String> result = new ArrayList<>();
        JSONObject data = new JSONObject(jsonResult);
        data = data.getJSONObject("response");
        JSONArray arr = data.getJSONArray("docs");
        for (int i = 0; i < arr.length(); i++)
        {
            JSONObject doc = arr.getJSONObject(i);
            result.add(doc.getString("v"));
        }
        return result;
    }

    private String retrieveJson(String groupID, String artifactID, int maxNumber)
    {
        waitSomeTime();
        StringBuilder result = new StringBuilder(100);
        try
        {
            URI uri = createURI(groupID, artifactID, maxNumber);

            HttpGet getRequest = new HttpGet(uri.toString());
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200)
            {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            while((output = br.readLine()) != null)
            {
                result.append(output);
            }

            // httpClient.getConnectionManager().shutdown();
        }
        catch(IOException | URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    protected URI createURI(String groupID, String artifactID, int maxNumber)
                    throws URISyntaxException, UnsupportedEncodingException
    {
        return new URI("http://search.maven.org/solrsearch/select?q="
                        + URLEncoder.encode("g:\"" + groupID + "\"", "UTF-8") + "+AND+"
                        + URLEncoder.encode("a:\"" + artifactID + "\"", "UTF-8") + "&core=gav&rows=" + maxNumber
                        + "&wt=json");
    }

    /**
     * Returns the version result from the maven central repository.
     *
     * @param dep
     *            the dependency definition object
     * @return the version information string
     */
    @Override
    public List<String> getVersions(String group, String artifactID)
    {
        String json = retrieveJson(group, artifactID, DEFAULT_MAX_VERSIONS);
        return extractVersions(json);
    }

    /**
     * Do not kill the maven server by very fast requests :)
     */
    private void waitSomeTime()
    {
        try
        {
            Thread.sleep(waitingTime);
        }
        catch(InterruptedException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't execute task", e);
        }
    }

}

package com.sourcesense.confluence.cmis;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlo Sciolla &lt;c.sciolla@sourcesense.com&gt;
 */
public class TestSearchMacro extends AbstractBaseUnitTest
{
    Logger log = Logger.getLogger (TestSearchMacro.class);
    public void testSearchTemplate()
    {
        Session session = getSession(TEST_REPOSITORY_NAME);
        Folder rootFolder = session.getRootFolder();

        List<String> properties = new ArrayList<String>();
        properties.add("cmis:name");
        properties.add("cmis:objectTypeId");

        vc.put("cmisObjects", rootFolder.getChildren());
        vc.put("cmisProperties", properties);

        String result = null;
        try
        {
            result = renderTemplate("templates/cmis/search.vm");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }

        log.info("result:\n" + result);
    }
}
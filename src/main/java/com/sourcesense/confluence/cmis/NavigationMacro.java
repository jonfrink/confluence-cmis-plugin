package com.sourcesense.confluence.cmis;

import java.io.IOException;
import java.util.Map;

import org.apache.chemistry.Repository;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.velocity.VelocityContext;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;

public class NavigationMacro extends BaseCMISMacro {

    private static final String TEMPLATE_NAME = "/templates/cmis/cmis-navigation.vm";
    private static final String SERVLET_OUTPUT = "servletOutput";

    @Override
    protected String doExecute(Map<String, String> params, String body, RenderContext renderContext, Repository repository) {
        HttpClient client = new HttpClient();
        String servletUrl = params.get("s");
        String id = params.get("id");
        String servername = params.get("n");
        if (id == null) {
            id = repository.getConnection(null).getRootFolder().getId();
        }
        HttpMethod method = new GetMethod(servletUrl + "?id=" + id + "&servername=" + servername);
        try {
            client.executeMethod(method);
            VelocityContext context = new VelocityContext(MacroUtils.defaultVelocityContext());
            context.put(SERVLET_OUTPUT, new String(method.getResponseBody()));
            context.put("s", params.get("s"));
            context.put("id", params.get("id"));
            context.put("n", params.get("n"));
            return VelocityUtils.getRenderedTemplate(TEMPLATE_NAME, context);
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {

        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public RenderMode getBodyRenderMode() {
        return RenderMode.NO_RENDER;
    }

    public boolean hasBody() {
        return false;
    }

    public boolean isInline() {
        return false;
    }

}
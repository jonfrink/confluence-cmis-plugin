/*
 * Copyright 2010 Sourcesense <http://www.sourcesense.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sourcesense.confluence.cmis;

import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.sourcesense.confluence.cmis.utils.ConfluenceCMISRepository;
import com.sourcesense.confluence.cmis.utils.Utils;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;

import java.util.Map;

public class DoclinkMacro extends BaseCMISMacro {

  @Override
  protected String executeImpl(Map params, String body, RenderContext renderContext, ConfluenceCMISRepository confluenceCmisRepository) throws MacroException {
    Session session = confluenceCmisRepository.getSession();

    String documentId = (String) params.get(BaseCMISMacro.PARAM_ID);
    ObjectId objectId = session.createObjectId(documentId);
    boolean useProxy = (Boolean) params.get(BaseCMISMacro.PARAM_USEPROXY);

    Document document = (Document) session.getObject(objectId);

    if (document == null) {
      throw new MacroException("Cannot find any document with the following ID: " + documentId);
    }
      
    renderContext.addParam(VM_DOCUMENT_LINK, Utils.getLink(session, confluenceCmisRepository, documentId, useProxy));

    return VelocityUtils.getRenderedTemplate("templates/cmis/doclink.vm", renderContext.getParams());
  }
}
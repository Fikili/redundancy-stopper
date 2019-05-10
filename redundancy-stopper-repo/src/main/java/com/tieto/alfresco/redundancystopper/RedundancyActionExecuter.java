package com.tieto.alfresco.redundancystopper;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.alfresco.redundancystopper.model.RedundancyStopperModel;

public class RedundancyActionExecuter extends ActionExecuterAbstractBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedundancyActionExecuter.class);
	private NodeService nodeService;
	
	@Override
	protected void executeImpl(Action action, final NodeRef actionedUponNodeRef) {
		AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Void>() {

			@Override
			public Void doWork() throws Exception {
				LOGGER.debug("Let's set fingerprint prop for node: {}", nodeService.getProperty(actionedUponNodeRef, ContentModel.PROP_NAME));
				if (!nodeService.hasAspect(actionedUponNodeRef, RedundancyStopperModel.ASPECT_REDUNDANCY_IDENTIFICATOR)) {
					nodeService.addAspect(actionedUponNodeRef, RedundancyStopperModel.ASPECT_REDUNDANCY_IDENTIFICATOR, Collections.emptyMap());
				}
				nodeService.setProperty(actionedUponNodeRef, RedundancyStopperModel.PROP_FINGERPRINT, "Dummy value");
				return null;
			}
		}, AuthenticationUtil.getAdminUserName());
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {

	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
}

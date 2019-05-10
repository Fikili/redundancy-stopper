package com.tieto.alfresco.redundancystopper;

import java.lang.invoke.MethodHandles;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies.OnAddAspectPolicy;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ParameterCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.alfresco.redundancystopper.model.RedundancyStopperModel;

public class RedundancyIdentificatorPolicy implements OnAddAspectPolicy{
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private PolicyComponent policyComponent;
	private NodeService nodeService;
	private DictionaryService dictionaryService;
	
	public void init() {
		ParameterCheck.mandatory("policyComponent", policyComponent);
		ParameterCheck.mandatory("nodeService", nodeService);
		ParameterCheck.mandatory("dictionaryService", dictionaryService);
		final Behaviour onAddAspect = new JavaBehaviour(this, OnAddAspectPolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT);
		policyComponent.bindClassBehaviour(OnAddAspectPolicy.QNAME,RedundancyStopperModel.ASPECT_REDUNDANCY_IDENTIFICATOR, onAddAspect);
	}
	
	@Override
	public void onAddAspect(NodeRef nodeRef, QName aspectTypeQName) {
		/*
		if (nodeService.exists(nodeRef) && dictionaryService.isSubClass(nodeService.getType(nodeRef), ContentModel.TYPE_CONTENT)) {
			LOGGER.debug("onAddAspect for: {}", nodeService.getProperty(nodeRef, ContentModel.PROP_NAME));			
		}
		// FIXME: Use afterCommit instead
		nodeService.removeAspect(nodeRef, RedundancyStopperModel.ASPECT_REDUNDANCY_IDENTIFICATOR);
		*/
	}
	
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	
	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}
}

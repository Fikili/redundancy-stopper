package com.tieto.alfresco.redundancystopper;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tieto.alfresco.redundancystopper.model.RedundancyStopperModel;

public class RedundancyActionExecuter extends ActionExecuterAbstractBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	@Qualifier("nodeService")
	private NodeService nodeService;
	
	@Autowired
	@Qualifier("dictionaryService")
	private DictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("searchService")
	private SearchService searchService;
	
	@Override
	protected void executeImpl(Action action, final NodeRef actionedUponNodeRef) {
		AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Void>() {

			@Override
			public Void doWork() throws Exception {
				if (nodeService.exists(actionedUponNodeRef) && dictionaryService.isSubClass(nodeService.getType(actionedUponNodeRef), ContentModel.TYPE_CONTENT)) {
					LOGGER.debug("Let's set fingerprint prop for node: {}", nodeService.getProperty(actionedUponNodeRef, ContentModel.PROP_NAME));
					// Add aspect and select proper strategy for comparing
					addIdentificatorAspect(actionedUponNodeRef);
					// Find files with same PROP_FINGERPRINT value
					List <NodeRef> listOfSameFiles = findSameFiles(actionedUponNodeRef);
					createAssociations(actionedUponNodeRef, listOfSameFiles);
					addRedundantAspect();
					
				}
				return null;
			}

			private void addRedundantAspect() {
				// TODO Auto-generated method stub
				
			}

			private void createAssociations(NodeRef sourceNodeRef, List<NodeRef> listOfSameFiles) {
				LOGGER.debug("Let's create associations");
				for (NodeRef nodeRef : listOfSameFiles) {
					if (! nodeRef.equals(sourceNodeRef)) {
						nodeService.createAssociation(sourceNodeRef, nodeRef, RedundancyStopperModel.ASSOC_DUPLICATES);
					}
				}
			}

			private List<NodeRef> findSameFiles(NodeRef actionedUponNodeRef) {
				List <NodeRef> listOfSameFiles = new ArrayList<>();
				String fingerprint = (String) nodeService.getProperty(actionedUponNodeRef, RedundancyStopperModel.PROP_FINGERPRINT);
				String cmisQuery = "select * from rstop:identificator where rstop:fingerPrint=\'" + fingerprint +"\'";
				ResultSet rs = null;
				
				// Find all files with same value in PROP_FINGERPRINT
				try {
					rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_CMIS_ALFRESCO, cmisQuery);
					if (null != rs && rs.length() > 0) {
						listOfSameFiles = rs.getNodeRefs();
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
				LOGGER.debug("Used query : {} | with results {}", cmisQuery, listOfSameFiles.size() );
				return listOfSameFiles;
				
			}

			private void addIdentificatorAspect(final NodeRef actionedUponNodeRef) {
				final Map<QName, Serializable> aspectProperties = new HashMap<QName, Serializable>(1);
				// TODO: Rewrite to contentService
				String contentSize = String.valueOf(nodeService.getProperty(actionedUponNodeRef, ContentModel.PROP_CONTENT)).split("\\|")[2];
				LOGGER.debug("With contentSize = {}", contentSize);
				aspectProperties.put(RedundancyStopperModel.PROP_FINGERPRINT, contentSize);
				nodeService.addAspect(actionedUponNodeRef, RedundancyStopperModel.ASPECT_REDUNDANCY_IDENTIFICATOR, aspectProperties);
			}
		}, AuthenticationUtil.getAdminUserName());
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		//Not needed in this action
	}
}

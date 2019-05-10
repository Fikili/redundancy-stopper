package com.tieto.alfresco.redundancystopper.service;

import java.util.Collections;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tieto.alfresco.redundancystopper.model.RedundancyStopperModel;

@Component
public class RedundancyServiceImpl implements RedundancyService {

	@Autowired
	@Qualifier("contentService")
	private ContentService contentService;

	@Autowired
	@Qualifier("nodeService")
	private NodeService nodeService;

	@Autowired
	private FingerprintService fingerPringService;

	@Override
	public void calculateFingerPrint(final NodeRef node) {
		final ContentReader r = contentService.getReader(node, ContentModel.PROP_CONTENT);
		final long size = r.getContentData().getSize();
		final String hash = fingerPringService.getFingerPrint(r.getFileChannel());
		final String fingerPrint = size + ";" + hash;
		nodeService.addAspect(node, RedundancyStopperModel.ASPECT_REDUNDANCY_IDENTIFICATOR,
				Collections.singletonMap(RedundancyStopperModel.PROP_FINGERPRINT,fingerPrint ));
	}
}

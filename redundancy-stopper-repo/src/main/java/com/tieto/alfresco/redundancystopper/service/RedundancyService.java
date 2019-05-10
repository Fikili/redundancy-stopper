package com.tieto.alfresco.redundancystopper.service;

import org.alfresco.service.cmr.repository.NodeRef;

public interface RedundancyService {

	public void calculateFingerPrint(final NodeRef node);
}

package com.tieto.alfresco.redundancystopper.model;

import org.alfresco.service.namespace.QName;

public class RedundancyStopperModel {
	public static final String MODEL_DELIMITER = ":";
	public static final String TIETO_MODEL_1_0_URI = "http://www.tieto.com/model/content/1.0";
	public static final QName ASPECT_REDUNDANCY_IDENTIFICATOR =  QName.createQName(TIETO_MODEL_1_0_URI,"identificator");
	public static final QName PROP_FINGERPRINT =  QName.createQName(TIETO_MODEL_1_0_URI,"fingerPrint");
	public static final QName ASPECT_DUPLICATES =  QName.createQName(TIETO_MODEL_1_0_URI,"duplicates");
	public static final QName ASSOC_DUPLICATES =  QName.createQName(TIETO_MODEL_1_0_URI,"duplicateAssocs");
	
	
}

package com.tieto.alfresco.redundancystopper.service;

import java.nio.channels.FileChannel;

public interface FingerprintService {

	public String getFingerPrint(final FileChannel fileChannel);

}

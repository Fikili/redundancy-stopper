package com.tieto.alfresco.redundancystopper.service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MD5Fingerprint implements FingerprintService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String ALGHORITM = "MD5";
	private static final int BUFFER = 2048;

	@Override
	public String getFingerPrint(FileChannel nodeContent) {
		try {
			final MessageDigest md = MessageDigest.getInstance(ALGHORITM);
			ByteBuffer buff = ByteBuffer.allocate(BUFFER);
			while (nodeContent.read(buff) != -1) {
				buff.flip();
				md.update(buff);
				buff.clear();
			}
			return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
		} catch (NoSuchAlgorithmException | IOException e) {
			LOGGER.error("getFingerPrint || Error while calulating checksum:{}", e.getMessage());
		}
		return "";
	}
}
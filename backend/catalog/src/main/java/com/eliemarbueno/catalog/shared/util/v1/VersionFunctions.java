package com.eliemarbueno.catalog.shared.util.v1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.MDC;

import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiMessages;
import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiParams;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VersionFunctions {
	private String version;

	public VersionFunctions() {
		Properties properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(ConstantsApiParams.VERSION_FILE)) {
			if (input == null) {
				log.info("Unable to find version.properties", MDC.getCopyOfContextMap());
				return;
			}
			properties.load(input);
			version = properties.getProperty("app.version");
		} catch (IOException e) {
			log.error(LogFunctions.getErrorMessage(e), MDC.getCopyOfContextMap());
		} catch (Exception e) {
			log.error(LogFunctions.getErrorMessage(e), MDC.getCopyOfContextMap());
		}
	}

	public String getVersion() {
		return version;
	}

	@PostConstruct
	private void checkFileVersionExist(String pathFile) {
		File credentialsFile = new File(pathFile);
		log.debug(LogFunctions.getMethod() + "Check version file existing during start: " + pathFile,
				MDC.getCopyOfContextMap());
		if (!credentialsFile.exists() || !Files.isReadable(Paths.get(pathFile))) {
			var e = new IllegalStateException(
					ConstantsApiMessages.MSG_ERR_FILE_NOT_FOUND.replace("{0}", ConstantsApiParams.VERSION_FILE)
							+ pathFile);
			log.error(LogFunctions.getErrorMessage(e), MDC.getCopyOfContextMap());
			throw e;
		}
	}
}

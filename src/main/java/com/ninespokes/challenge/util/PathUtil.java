package com.ninespokes.challenge.util;

import com.ninespokes.challenge.settings.ResourceSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import static com.ninespokes.challenge.settings.ResourceSchema.*;

@Slf4j
public class PathUtil {

    /**
     * Initialise InputStream from path given.
     *
     * @param path  A valid path should either start with {@link ResourceSchema} or a valid file path in file system
     * @return      InputStream to read from the resource or null if invalid path given
     */
    public static InputStream getInputStreamFromPath(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }

        ResourceSchema prefix = Arrays.stream(values()).filter(p -> path.startsWith(p.getPrefix())).findAny().orElse(null);
        if (prefix == null) {
            return getInputStreamFromFile(path);
        }

        switch (prefix) {
            case FILE:
                return getInputStreamFromFile(path.substring(FILE.getPrefix().length()));
            case CLASSPATH:
                return getInputStreamFromClassPath(path.substring(CLASSPATH.getPrefix().length()));
            default:
                if (prefix.isSupported()) {
                    log.warn("Call for developers! URL schema {} should be supported", prefix.getPrefix());
                } else {
                    log.info("URL schema {} has not been supported yet", prefix.getPrefix());
                }
                return null;
        }
    }

    /**
     * Initialise InputStream from classpath with the resourcePath given.
     *
     * @param resourcePath  A valid resourcePath should not start with URL prefix
     * @return              InputStream to read from the resource or null if invalid resourcePath given
     */
    public static InputStream getInputStreamFromClassPath(String resourcePath) {
        if (StringUtils.isEmpty(resourcePath)) {
            return null;
        }

        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    }

    /**
     * Initialise InputStream from file system with the filePath given.
     *
     * @param filePath  A valid filePath should not start with URL prefix
     * @return          InputStream to read from the resource or null if invalid filePath given
     */
    public static InputStream getInputStreamFromFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("Failed to create input stream from file ({})", filePath);
        }
        return inputStream;
    }
}

package org.checkstyle;

import java.nio.file.Path;
import java.util.List;

import com.puppycrawl.tools.checkstyle.site.SiteUtil;

public class GenerationUtil {

    public static final String MAIN_FOLDER_PATH = Path.of(
            "src", "main", "java", "com", "puppycrawl", "tools", "checkstyle").toString();

    private static final String CHECKS = "checks";

    private static final String API = "api";

    private static final String NAMING = "naming";

    private static String absolutePathToCheckstyle;

    public static void setCheckstyleAbsolutePath(String path) {
        absolutePathToCheckstyle = path;
    }

    public static String getCheckstyleAbsolutePath() {
        return absolutePathToCheckstyle;
    }

    public static String getXdocPath() {
        return Path.of(absolutePathToCheckstyle, SiteUtil.PATH_TO_XDOC_TEMPLATES).toString();
    }

    public static List<Path> getSuperClassPaths() {
        return List.of(
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, CHECKS, NAMING,
                "AbstractAccessControlNameCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, CHECKS, NAMING,
                "AbstractNameCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, CHECKS, "javadoc",
                "AbstractJavadocCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, API,
                "AbstractFileSetCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, API,
                "AbstractCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, CHECKS, "header",
                "AbstractHeaderCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, CHECKS, "metrics",
                "AbstractClassCouplingCheck.java"),
            Path.of(GenerationUtil.getCheckstyleAbsolutePath(), MAIN_FOLDER_PATH, CHECKS, "whitespace",
                "AbstractParenPadCheck.java")
        );
    }
}

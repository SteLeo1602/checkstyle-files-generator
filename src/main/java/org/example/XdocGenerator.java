///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2025 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkFactory;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.util.ReaderFactory;

import com.puppycrawl.tools.checkstyle.site.SiteUtil;

/**
 * Generates xdoc content from xdoc templates.
 * This module will be removed once
 * <a href="https://github.com/checkstyle/checkstyle/issues/13426">#13426</a> is resolved.
 */
public final class XdocGenerator {
    private static final String MAIN_FOLDER_PATH = Path.of(
            "src", "main", "java", "com", "puppycrawl", "tools", "checkstyle").toString();

    private static final String CHECKS = "checks";

    private static final List<Path> MODULE_SUPER_CLASS_PATHS = List.of(
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, CHECKS, "naming",
            "AbstractAccessControlNameCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, CHECKS, "naming",
            "AbstractNameCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, CHECKS, "javadoc",
            "AbstractJavadocCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, "api",
            "AbstractFileSetCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, "api",
            "AbstractCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, CHECKS, "header",
            "AbstractHeaderCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, CHECKS, "metrics",
            "AbstractClassCouplingCheck.java"),
        Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, MAIN_FOLDER_PATH, CHECKS, "whitespace",
            "AbstractParenPadCheck.java")
    );

    public static final String XDOC_DIRECTORY_PATH =
        "src\\site\\xdoc";

    private static final String XDOCS_TEMPLATE_HINT = "xdocs-template";

    private XdocGenerator() {
    }

    public static void generateXdocContent(File temporaryFolder) throws Exception {
        final PlexusContainer plexus = new DefaultPlexusContainer();
        final Set<Path> templatesFilePaths = getXdocsTemplatesFilePaths();

        SiteUtil.processSuperclasses(MODULE_SUPER_CLASS_PATHS);

        for (Path path : templatesFilePaths) {
            final String pathToFile = path.toString();
            final File inputFile = new File(pathToFile);
            final File outputFile = new File(pathToFile.replace(".template", ""));
            final File tempFile = new File(temporaryFolder, outputFile.getName());
            tempFile.deleteOnExit();
            final XdocsTemplateSinkFactory sinkFactory = (XdocsTemplateSinkFactory)
                    plexus.lookup(SinkFactory.ROLE, XDOCS_TEMPLATE_HINT);
            final Sink sink = sinkFactory.createSink(tempFile.getParentFile(),
                    tempFile.getName(), String.valueOf(StandardCharsets.UTF_8));
            final XdocsTemplateParser parser = (XdocsTemplateParser)
                    plexus.lookup(Parser.ROLE, XDOCS_TEMPLATE_HINT);
            try (Reader reader = ReaderFactory.newReader(inputFile,
                    String.valueOf(StandardCharsets.UTF_8))) {
                parser.parse(reader, sink);
            }
            finally {
                sink.close();
            }
            final StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
            Files.copy(tempFile.toPath(), outputFile.toPath(), copyOption);
        }
    }

    /**
     * Gets xdocs template file paths. These are files ending with .xml.template.
     * This module will be removed once
     * <a href="https://github.com/checkstyle/checkstyle/issues/13426">#13426</a> is resolved.
     *
     * @return a set of xdocs template file paths.
     * @throws IOException if an I/O error occurs.
     */
    public static Set<Path> getXdocsTemplatesFilePaths() throws IOException {
        final Path directory = Path.of(Main.ABSOLUTE_PATH_TO_CHECKSTYLE, XDOC_DIRECTORY_PATH);
        try (Stream<Path> stream = Files.find(directory, Integer.MAX_VALUE,
                (path, attr) -> {
                    return attr.isRegularFile()
                            && path.toString().endsWith(".xml.template");
                })) {
            return stream.collect(Collectors.toUnmodifiableSet());
        }
    }
}

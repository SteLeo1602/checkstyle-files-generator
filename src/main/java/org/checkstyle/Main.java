package org.checkstyle;

import java.io.File;

import java.nio.file.Files;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "checkstyleFilesGenerator", mixinStandardHelpOptions = true,
         description = "Generates meta and xdoc files in checkstyle repository")
public class Main implements Callable<Integer> {

    @Parameters
    String pathToCheckstyle;

    @Option(names = { "-m", "--generateMeta" }, description = "Generate meta files")
    boolean generateMeta;

    @Option(names = { "-x", "--generateXdoc" }, description = "Generate xdoc files")
    boolean generateXdoc;

    @Override
    public Integer call() throws Exception {
        GenerationUtil.setCheckstyleAbsolutePath(pathToCheckstyle);

        if (generateMeta) {
            MetadataGeneratorUtil.generate(GenerationUtil.getCheckstyleAbsolutePath()
                    + "/src/main/java/com/puppycrawl/tools/checkstyle",
                "checks", "filters", "filefilters");
        }

        if (generateXdoc) {
            File temporaryFolder = Files.createTempDirectory(null).toFile();
            XdocGenerator.generateXdocContent(temporaryFolder);
        }

        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);

    }

}

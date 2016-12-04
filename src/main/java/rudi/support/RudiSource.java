package rudi.support;

import rudi.error.CannotReadSourceException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Object representing a RUDI source file. It provides functionality to
 * inspect a specific line of code. Note that all code lines are 1-based.
 */
public class RudiSource {

    // number of LOC that is before the first line in source as in the original source file
    private int globalLineOffset = 0;

    // List storing the source lines
    private ArrayList<String> source;

    /**
     * Construct a source object from file location
     *
     * @param sourceFileLocation
     */
    public RudiSource(String sourceFileLocation) {
        this.source = new ArrayList<>();
        this.globalLineOffset = 0;
        try {
            Files.lines(Paths.get(sourceFileLocation))
                    .collect(Collectors.toCollection(() -> this.source));
        } catch (Exception ex) {
            throw new CannotReadSourceException(sourceFileLocation, ex.getMessage());
        }
    }

    /**
     * Construct a source object from another source object. Useful for
     * dealing with subroutines.
     *
     * @param parentSource
     * @param startLine
     * @param endLine
     */
    public RudiSource(RudiSource parentSource, int startLine, int endLine) {
        this.source = new ArrayList<>();
        this.globalLineOffset = startLine - 1;
        for (int i = startLine; i <= endLine; i++) {
           this.source.add(parentSource.getLine(i));
        }
    }

    /**
     * Create a source object from a list of sources. Useful for debugging
     * and testing.
     *
     * @param sourceText
     */
    public RudiSource(List<String> sourceText) {
        this.source = new ArrayList<>();
        this.source.addAll(sourceText);
        this.globalLineOffset = 0;
    }

    public RudiSource clone() {
        return new RudiSource(this, 1, this.totalLines());
    }

    /**
     * Get the source line at line X.
     *
     * @param lineNumber
     * @return
     */
    public String getLine(int lineNumber) {
        return this.source.get(lineNumber-1);
    }

    /**
     * Update the source
     * @param lineNuber
     * @param newSource
     */
    public void updateLine(int lineNuber, String newSource) {
        this.source.set(lineNuber-1, newSource);
    }

    /**
     * Get total number of LOC
     * @return
     */
    public int totalLines() {
        return this.source.size();
    }

    public int getGlobalLineOffset() {
        return globalLineOffset;
    }

    public List<String> getParameterList() {
        Optional<String> declaration = this.source.stream()
                .filter(s -> RudiUtils.stripComments(s).trim().startsWith(RudiConstant.SUBROUTINE_COMMAND))
                .findFirst();
        if (!declaration.isPresent())
            return new ArrayList<>();
        else {
            String s = declaration.get();
            s = s.substring(s.indexOf(RudiConstant.LEFT_PAREN) + 1, s.indexOf(RudiConstant.RIGHT_PAREN));
            return Arrays.stream(s.split(RudiConstant.COMMA))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .collect(Collectors.toList());
        }
    }
}

package com.easyinsight.goals;

import com.easyinsight.solutions.Solution;

import java.util.List;

/**
 * User: James Boe
 * Date: Jan 9, 2009
 * Time: 12:44:04 PM
 */
public class AvailableSolutionList {
    private List<Solution> tagMatchedSolutions;
    private List<Solution> allSolutions;

    public AvailableSolutionList() {
    }

    public AvailableSolutionList(List<Solution> tagMatchedSolutions, List<Solution> allSolutions) {

        this.tagMatchedSolutions = tagMatchedSolutions;
        this.allSolutions = allSolutions;
    }

    public List<Solution> getTagMatchedSolutions() {
        return tagMatchedSolutions;
    }

    public void setTagMatchedSolutions(List<Solution> tagMatchedSolutions) {
        this.tagMatchedSolutions = tagMatchedSolutions;
    }

    public List<Solution> getAllSolutions() {
        return allSolutions;
    }

    public void setAllSolutions(List<Solution> allSolutions) {
        this.allSolutions = allSolutions;
    }
}

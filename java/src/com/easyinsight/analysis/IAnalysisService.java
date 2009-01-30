package com.easyinsight.analysis;

import java.util.Collection;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:32:30 PM
 */
public interface IAnalysisService {
    public Collection<WSAnalysisDefinition> getAnalysisDefinitions();
    public long saveAnalysisDefinition(WSAnalysisDefinition analysisDefinition);
    public void deleteAnalysisDefinition(WSAnalysisDefinition analysisDefinition);

    WSAnalysisDefinition openAnalysisDefinition(long analysisID);
}

package org.iplantc.de.client.models.pipelines;

import com.google.gwt.user.client.ui.HasName;
import org.iplantc.de.client.models.HasSystemId;

import java.util.List;

/**
 * An AutoBean interface for a Pipeline.
 * 
 * @author psarando
 *
 */
public interface Pipeline extends HasName, HasSystemId {

    public String getId();

    public void setId(String id);

    public String getDescription();

    public void setDescription(String description);

    public List<PipelineTask> getApps();

    public void setApps(List<PipelineTask> apps);
}

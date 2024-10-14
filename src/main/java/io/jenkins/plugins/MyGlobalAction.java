package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.RootAction;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class MyGlobalAction implements RootAction {

    private static final Logger LOGGER = Logger.getLogger(MyGlobalAction.class.getName());

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "My Global Action";
    }

    @Override
    public String getUrlName() {
        return "myglobalaction";
    }

    @GET
    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException {
        LOGGER.info("doDynamic called");
    }
}

package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.RootAction;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class QueueJobRedirectorGlobalAction implements RootAction {

    private static final Logger LOGGER = Logger.getLogger(QueueJobRedirectorGlobalAction.class.getName());

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "QueueJob Redirector Global Action";
    }

    @Override
    public String getUrlName() {
        return "from-queue";
    }

    @GET
    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException {
        LOGGER.info("doDynamic called");
        String queueId = req.getParameter("queueid");
        long id = 0;
        try {
            id = Long.parseLong(queueId);
        } catch (NumberFormatException e) {
            String errorMessage = String.format("QueueId %s isn't a number. Please check your inputs", queueId);
            LOGGER.finest(errorMessage);
            rsp.sendError(404, errorMessage);
            return;
        }

        if (BuildUtils.buildStarted(id)) {
            Optional<String> newUrl = BuildUtils.buildUrl(id);
            if (newUrl.isEmpty()) {
                String errorMessage = String.format("No build with a queue id %d was found", id);
                LOGGER.finest(errorMessage);
                rsp.sendError(404, errorMessage);
                return;
            }

            String message = String.format("Redirecting queuid id %d to %s", id, newUrl);
            LOGGER.finest(message);
            rsp.sendRedirect2(newUrl.get());
        } else {
            String message = String.format("Build %d has not started yet", id);
            LOGGER.finest(message);
            rsp.sendError(404, "Build not started yet " + queueId);
        }
    }
}

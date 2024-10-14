package io.jenkins.plugins;

import hudson.model.Action;
import hudson.model.Job;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class QueueJobRedirectAction implements Action {
    private static final Logger LOGGER = Logger.getLogger(QueueJobRedirectAction.class.getName());

    private final Job<?, ?> job;

    public QueueJobRedirectAction(Job<?, ?> job) {
        this.job = job;
    }

    @Override
    public String getIconFileName() {
        return null; // or provide an icon file name
    }

    @Override
    public String getDisplayName() {
        return "My Action";
    }

    @Override
    public String getUrlName() {
        return "from-queue";
    }

    @GET
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
        LOGGER.info("doIndex called");
        String queueId = req.getParameter("queueid");
        long id = 0;
        try {
            id = Long.parseLong(queueId);
        } catch (NumberFormatException e) {
            String errorMessage = String.format("QueueId %s isn't a number. Please check your inputs", queueId);
            LOGGER.info(errorMessage);
            rsp.sendError(404, errorMessage);
            return;
        }

        // Logic to check if the build has started
        if (BuildUtils.buildStarted(id)) {
            String newUrl = BuildUtils.buildUrl(job, id);
            if (newUrl == null) {
                String errorMessage = String.format("No build with a queue id %d was found in the job %s", id, job.getName());
                LOGGER.info(errorMessage);
                rsp.sendError(404, errorMessage);
                return;
            }
            rsp.sendRedirect2(newUrl);
        } else {
            rsp.sendError(404, "Build not started yet " + queueId);
        }
    }
}

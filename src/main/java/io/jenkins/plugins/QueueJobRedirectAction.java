package io.jenkins.plugins;

import hudson.model.Action;
import hudson.model.Job;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.verb.GET;

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
        return "QueueJobRedirect";
    }

    @Override
    public String getUrlName() {
        return "from-queue";
    }

    @GET
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
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

        // Logic to check if the build has started
        if (BuildUtils.buildStarted(id)) {
            Optional<String> newUrl = BuildUtils.buildUrl(job, id);
            if (newUrl.isEmpty()) {
                String errorMessage =
                        String.format("No build with a queue id %d was found in the job %s", id, job.getName());
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

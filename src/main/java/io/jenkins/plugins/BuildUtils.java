package io.jenkins.plugins;

import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Run;
import java.util.Collection;
import java.util.logging.Logger;
import jenkins.model.Jenkins;

public class BuildUtils {
    private static final Logger LOGGER = Logger.getLogger(BuildUtils.class.getName());

    public static boolean buildStarted(Long queueId) {
        // Get the queue item
        Queue.Item item = Jenkins.get().getQueue().getItem(queueId);
        if (item == null) {
            // If the item is not in the queue, it might have started
            return true;
        }

        // Maybe the job was just started and is now in the LeftItem queue
        Collection<Queue.LeftItem> leftItems = Jenkins.get().getQueue().getLeftItems();
        for (Queue.LeftItem leftitem : leftItems) {
            if (leftitem.getId() == queueId) {
                return true;
            }
        }

        LOGGER.info("Found queueId in normal Queue " + queueId);
        return false;
    }

    public static String buildUrl(Job<?, ?> job, Long queueId) {
        LOGGER.info("Looking for Job that had the queue " + queueId);
        for (Run<?, ?> run : job.getBuilds()) {
            if (run.getQueueId() == queueId) {
                // Construct the URL for the build
                return Jenkins.get().getRootUrl() + run.getUrl();
            }
        }

        // If no matching build is found, return null or an appropriate message
        return null;
    }
}

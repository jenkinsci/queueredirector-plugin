package io.jenkins.plugins;

import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Run;
import java.util.Collection;
import java.util.Optional;
import jenkins.model.Jenkins;

public class BuildUtils {
    public static boolean buildStarted(Long queueId) {
        if (Jenkins.getInstanceOrNull() == null) {
            // Jenkins is not running
            return false;
        }
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

        return false;
    }

    public static Optional<String> buildUrl(Job<?, ?> job, Long queueId) {
        if (Jenkins.getInstanceOrNull() == null) {
            // Jenkins is not running
            return Optional.empty();
        }

        for (Run<?, ?> run : job.getBuilds()) {
            if (run.getQueueId() == queueId) {
                // Construct the URL for the build
                return Optional.of(Jenkins.get().getRootUrl() + run.getUrl());
            }
        }

        // If no matching build is found, return null or an appropriate message
        return Optional.empty();
    }

    public static Optional<String> buildUrl(Long queueId) {
        if (Jenkins.getInstanceOrNull() == null) {
            // Jenkins is not running
            return Optional.empty();
        }
        for (Job<?, ?> job : Jenkins.get().getAllItems(Job.class)) {
            for (Run<?, ?> run : job.getBuilds()) {
                if (run.getQueueId() == queueId) {
                    // Construct the URL for the build
                    return Optional.of(Jenkins.get().getRootUrl() + run.getUrl());
                }
            }
        }

        // If no matching build is found, return null or an appropriate message
        return Optional.empty();
    }
}

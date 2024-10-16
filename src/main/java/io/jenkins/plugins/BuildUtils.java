package io.jenkins.plugins;

import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Run;
import java.util.Optional;
import jenkins.model.Jenkins;

public class BuildUtils {

    public static boolean buildStarted(Long queueId) {
        if (Jenkins.getInstanceOrNull() == null) {
            return false;
        }

        // Check if the item is in the queue
        Queue queue = Jenkins.get().getQueue();
        Queue.Item item = queue.getItem(queueId);
        if (item == null) {
            // If the item is not in the queue, it might have started
            return true;
        }

        // Check if the job was started and is now in the LeftItem queue
        return queue.getLeftItems().stream().anyMatch(leftItem -> leftItem.getId() == queueId);
    }

    public static Optional<String> buildUrl(Job<?, ?> job, Long queueId) {
        if (Jenkins.getInstanceOrNull() == null || job == null) {
            return Optional.empty();
        }

        return job.getBuilds().stream()
                .filter(run -> run.getQueueId() == queueId)
                .findFirst()
                .map(run -> Jenkins.get().getRootUrl() + run.getUrl());
    }

    public static Optional<String> buildUrl(Long queueId) {
        if (Jenkins.getInstanceOrNull() == null) {
            return Optional.empty();
        }

        for (Job<?, ?> job : Jenkins.get().getAllItems(Job.class)) {
            Run<?, ?> run = job.getLastBuild();
            while (run != null && run.getQueueId() >= queueId) {
                if (run.getQueueId() == queueId) {
                    return Optional.of(Jenkins.get().getRootUrl() + run.getUrl());
                } else {
                    run = run.getPreviousBuild();
                }
            }
        }

        return Optional.empty();
    }
}

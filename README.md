# Queue Redirector Plugin for Jenkins

The Queue Redirector plugin adds a new route to Jenkins that allows users to redirect a queued build to its corresponding build page once the job has started.

[![Build Status](https://ci.jenkins.io/job/Plugins/job/queueredirector-plugin/job/main/badge/icon)](https://ci.jenkins.io/job/Plugins/job/queueredirector-plugin/job/main/)

## Overview

When a build is added to the queue through a remote call, a `queueid` is returned to the caller. Once the job starts, it leaves the queue and enters the `LeftQueue`, where it remains for only five minutes. After this period, there is no way to trace the original `queueid` to the `jobid`.

The Queue Redirector plugin solves this problem by adding a new route that can take a `queueid`, perform a lookup, and redirect to the corresponding build page if the `jobid` is found. This enables you to queue a job using the REST API, receive a `queueid`, and provide users with a URL that will always resolve to the correct build.

## New Route

The new route is accessible either from a specific job or from the Jenkins instance itself:

- **Job-specific route:**  
  Example: `http://localhost:8080/jenkins/job/jobA/from-queue?queueid=123`  
  This approach is faster, as the lookup is limited to fewer builds.

- **Instance-wide route:**  
  Example: `http://localhost:8080/jenkins/from-queue?queueid=123`

## Troubleshooting

### Accessing Logs

The plugin uses `INFO` and above levels to report events that may require user attention. For debugging purposes, set the logging level to `FINEST`. Note that the `ALL` level is not sufficient to print these messages.

To configure logging for the Queue Redirector plugin in the Jenkins UI, go to:

**Manage Jenkins > System Log > New Log Recorder**, and use `jenkins.plugins.queueredirector` as the logger name.

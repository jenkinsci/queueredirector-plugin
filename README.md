# Queue Redirector plugin for Jenkins

Adds new route to redirect a queued build to the actual build page once the jkob has started 
  
[![Build Status](https://ci.jenkins.io/job/Plugins/job/queueredirector-plugin/job/main/badge/icon)](https://ci.jenkins.io/job/Plugins/job/queueredirector-plugin/job/main/)

## Essentials

When a build is added to the queue from a remote call, only a queueid is returned to the caller.
When a job starts, it leaves the queue, to enter the LeftQueue, and stays there for only 5 minutes.
After that time, there is no way to know what jobid was associated with the queueid

This plugin adds a new route to the job that takes a queueid, performs a lookup, and if the jobid is found, automatically redirects to the job page
This allows you to queue a job using the REAT Api, receive the Queuid, and provide your users with a URL that will always resolve to the proper build.
  
New route is available either From the job itself, or from the instance.
If using the job version, lookup will be faster since there are less builds in the search list.

http://localhost:8080/jenkins/job/jobA/from-queue?queueid=123
http://localhost:8080/jenkins/from-queue?queueid=123

### Troubleshooting

#### Accessing logs

Plugin uses `INFO` and above levels to report things user should worry
about. For debugging, set it to `FINEST` - note the `ALL` level is not
sufficient to print these. To configure OpenStack plugin logging in
Jenkins UI go to *Manage Jenkins \> System Log \> New Log Recorder* and
use `jenkins.plugins.queueresirector` as the logger name.







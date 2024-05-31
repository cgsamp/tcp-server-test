# TCP Router testing

Building an app to deploy to Cloud Foundry that listens to a certain port and reads the input. In a couple versions.

## TCP Routing on CF


### Enabling TCP Routing

https://docs.cloudfoundry.org/adminguide/enabling-tcp-routing.html

In TAS, check the appropriate boxes in OpsMan. Optionally, increase the allowable port range to include 48556.

### Adjust router groups

CF TCP Routing by default delivers TCP traffic from any source to port 8080. This can be changed.

https://docs.cloudfoundry.org/devguide/custom-ports.html

`cf curl /routing/v1/router_groups/0d3d58c3-9f3f-4e3b-4df0-8cecc1c5a37a -d @update_router_group.json -X PUT`

 ## Running Locally

Start with 
`mvn clean spring:boot run`
then
`nc localhost 8080 < testfile.bin`

### Log example

```
2024-05-31T14:16:05.014-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Started App in 0.683 seconds (process running for 1.011)
2024-05-31T14:16:05.016-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Starting APP
2024-05-31T14:16:05.035-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Listening at 0.0.0.0 on port 8080
2024-05-31T14:16:06.949-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Received connection from /192.168.1.14:56192
2024-05-31T14:16:06.949-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Listening at 0.0.0.0 on port 8080
2024-05-31T14:16:37.570-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Received connection from /[0:0:0:0:0:0:0:1]:51958
2024-05-31T14:16:37.570-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Listening at 0.0.0.0 on port 8080
2024-05-31T14:16:37.570-04:00  INFO 86021 --- [pool-2-thread-3] tcproutes.lab.sampsoftware.net.App       : bytesRead=9
2024-05-31T14:16:37.571-04:00  INFO 86021 --- [pool-2-thread-3] tcproutes.lab.sampsoftware.net.App       : TThe incoming request is:[0x00 0x00]
2024-05-31T14:16:56.456-04:00  INFO 86021 --- [           main] tcproutes.lab.sampsoftware.net.App       : Listening at 0.0.0.0 on port 8080
```

## Deploying to CF

### Create Domain

`cf create-shared-domain test.tcp.tas.lab.sampsoftware.net --router-group default-tcp`

Ensure that the router group is updated to use the ports required if using custom ports.

### CF Push

`mvn clean package && cf push`
`cf logs very-simple-tcp-server`
`cf routes`
`nc test.tcp.tas.lab.sampsoftware.net 30000 < testfile.bin`

...Nothing!

### Logs
```
2024-05-31T14:26:11.37-0400 [APP/PROC/WEB/0] OUT 2024-05-31T18:26:11.369Z  INFO 8 --- [           main] tcproutes.lab.sampsoftware.net.App       : Starting App v1.0-SNAPSHOT using Java 17.0.9 with PID 8 (/home/vcap/app/BOOT-INF/classes started by vcap in /home/vcap/app)
2024-05-31T14:26:11.37-0400 [APP/PROC/WEB/0] OUT 2024-05-31T18:26:11.373Z  INFO 8 --- [           main] tcproutes.lab.sampsoftware.net.App       : The following 1 profile is active: "cloud"
2024-05-31T14:26:11.45-0400 [APP/PROC/WEB/0] OUT 2024-05-31T18:26:11.456Z  INFO 8 --- [           main] i.p.c.p.CloudProfileApplicationListener  : 'cloud' profile activated
2024-05-31T14:26:12.39-0400 [APP/PROC/WEB/0] OUT 2024-05-31T18:26:12.393Z  INFO 8 --- [           main] tcproutes.lab.sampsoftware.net.App       : Started App in 1.605 seconds (process running for 2.255)
2024-05-31T14:26:12.40-0400 [APP/PROC/WEB/0] OUT 2024-05-31T18:26:12.407Z  INFO 8 --- [           main] tcproutes.lab.sampsoftware.net.App       : Starting APP
2024-05-31T14:26:12.42-0400 [APP/PROC/WEB/0] OUT 2024-05-31T18:26:12.418Z  INFO 8 --- [           main] tcproutes.lab.sampsoftware.net.App       : Listening at 0.0.0.0 on port 8080
```

### Comments

Why nothing?

If I create a http route, the app does receive traffic.


# artemis-lock-test

Build the project

```
mvn clean package
```

Copy the app to both the active and the passive machines. Run one instance on the active machine, and one on the passive machine. Point them both to the shared storage. When you kill the instance (`ctrl+c`) on the active, you will see the stop time in ms. On the passive machine, you will see the lock time in ms. If you subtract the active stop time from the passive lock time, you can get the total time for the failover (within the margin of NTP clock drift).

```
java -jar artemis-lock-test-1.0.0-runner.jar <path_to_directory_on_shared_mount>
```
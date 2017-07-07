Reactive Flight Service
==============

A gatling project used for carrying out load/performance test on the reactive and non reactive flight service versions.
For now only the test endpoint <<host>>/apitest/airlines is being load-tested since it retrieves airlines data from the repository without exectuting any further application logic. Thus the performance gap between the reactive and non-reactive implementation can be better disclosed.

Run Engine.scala to start a load test with 100 users against the non-reactive service implementation. The test result can be viewed in the folder .\target\results.

Run EngineReactive.scala to start a load test with 100 users against the reactive application implementation. The test result can be viewed in the folder .\target\results.
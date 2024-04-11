# TokenRingUDP

## Description

This Java project demonstrates the basic usage of the UDP protocol by establishing a
token ring network between multiple nodes. The ring is formed dynamically with a first
node serving as the ring leader. The leader is responsible for sending the first token
the moment a second node connects. Any subsequent node can join the ring by connecting
to the leader or any other node that is already part of the ring. A token is forwarded by a ring node
1 second after receiving it. Every time a node receives a token, it prints a message
to the console including the sequence number, the current size of the ring, and the IP
addresses and port number of all nodes in the ring.

## Usage

The implementation has been compiled against Java 21, although no special features are
used that would prevent it from running on older versions. The `Jackson` package is used
for JSON serialization and deserialization. To ease the process of running the program,
a *fatjar* has been created that includes all dependencies. The jar file `TokenRingUDP.jar`
has been added to the root of the repository.

The ring leader can be started by just running `java -jar TokenRingUDP.jar`. The
leader then prints its IP address and port number to the console. Any subsequent
node can join by running `java -jar TokenRingUDP.jar <ip> <port>` for any `<ip>`
and `<port`> of a node already part of the ring.



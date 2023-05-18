# FramedTCP
A simple Java networking library that encapsulates messages using length-prefixing

## The problem
When you send messages over TCP sockets, you're not guaranteed to receive those messages exactly how you sent them.
TCP only guarantees that you'll receive all packets in the correct order, but does not keep bounderies between messages.
This means that if you were to read the entire buffer, you may get a partial message or multiple messages at once.

## The solution
A possible solution is to encapsule messages into an application-level frame that also carries the **length** of the message. This way, the receiver always knows where a message starts and where it ends.

## Build
This project uses Maven as its build system.

## How to use
The library provides a series of classes that you can use to take advantage of its features. Unfortunatelly these are **NOT** standardized for now.
This is how you build an echo server, for example.

### Server
```java
try (ServerSocket serverSocket = new ServerSocket(8000)) {
    Socket client = serverSocket.accept();
    FramedReader reader = new FramedReader(new InputStreamReader(client.getInputStream()));
    FramedWriter writer = new FramedWriter(new OutputStreamWriter(client.getOutputStream()));
    writer.writeBytes(reader.readBytes());
} catch (Exception e) {
    throw new RuntimeException(e);
}
```
### Client
```java
try (Socket socket = new Socket("", 8000)) {
    FramedReader reader = new FramedReader(new InputStreamReader(socket.getInputStream()));
    FramedWriter writer = new FramedWriter(new OutputStreamWriter(socket.getOutputStream()));
    writer.writeString("Hello world");
    System.out.println("ECHO: " + reader.readString());
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

## License
This projects is license under the MIT License.

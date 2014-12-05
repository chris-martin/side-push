side-push
=========

HTTP server push - for any platform.

It's a pain to get websocket/comet support into your web server,
so put it next to your web server instead.

How it works
------------

Side-push runs as a standalone server. When your traditional web
server wants to send a message to clients, send it to the side-push
server's REST API, which will forward it along to the client.

    +------------+    Push messages     +-------------+
    | Web server | -------------------> |  side-push  |
    +------------+                      +-------------+
             ^ |                          ^ |
        HTTP | | HTTP          Initialize | | Forwarded
     request | | response      connection | | messages
             | v                          | v
           +----------------------------------+
           |              Client              |
           +----------------------------------+

Configuration
-------------

Defaults:

```yml
listen-for-client-connections:
  host: "::0"
  port: 8081
  path: "/"

listen-for-messages:
  host: "::0"
  port: 8082
  path: "/"
```

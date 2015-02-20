side-push
=========

HTTP server push — for any platform.

It's a pain to get websocket support into your web server,
so put it next to your web server instead.

How it works
------------

Side-push runs as a standalone server. When your traditional web
server wants to send a message to clients, send it to the side-push
server's REST API, which will forward it along to the client.

    ╭────────────╮    push        ╭─────────────╮
    │   web      │    messages    │  side-push  │
    │   server   │ ━━━━━━▶━━━━━━━━┿━━┓          │
    ╰────────────╯                ╰──╂──────────╯
              ▲                      ┃
        plain │                      ┃ forwarded
        HTTP  │                      ┃ messages
              ▼                      ▼
           ╭────────────────────────────╮
           │           client           │
           ╰────────────────────────────╯

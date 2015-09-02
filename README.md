# lispjs-bot

This is a simple IRC bot written in ClojureScript. Oh my. It is quite messy. Please excuse the mess while I figure out how to wrap the node-irc library in ClojureScript in a nice way.

## Usage

???

Set these ENV vars: (shown here in a .env compatible format

```bash
IRC_SERVER=irc.freenode.org
USERNAME=lispjs
PASSWORD=CHANGEME
CHANNEL_LIST="#symbolics,#madeupchannel"
```

`CHANNEL_LIST` should be a comma-separated list, with hash symbols included.

## License

Copyright (c) 2015 Matt Gauger.

Released under an MIT License.

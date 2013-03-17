#!/usr/bin/env groovy
def cli = new CliBuilder(usage: 'CommandLineClient.groovy -s <server> -p <port>')
cli.with {
    h longOpt: 'help',   'Show usage information'
    s longOpt: 'server', args: 1, 'The server where to connect. Defaults to localhost'
    p longOpt: 'port',   args: 1, 'The port on which to connect to the server. Defaults to 4444'
}
def options = cli.parse(args)
if (!options || options.h) {
    cli.usage()
    System.exit(0)
}

port = options.p ?: 4444
server = options.s ?: "localhost"

println "Using server ${server}:${port}"

def running = true

def reader = System.in.newReader()
reader.withReader {

    while (running) {
        print "Enter your input: "
        def text = it.readLine()

        if (text == "quit" || text == "q") {
            running = false
        }
        else if (text == "help" || text == "h") {
            println ">  send X \t\t - send message X to server"
            println ">  help \t\t - show this message"
            println ">  quit \t\t - quit application"
        }
        else if (text.startsWith("send")) {
            send(text)
        }

    }
}

println "Exiting"
System.exit(0)


void send(def userInput) {
    def message = userInput.split(" ")[1]

    def s = openSocket()

    s.withStreams { input, output ->
        output << "${message}\n"
        buffer = input.newReader().readLine()
        println "response = $buffer"
    }
}

def openSocket() {
    try {
        s = new Socket(server, port);
    }
    catch (java.net.ConnectException e) {
        println "Cannot connect to ${server} on ${port} : ${e.getMessage()}"
        running = false
    }
    s
}
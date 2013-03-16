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
    def port = 4444
    def server = "localhost"
    try {
        s = new Socket(server, port);
    }
    catch (java.net.ConnectException e) {
        println "Cannot connect to ${server} on ${port} : ${e.getMessage()}"
        running = false
    }
    s
}
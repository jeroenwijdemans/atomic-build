Atomic-Build
============

Atomic build is a project that allows project members to fire virtual atomic bombs to team members or get bombed by applications such as Hudson.

The server sends out alarm signals. Signals are "Stand-down", "Alarm Phase 1", "Alarm Phase 2", "Alarm Phase 3", "Nuclear war".

The server has a way to convert incoming calls to an signal send to the clients.

Incoming signal can arrive from: command line, a Hudson Plugin or custom hardware via USB.

A typical scenario would be:
- Hudson sending a build failure call.
- Alarm phase 1 is send
- Hudson sending new failure call
- Alarm phase 2 i send
- 30 seconds later alarm is raised to 3
- 15 seconds later the client receive a nuclear warhead

or

- Hudson sending a build failure call.
- Alarm phase 1 is send
- Hudson sending new failure call
- Alarm phase 2 I send
- The custom hardware is used to override and alarm phase with stand down
- Server sends distress call to custom hardware


Applications
============

The entire server with custom hardware should be no larger than 40 x 30 x 20 cm

Server
------

Small daemon application that can run on a Rasberry Pi

Can have different plugins for operations
- command line input + output
- custom hardware plugin
- Hudson

Uses the Eth to communicate with Hudson and clients

Signal to clients are UDP packages

Clients connect via a pub sub mechanism. Doing a search on port 37591

Written in Java / Groovy / Scala / Clojure

Custom hardware
---------------

Has a button to signal stand down
Has a speaker to play Alarm Phase 3 music
Has a speaker to play stand down music
Has a alarm light for Alarm Phase 2 and 3
Has a blinking distress light

Hardware is operated via a plugin on the server wired via USB.

Clients
-------

When the nuclear war is passed a full screen video of an Atomic Explosian is shown. Second before the explosion the
screen is already shaking and giving bits of static.

Windows 7

A daemon running somewhere small and visible. Can display Green/Yellow/Red.
Written in .NET or C#/C++

OSX

A daemon running somewhere small and visible. Can display Green/Yellow/Red
Written in Objective C

Linux

Daemon only
In Java or C

Tablets

In Android or Objective C

Goal
====

The goal of this application

- do low level networking (udp)
- learn how to create a windows application
- learn how to create a osx application
- work with the Pi
- DIY wood + electronics + lights
<div align="center">
<h3>HACKLYMPICS</h3>
<img src="https://github.com/aesophor/Hacklympics/raw/master/.meta/Dashboard.png">

`OnlineJudge` `Keylogging` `ScreenRecording` `REST` `Java8` `Python3` `Django1.8` `Material UI`
</div>

<br>

## What's This
* This was my undergrad project while I was a [cadet](https://en.wikipedia.org/wiki/Cadet) at [National Defense University](https://zh.wikipedia.org/wiki/%E5%9C%8B%E9%98%B2%E5%A4%A7%E5%AD%B8_(%E4%B8%AD%E8%8F%AF%E6%B0%91%E5%9C%8B)) (Jun. 2015 ~ Jun. 2018)
* I left the R.O.C. army in Jun. 2018 due to my disappointment in them.
* I transferred to University of Taipei in Aug. 2018, and got admitted to [NCTU](https://www.nycu.edu.tw/en/) in Nov. 2019
* This is my complete rewrite of [its predecessor](https://github.com/Max-Projects) which was written by six of my seniors
* A programming exam platform with **online judge and anticheat**
* Can prevent students from cheating (using StackOverflow, LINE, etc) with keylogging and continuous screen capture

<br>

## Overview
* Three-tier [REST architecture](https://en.wikipedia.org/wiki/Representational_state_transfer)
  - **Hacklympics** - Client (Java FX)    
  - **HacklympicsAPI** - RESTful API (Java 8)    
  - **Nocturnal** - Backend (Python 3.5 + Django 1.8.18)    

* [Event-driven Architecture](https://en.wikipedia.org/wiki/Event-driven_architecture)    
  > When there's a change in state at the server side, the server will notify all relevant clients by "dispatching events" to them.
  When a client receives an event from the server, it will take appropriate actions in order to reflect the changes to the user.    
  - server-side event dispatcher: [dispatcher.py](https://github.com/aesophor/hacklympics/blob/master/Nocturnal/hacklympics/events/dispatcher.py)    
  - client-side event manager: [EventManager.java](https://github.com/aesophor/hacklympics/blob/master/HacklympicsAPI/src/com/hacklympics/api/event/EventManager.java)     

* Design Patterns used
  - Singleton    
  - Observer    

* Libraries used
  - **[okhttp](https://github.com/square/okhttp)** - HTTP+HTTP/2 client for Android and Java applications.    
  - **[gson](https://github.com/google/gson)** -  JSON Library. Used for passing data between Django and HacklympicsAPI    
  -  **[java-diff-utils](https://code.google.com/archive/p/java-diff-utils/#!)** - Diff Patch Library. Used for syncing keylogs.    
  -  **[thumbnailator](https://github.com/coobird/thumbnailator)** - Thumbnail generation library. Used for down-scaling snapshots.    
  - **[JFoenix](https://github.com/jfoenixadmin/JFoenix)** - JavaFX Material Design Library    
  - **[FontawesomeFX](https://bitbucket.org/Jerady/fontawesomefx)** - Icon Library    
  - **[RichTextFX](https://github.com/FXMisc/RichTextFX)** - RichText Area for JavaFX. Used for code areas    
  - **[TerminalFX](https://github.com/javaterminal/TerminalFX)** - A Terminal Emulator written in JavaFX 8    

<br>

## Features
* Users with Teacher's privileges can create/edit/remove Courses/Exams/Problems.
  * each Course contains Exam(s)
  * each Exam contains Problem(s)
* Server-side **Online Judge** (implemented in  [judge.py](https://github.com/aesophor/Hacklympics/blob/master/Nocturnal/hacklympics/judge.py))
  * Automatic score assiging (not implemented yet)
* Server-side **countdown timer for each Exam** (implemented in [session.py](https://github.com/aesophor/Hacklympics/blob/9b3166e13aca28ca29bd304db087336081c48a45/Nocturnal/hacklympics/sessions.py#L85).
  * when the time's up, the exam will halt automatically.
* **Screen recording** and **keylogging** targeting students while they are in exams
  * no more visiting StackOverflow! Everything is under surveillence!
  * taking snapshots
  * syncing keystrokes via [diff-patches](https://github.com/aesophor/Hacklympics/blob/master/Hacklympics/src/com/hacklympics/common/code).
* Each exam has its own **chatroom**
  *  all messages are public
  * useful for students asking teachers questions
  * togglable toast notifications (not implemented yet)

<br>

## Supported Programming Languages
* Currently only Java is supported since this project mainly focuses on the proctoring mechanisms.

<br>

## Usage
1. Clone this repo
```
$ git clone https://github.com/aesophor/hacklympics
$ cd hacklympics
```

2. Start the django server on 0.0.0.0:8000
```
$ cd Nocturnal && ./start_server.sh
```

3. Open another terminal, cd to hacklympics directory, and run the desktop client.
```
$ cd Hacklympics/dist
$ java -jar Hacklympics.jar
```

Note: Please edit `Hacklympics/dist/config.properties` to change where the desktop client will connect to.

<br>

## Gallery
Please visit [imgur](https://imgur.com/a/wuw9Yaa) for more screenshots.
<div align="center">
    <img src="https://github.com/aesophor/Hacklympics/raw/master/.meta/teacher/EditMaterials - Course.png" alt="scrot1"><br>Editing a Course
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/.meta/teacher/Proctor - Snapshots.png" alt="scrot2"><br>Student screens in real time
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/.meta/teacher/Proctor - Keystrokes.png" alt="scrot3"><br>Syncing keylogs & playback
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/.meta/student/TakeExam - Code.png" alt="scrot4"><br>Full-fledged code editor for students
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/.meta/student/TakeExam - OnlineJudge.png" alt="scrot5"><br>Passing an exam with Online Judge
    <br><br>

</div>

<br>

## License
Available under [Mozilla Public License 2.0](https://github.com/aesophor/hacklympics/blob/master/LICENSE).

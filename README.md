<div align="center">
<h3>HACKLYMPICS</h3>
<img src="https://github.com/aesophor/Hacklympics/raw/master/assets/Dashboard.png">

`OnlineJudge` `Keylogging` `ScreenRecording` `REST` `Java8` `Python3` `Django1.8` `Material UI`
</div>

## What's This
* This is my former undergraduate project while I was a cadet at NDU (National Defense University)
* My complete rewrite of [its predecessor](https://github.com/Max-Projects) which was written by six of my seniors

## Overview
* Three-tier [REST architecture](https://en.wikipedia.org/wiki/Representational_state_transfer)
  > **Hacklympics** - Client (Java FX)    
  > **HacklympicsAPI** - RESTful API (Java 8)    
  > **Nocturnal** - Backend (Python 3.5 + Django 1.8.18)    

 * Design Patterns used
   > Singleton    
   > Observer    

* Libraries used
  
  > **[okhttp](https://github.com/square/okhttp)** - HTTP+HTTP/2 client for Android and Java applications.    
  > **[gson](https://github.com/google/gson)** -  JSON Library. Used for passing data between Django and HacklympicsAPI    
  >  **[java-diff-utils](https://code.google.com/archive/p/java-diff-utils/#!)** - Diff Patch Library. Used for syncing keylogs.    
  >  **[thumbnailator](https://github.com/coobird/thumbnailator)** - Thumbnail generation library. Used for down-scaling snapshots.    
  > **[JFoenix](https://github.com/jfoenixadmin/JFoenix)** - JavaFX Material Design Library    
  > **[FontawesomeFX](https://bitbucket.org/Jerady/fontawesomefx)** - Icon Library    
  > **[RichTextFX](https://github.com/FXMisc/RichTextFX)** - RichText Area for JavaFX. Used for code areas    
  > **[TerminalFX](https://github.com/javaterminal/TerminalFX)** - A Terminal Emulator written in JavaFX 8    

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

## Supported Languages
* Java

Supports for more langugages will be added over time.


## Gallery
Please visit [imgur](https://imgur.com/a/wuw9Yaa) for more screenshots.
<div align="center">
    <img src="https://github.com/aesophor/Hacklympics/raw/master/assets/teacher/EditMaterials - Course.png" alt="scrot1"><br>Editing a Course
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/assets/teacher/Proctor - Snapshots.png" alt="scrot2"><br>Student screens in real time
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/assets/teacher/Proctor - Keystrokes.png" alt="scrot3"><br>Syncing keylogs & playback
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/assets/student/TakeExam - Code.png" alt="scrot4"><br>Full-fledged code editor for students
    <br><br>
    <img src="https://github.com/aesophor/Hacklympics/raw/master/assets/student/TakeExam - OnlineJudge.png" alt="scrot5"><br>Passing an exam with Online Judge
    <br><br>

</div>

## License
Available under [Mozilla Public License 2.0](https://github.com/aesophor/hacklympics/blob/master/LICENSE).


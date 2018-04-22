# Hacklympics
My undergraduate project in Computer Science.    
An **Online Exam System**  for taking/monitoring programming exams.

Attempting to recreate [this project](https://github.com/Max-Projects) from scratch.
<p align="center">
    <img src="https://i.imgur.com/7lmcEDr.png" alt="Screenshot">
</p>

- [ ] Online chatting room for all users.
- [x] Teachers can create/edit/remove their courses/exams/problems.
- [ ] Teachers can **proctor** any exams, watching the students write their code LIVE.
- [ ] Teachers can set up input/output data for each problems (Online Judge)
- [ ]  Students can write and sumbit their codes to the server.
- [ ]  Students can compile and execute their codes (without the need to fire up a terminal).

## Features
* Three-tier [REST architecture](https://en.wikipedia.org/wiki/Representational_state_transfer)
  > **Hacklympics** - Frontend (Java 8)    
  > **HacklympicsAPI** - RESTful API (Java 8)    
  > **Nocturnal** - Backend (Python 3.5 + Django 1.8.18)

* Library used
  > **[JFoenix](https://github.com/jfoenixadmin/JFoenix)** - JavaFX Material Design Library
  > **[GSON](https://github.com/google/gson)** -  JSON Library. Used for passing data between Django and HacklympicsAPI
  > **[FontawesomeFX](https://bitbucket.org/Jerady/fontawesomefx)** - Icon Library

 * Design Pattern used
   > Singleton Pattern

## Screenshots
<p align="center">
	<img src="https://i.imgur.com/MXWv148.png" alt="login-panel-scrot"> <br>
    <h5 align="center">▲ Login Panel</h5> <br><br>
    <img src="https://i.imgur.com/7lmcEDr.png" alt="teacher-scrot"><br>
    <h5 align="center">▲  Teachers can create/edit/remove their courses/exams/problems.</h5> <br><br>
    <img src="https://i.imgur.com/WN8heIh.png" alt="edit"><br>
    <h5 align="center">▲  Teacher editing an existing course. Hold shift to enable multiple selection.</h5> <br><br>
    <img src="https://i.imgur.com/yT58bNq.png" alt="edit"><br>
    <h5 align="center">▲ Material Design</h5> <br><br>
</p>

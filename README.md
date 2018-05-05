# Hacklympics
My undergraduate project in Computer Science.    
An **Online Exam System**  for taking/monitoring programming exams.

Attempting to recreate [this project](https://github.com/Max-Projects) from scratch.
<p align="center">
    <img src="https://i.imgur.com/GtwwIaW.png" alt="Screenshot">
</p>

- [ ] Online chatting room for all users.
- [x] Teachers can create/edit/remove their courses/exams/problems.
- [ ] Teachers can **proctor** any exams, watching the students write their code LIVE.
- [x] Teachers can set up input/output data for each problems (Online Judge)
- [x]  Students can write and sumbit their codes to the server.
- [x]  Students can compile and execute their codes (without the need to fire up a terminal).

## Features
* Three-tier [REST architecture](https://en.wikipedia.org/wiki/Representational_state_transfer)
  > **Hacklympics** - Frontend (Java 8)    
  > **HacklympicsAPI** - RESTful API (Java 8)    
  > **Nocturnal** - Backend (Python 3.5 + Django 1.8.18)    

* Library used
  > **[JFoenix](https://github.com/jfoenixadmin/JFoenix)** - JavaFX Material Design Library    
  > **[GSON](https://github.com/google/gson)** -  JSON Library. Used for passing data between Django and HacklympicsAPI    
  > **[FontawesomeFX](https://bitbucket.org/Jerady/fontawesomefx)** - Icon Library    
  > **[RichTextFX](https://github.com/FXMisc/RichTextFX)** - RichText Area for JavaFX. Used for code areas    
  > **[TerminalFX](https://github.com/javaterminal/TerminalFX)** - A Terminal Emulator written in JavaFX 8    

 * Design Pattern used
   > Singleton Pattern


## Screenshots
<p align="center">
	<img src="https://i.imgur.com/MXWv148.png" alt="login-panel-scrot"> <br>
    <h5 align="center">▲ Login Panel</h5> <br><br>
    <img src="https://i.imgur.com/VUhQ0hX.png" alt="edit"><br>
    <h5 align="center">▲ Student submitting his code.</h5> <br><br>
    <img src="https://i.imgur.com/xNDU0zO.png" alt="edit"><br>
    <h5 align="center">▲  Student passing a exam.</h5> <br><br>
    <img src="https://i.imgur.com/QUpV7gj.png" alt="teacher-scrot"><br>
    <h5 align="center">▲  Teachers can create/edit/remove their courses/exams/problems.</h5> <br><br>
    <img src="https://i.imgur.com/b5J1JoX.png" alt="edit"><br>
    <h5 align="center">▲  Teacher editing an existing problem.</h5> <br><br>
</p>


## License
<img src="https://i.imgur.com/sWzVHFt.png" alt="login-panel-scrot"> <br>
Available under [Mozilla Public License 2.0](https://github.com/aesophor/hacklympics/blob/master/LICENSE).
